package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.CryptoCurrency
import com.example.data.model.MiningProfileEntity
import com.example.data.model.RigUpgradeEntity
import com.example.data.model.SavedAddressEntity
import com.example.data.model.TransactionEntity
import com.example.data.repository.MiningRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    MINING,
    TRANSFER,
    HARDWARE,
    TRANSACTIONS,
    MARKET
}

enum class WithdrawTab {
    BINANCE,
    BLOCKCHAIN
}

enum class BinanceTransferMode {
    BINANCE_PAY,
    BEP20_CHAIN
}

enum class GasSpeed(val labelAr: String, val multiplier: Double) {
    ECONOMY("اقتصادي", 0.6),
    STANDARD("عادي (موصى به)", 1.0),
    FAST("فوري سريع", 1.8)
}

data class MiningUiState(
    val profile: MiningProfileEntity = MiningProfileEntity(),
    val liveHashrate: Double = 94.8,
    val transactions: List<TransactionEntity> = emptyList(),
    val upgrades: List<RigUpgradeEntity> = emptyList(),
    val savedAddresses: List<SavedAddressEntity> = emptyList(),
    val currentScreen: Screen = Screen.MINING,
    val withdrawTab: WithdrawTab = WithdrawTab.BINANCE,
    val binanceMode: BinanceTransferMode = BinanceTransferMode.BINANCE_PAY,
    val selectedCoin: CryptoCurrency = CryptoCurrency.BTC,
    val withdrawAmountText: String = "",
    val destinationAddressText: String = "",
    val selectedNetwork: String = "",
    val gasSpeed: GasSpeed = GasSpeed.STANDARD,
    val isConfirmDialogOpen: Boolean = false,
    val lastCompletedTx: TransactionEntity? = null,
    val isReceiptDialogOpen: Boolean = false,
    val snackbarMessage: String? = null,
    val isProcessing: Boolean = false
)

class MiningViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MiningRepository

    private val _currentScreen = MutableStateFlow(Screen.MINING)
    private val _withdrawTab = MutableStateFlow(WithdrawTab.BINANCE)
    private val _binanceMode = MutableStateFlow(BinanceTransferMode.BINANCE_PAY)
    private val _selectedCoin = MutableStateFlow(CryptoCurrency.USDT)
    private val _withdrawAmountText = MutableStateFlow("")
    private val _destinationAddressText = MutableStateFlow("")
    private val _selectedNetwork = MutableStateFlow("Binance Pay ID (0% رسوم)")
    private val _gasSpeed = MutableStateFlow(GasSpeed.STANDARD)
    private val _isConfirmDialogOpen = MutableStateFlow(false)
    private val _lastCompletedTx = MutableStateFlow<TransactionEntity?>(null)
    private val _isReceiptDialogOpen = MutableStateFlow(false)
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    private val _isProcessing = MutableStateFlow(false)

    val uiState: StateFlow<MiningUiState>

    init {
        val database = AppDatabase.getInstance(application)
        repository = MiningRepository(database.miningDao(), viewModelScope)

        uiState = combine(
            repository.profileFlow,
            repository.liveHashrate,
            repository.transactionsFlow,
            repository.upgradesFlow,
            repository.savedAddressesFlow,
            _currentScreen,
            _withdrawTab,
            _binanceMode,
            _selectedCoin,
            _withdrawAmountText,
            _destinationAddressText,
            _selectedNetwork,
            _gasSpeed,
            _isConfirmDialogOpen,
            _lastCompletedTx,
            _isReceiptDialogOpen,
            _snackbarMessage,
            _isProcessing
        ) { args: Array<Any?> ->
            MiningUiState(
                profile = (args[0] as? MiningProfileEntity) ?: MiningProfileEntity(),
                liveHashrate = (args[1] as? Double) ?: 94.8,
                transactions = (args[2] as? List<*>)?.filterIsInstance<TransactionEntity>() ?: emptyList(),
                upgrades = (args[3] as? List<*>)?.filterIsInstance<RigUpgradeEntity>() ?: emptyList(),
                savedAddresses = (args[4] as? List<*>)?.filterIsInstance<SavedAddressEntity>() ?: emptyList(),
                currentScreen = (args[5] as? Screen) ?: Screen.MINING,
                withdrawTab = (args[6] as? WithdrawTab) ?: WithdrawTab.BINANCE,
                binanceMode = (args[7] as? BinanceTransferMode) ?: BinanceTransferMode.BINANCE_PAY,
                selectedCoin = (args[8] as? CryptoCurrency) ?: CryptoCurrency.USDT,
                withdrawAmountText = (args[9] as? String) ?: "",
                destinationAddressText = (args[10] as? String) ?: "",
                selectedNetwork = (args[11] as? String) ?: "Binance Pay ID (0% رسوم)",
                gasSpeed = (args[12] as? GasSpeed) ?: GasSpeed.STANDARD,
                isConfirmDialogOpen = (args[13] as? Boolean) ?: false,
                lastCompletedTx = args[14] as? TransactionEntity,
                isReceiptDialogOpen = (args[15] as? Boolean) ?: false,
                snackbarMessage = args[16] as? String,
                isProcessing = (args[17] as? Boolean) ?: false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MiningUiState()
        )
    }

    fun setScreen(screen: Screen) {
        _currentScreen.value = screen
    }

    fun setWithdrawTab(tab: WithdrawTab) {
        _withdrawTab.value = tab
        if (tab == WithdrawTab.BINANCE) {
            _selectedNetwork.value = if (_binanceMode.value == BinanceTransferMode.BINANCE_PAY)
                "Binance Pay ID (فوري 0% رسوم)"
            else "BNB Smart Chain (BEP-20)"
        } else {
            _selectedNetwork.value = _selectedCoin.value.defaultNetwork
        }
    }

    fun setBinanceMode(mode: BinanceTransferMode) {
        _binanceMode.value = mode
        _selectedNetwork.value = if (mode == BinanceTransferMode.BINANCE_PAY)
            "Binance Pay ID (فوري 0% رسوم)"
        else "BNB Smart Chain (BEP-20)"
    }

    fun selectCoin(coin: CryptoCurrency) {
        _selectedCoin.value = coin
        if (_withdrawTab.value == WithdrawTab.BLOCKCHAIN) {
            _selectedNetwork.value = coin.defaultNetwork
        }
    }

    fun setWithdrawAmount(amount: String) {
        _withdrawAmountText.value = amount
    }

    fun fillMaxAmount() {
        val currentProfile = uiState.value.profile
        val coin = _selectedCoin.value
        val balance = when (coin) {
            CryptoCurrency.BTC -> currentProfile.btcBalance
            CryptoCurrency.ETH -> currentProfile.ethBalance
            CryptoCurrency.SOL -> currentProfile.solBalance
            CryptoCurrency.USDT -> currentProfile.usdtBalance
            CryptoCurrency.KAS -> currentProfile.kasBalance
        }
        _withdrawAmountText.value = String.format("%.6f", balance).trimEnd('0').trimEnd('.')
    }

    fun fillPercentageAmount(percent: Int) {
        val currentProfile = uiState.value.profile
        val coin = _selectedCoin.value
        val balance = when (coin) {
            CryptoCurrency.BTC -> currentProfile.btcBalance
            CryptoCurrency.ETH -> currentProfile.ethBalance
            CryptoCurrency.SOL -> currentProfile.solBalance
            CryptoCurrency.USDT -> currentProfile.usdtBalance
            CryptoCurrency.KAS -> currentProfile.kasBalance
        }
        val target = balance * (percent / 100.0)
        _withdrawAmountText.value = String.format("%.6f", target).trimEnd('0').trimEnd('.')
    }

    fun setDestinationAddress(address: String) {
        _destinationAddressText.value = address
    }

    fun setSelectedNetwork(network: String) {
        _selectedNetwork.value = network
    }

    fun setGasSpeed(speed: GasSpeed) {
        _gasSpeed.value = speed
    }

    fun toggleMining() {
        viewModelScope.launch {
            val isCurrentlyActive = uiState.value.profile.isMining
            repository.setMiningState(!isCurrentlyActive)
            _snackbarMessage.value = if (!isCurrentlyActive) "تم استئناف التعدين السحابي ⚡" else "تم إيقاف التعدين مؤقتاً ⏸"
        }
    }

    fun toggleTurbo() {
        viewModelScope.launch {
            repository.toggleTurbo()
            val turbo = !uiState.value.profile.isTurbo
            _snackbarMessage.value = if (turbo) "تم تفعيل وضع التوربو الأقصى (+35% هاش ريت) 🔥" else "تم العودة إلى الوضع القياسي المعتدل"
        }
    }

    fun selectMiningCoin(coin: CryptoCurrency) {
        viewModelScope.launch {
            repository.selectActiveCoin(coin.symbol)
            _snackbarMessage.value = "تم تحويل وحدة التعدين النشطة إلى عملة ${coin.nameAr} (${coin.symbol})"
        }
    }

    fun upgradeRig(upgrade: RigUpgradeEntity) {
        viewModelScope.launch {
            val result = repository.upgradeRig(upgrade)
            result.onSuccess { msg ->
                _snackbarMessage.value = msg
            }.onFailure { err ->
                _snackbarMessage.value = err.message ?: "فشلت الترقية"
            }
        }
    }

    fun openConfirmWithdrawDialog() {
        val amount = _withdrawAmountText.value.toDoubleOrNull()
        val address = _destinationAddressText.value.trim()
        val coin = _selectedCoin.value

        if (amount == null || amount <= 0) {
            _snackbarMessage.value = "يرجى إدخال مبلغ سحب صحيح"
            return
        }

        if (address.isEmpty()) {
            _snackbarMessage.value = if (_withdrawTab.value == WithdrawTab.BINANCE)
                "يرجى إدخال معرّف بايننس (UID أو Pay ID)"
            else "يرجى إدخال عنوان محفظة البلوكشاين"
            return
        }

        if (amount < coin.minPayout) {
            _snackbarMessage.value = "الحد الأدنى للسحب هو ${coin.minPayout} ${coin.symbol}"
            return
        }

        val balance = when (coin) {
            CryptoCurrency.BTC -> uiState.value.profile.btcBalance
            CryptoCurrency.ETH -> uiState.value.profile.ethBalance
            CryptoCurrency.SOL -> uiState.value.profile.solBalance
            CryptoCurrency.USDT -> uiState.value.profile.usdtBalance
            CryptoCurrency.KAS -> uiState.value.profile.kasBalance
        }

        if (amount > balance) {
            _snackbarMessage.value = "الرصيد المتاح غير كافٍ. رصيدك الحالي: ${String.format("%.6f", balance)} ${coin.symbol}"
            return
        }

        _isConfirmDialogOpen.value = true
    }

    fun dismissConfirmDialog() {
        _isConfirmDialogOpen.value = false
    }

    fun confirmWithdrawal() {
        _isConfirmDialogOpen.value = false
        _isProcessing.value = true

        val amount = _withdrawAmountText.value.toDoubleOrNull() ?: return
        val address = _destinationAddressText.value.trim()
        val coin = _selectedCoin.value
        val tab = _withdrawTab.value

        val platform = if (tab == WithdrawTab.BINANCE) "Binance" else "Blockchain"
        val type = if (tab == WithdrawTab.BINANCE) {
            if (_binanceMode.value == BinanceTransferMode.BINANCE_PAY) "BINANCE_PAY" else "BINANCE_BEP20"
        } else {
            "BLOCKCHAIN_ONCHAIN"
        }

        // Calculate network gas fee
        val baseFee = if (tab == WithdrawTab.BINANCE && _binanceMode.value == BinanceTransferMode.BINANCE_PAY) {
            0.0
        } else {
            when (coin) {
                CryptoCurrency.BTC -> 0.00003 * _gasSpeed.value.multiplier
                CryptoCurrency.ETH -> 0.0012 * _gasSpeed.value.multiplier
                CryptoCurrency.SOL -> 0.005 * _gasSpeed.value.multiplier
                CryptoCurrency.USDT -> 1.0 * _gasSpeed.value.multiplier
                CryptoCurrency.KAS -> 1.0 * _gasSpeed.value.multiplier
            }
        }

        viewModelScope.launch {
            kotlinx.coroutines.delay(1400) // Realistic blockchain propagation animation delay
            val result = repository.executeWithdrawal(
                platform = platform,
                type = type,
                coin = coin,
                amount = amount,
                destinationAddress = address,
                network = _selectedNetwork.value,
                feeAmount = baseFee
            )

            _isProcessing.value = false
            result.onSuccess { tx ->
                _lastCompletedTx.value = tx
                _isReceiptDialogOpen.value = true
                _withdrawAmountText.value = ""
                _snackbarMessage.value = "تمت معالجة وتأكيد السحب بنجاح إلى $platform!"
            }.onFailure { error ->
                _snackbarMessage.value = error.message ?: "فشلت عملية التحويل"
            }
        }
    }

    fun dismissReceiptDialog() {
        _isReceiptDialogOpen.value = false
        _lastCompletedTx.value = null
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun saveAddressPreset(label: String, address: String) {
        viewModelScope.launch {
            val platform = if (_withdrawTab.value == WithdrawTab.BINANCE) "BINANCE" else "BLOCKCHAIN"
            repository.saveAddress(
                label = label,
                platform = platform,
                coinSymbol = _selectedCoin.value.symbol,
                network = _selectedNetwork.value,
                address = address
            )
            _snackbarMessage.value = "تم حفظ العنوان في سجل العناوين السريعة"
        }
    }
}
