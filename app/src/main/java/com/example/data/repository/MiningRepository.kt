package com.example.data.repository

import com.example.data.local.MiningDao
import com.example.data.model.CryptoCurrency
import com.example.data.model.MiningProfileEntity
import com.example.data.model.RigUpgradeEntity
import com.example.data.model.SavedAddressEntity
import com.example.data.model.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class MiningRepository(
    private val dao: MiningDao,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

    val profileFlow: Flow<MiningProfileEntity?> = dao.getProfileFlow()
    val transactionsFlow: Flow<List<TransactionEntity>> = dao.getAllTransactionsFlow()
    val upgradesFlow: Flow<List<RigUpgradeEntity>> = dao.getAllUpgradesFlow()
    val savedAddressesFlow: Flow<List<SavedAddressEntity>> = dao.getSavedAddressesFlow()

    // Live hashrate jitter simulation for realism
    private val _liveHashrate = MutableStateFlow(94.8)
    val liveHashrate = _liveHashrate.asStateFlow()

    private var miningJob: Job? = null

    init {
        startMiningLoop()
    }

    private fun startMiningLoop() {
        miningJob?.cancel()
        miningJob = scope.launch {
            while (isActive) {
                delay(1200)
                val current = dao.getProfile() ?: MiningProfileEntity()
                if (current.isMining) {
                    val multiplier = if (current.isTurbo) 1.35 else 1.0
                    val jitter = (Random.nextDouble(-1.2, 1.4))
                    val effectiveHashrate = ((current.baseHashrate + jitter) * multiplier).coerceAtLeast(10.0)
                    _liveHashrate.value = (effectiveHashrate * 100).toLong() / 100.0

                    // Calculate small reward per tick
                    // reward per MH/s per tick in respective coin units
                    val coin = CryptoCurrency.fromSymbol(current.activeCoinSymbol)
                    val basePerMhUsd = 0.00000035 * multiplier
                    val rewardUsd = effectiveHashrate * basePerMhUsd
                    val coinReward = rewardUsd / coin.currentPriceUsd

                    val newTotalHashes = current.totalHashesCalculated + (effectiveHashrate * 1200).toLong()
                    val newAccepted = if (Random.nextInt(0, 10) == 0) current.acceptedShares + 1 else current.acceptedShares

                    val updatedProfile = when (coin) {
                        CryptoCurrency.BTC -> current.copy(
                            btcBalance = current.btcBalance + coinReward,
                            totalHashesCalculated = newTotalHashes,
                            acceptedShares = newAccepted,
                            temperatureC = if (current.isTurbo) 67 else 58,
                            lastUpdated = System.currentTimeMillis()
                        )
                        CryptoCurrency.ETH -> current.copy(
                            ethBalance = current.ethBalance + coinReward,
                            totalHashesCalculated = newTotalHashes,
                            acceptedShares = newAccepted,
                            temperatureC = if (current.isTurbo) 66 else 57,
                            lastUpdated = System.currentTimeMillis()
                        )
                        CryptoCurrency.SOL -> current.copy(
                            solBalance = current.solBalance + coinReward,
                            totalHashesCalculated = newTotalHashes,
                            acceptedShares = newAccepted,
                            temperatureC = if (current.isTurbo) 68 else 59,
                            lastUpdated = System.currentTimeMillis()
                        )
                        CryptoCurrency.USDT -> current.copy(
                            usdtBalance = current.usdtBalance + coinReward,
                            totalHashesCalculated = newTotalHashes,
                            acceptedShares = newAccepted,
                            temperatureC = if (current.isTurbo) 65 else 56,
                            lastUpdated = System.currentTimeMillis()
                        )
                        CryptoCurrency.KAS -> current.copy(
                            kasBalance = current.kasBalance + coinReward,
                            totalHashesCalculated = newTotalHashes,
                            acceptedShares = newAccepted,
                            temperatureC = if (current.isTurbo) 69 else 60,
                            lastUpdated = System.currentTimeMillis()
                        )
                    }
                    dao.upsertProfile(updatedProfile)
                } else {
                    _liveHashrate.value = 0.0
                }
            }
        }
    }

    suspend fun setMiningState(active: Boolean) {
        val current = dao.getProfile() ?: MiningProfileEntity()
        dao.upsertProfile(current.copy(isMining = active))
    }

    suspend fun toggleTurbo() {
        val current = dao.getProfile() ?: MiningProfileEntity()
        val nextTurbo = !current.isTurbo
        dao.upsertProfile(current.copy(isTurbo = nextTurbo))
    }

    suspend fun selectActiveCoin(symbol: String) {
        val current = dao.getProfile() ?: MiningProfileEntity()
        dao.upsertProfile(current.copy(activeCoinSymbol = symbol))
    }

    suspend fun upgradeRig(upgrade: RigUpgradeEntity): Result<String> {
        val current = dao.getProfile() ?: MiningProfileEntity()
        val totalUsdEquivalent = (current.usdtBalance) +
                (current.btcBalance * CryptoCurrency.BTC.currentPriceUsd) +
                (current.ethBalance * CryptoCurrency.ETH.currentPriceUsd)

        if (totalUsdEquivalent < upgrade.costUsdt) {
            return Result.failure(Exception("الرصيد الكلي غير كافٍ لشراء هذه الترقية (${upgrade.costUsdt} USDT)"))
        }

        // Deduct from USDT balance first, then other coins if needed
        var remainingCost = upgrade.costUsdt
        var newUsdt = current.usdtBalance
        var newBtc = current.btcBalance

        if (newUsdt >= remainingCost) {
            newUsdt -= remainingCost
            remainingCost = 0.0
        } else {
            remainingCost -= newUsdt
            newUsdt = 0.0
            val btcNeeded = remainingCost / CryptoCurrency.BTC.currentPriceUsd
            newBtc = (newBtc - btcNeeded).coerceAtLeast(0.0)
        }

        val updatedUpgrade = upgrade.copy(
            level = upgrade.level + 1,
            isUnlocked = true,
            costUsdt = (upgrade.costUsdt * 1.5).toLong().toDouble()
        )
        dao.updateUpgrade(updatedUpgrade)

        val newBaseHashrate = current.baseHashrate + upgrade.hashrateBoostMh
        val newRigLevel = (current.rigLevel + 1).coerceAtMost(10)
        dao.upsertProfile(
            current.copy(
                baseHashrate = newBaseHashrate,
                usdtBalance = newUsdt,
                btcBalance = newBtc,
                rigLevel = newRigLevel,
                powerWatts = current.powerWatts + 25
            )
        )
        return Result.success("تم ترقية ${upgrade.nameAr} بنجاح!")
    }

    suspend fun executeWithdrawal(
        platform: String, // "Binance" or "Blockchain"
        type: String, // "BINANCE_PAY", "BINANCE_BEP20", "BLOCKCHAIN_ONCHAIN"
        coin: CryptoCurrency,
        amount: Double,
        destinationAddress: String,
        network: String,
        feeAmount: Double
    ): Result<TransactionEntity> {
        val current = dao.getProfile() ?: MiningProfileEntity()
        val currentCoinBalance = when (coin) {
            CryptoCurrency.BTC -> current.btcBalance
            CryptoCurrency.ETH -> current.ethBalance
            CryptoCurrency.SOL -> current.solBalance
            CryptoCurrency.USDT -> current.usdtBalance
            CryptoCurrency.KAS -> current.kasBalance
        }

        val totalDeduction = amount + feeAmount
        if (currentCoinBalance < totalDeduction) {
            return Result.failure(Exception("رصيد ${coin.nameAr} غير كافٍ لإتمام السحب. المتاح: ${String.format("%.6f", currentCoinBalance)}"))
        }

        // Generate authentic looking transaction hash
        val txHash = if (platform == "Binance") {
            "BIN-" + UUID.randomUUID().toString().replace("-", "").take(20).uppercase()
        } else {
            "0x" + UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "")
        }

        // Deduct balance
        val updatedProfile = when (coin) {
            CryptoCurrency.BTC -> current.copy(btcBalance = (current.btcBalance - totalDeduction).coerceAtLeast(0.0))
            CryptoCurrency.ETH -> current.copy(ethBalance = (current.ethBalance - totalDeduction).coerceAtLeast(0.0))
            CryptoCurrency.SOL -> current.copy(solBalance = (current.solBalance - totalDeduction).coerceAtLeast(0.0))
            CryptoCurrency.USDT -> current.copy(usdtBalance = (current.usdtBalance - totalDeduction).coerceAtLeast(0.0))
            CryptoCurrency.KAS -> current.copy(kasBalance = (current.kasBalance - totalDeduction).coerceAtLeast(0.0))
        }
        dao.upsertProfile(updatedProfile)

        val tx = TransactionEntity(
            type = type,
            platformName = platform,
            coinSymbol = coin.symbol,
            amount = amount,
            usdValue = amount * coin.currentPriceUsd,
            recipientAddress = destinationAddress,
            network = network,
            feeAmount = feeAmount,
            txHash = txHash,
            status = "COMPLETED",
            timestamp = System.currentTimeMillis()
        )
        val id = dao.insertTransaction(tx)
        return Result.success(tx.copy(id = id))
    }

    suspend fun saveAddress(
        label: String,
        platform: String,
        coinSymbol: String,
        network: String,
        address: String
    ) {
        dao.insertSavedAddress(
            SavedAddressEntity(
                label = label,
                platform = platform,
                coinSymbol = coinSymbol,
                network = network,
                address = address
            )
        )
    }

    suspend fun deleteAddress(id: Long) {
        dao.deleteSavedAddress(id)
    }
}
