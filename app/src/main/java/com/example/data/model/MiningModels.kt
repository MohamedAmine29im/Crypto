package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mining_profile")
data class MiningProfileEntity(
    @PrimaryKey val id: Int = 1,
    val activeCoinSymbol: String = "BTC",
    val isMining: Boolean = true,
    val isTurbo: Boolean = false,
    val baseHashrate: Double = 94.8, // MH/s
    val btcBalance: Double = 0.00342500,
    val ethBalance: Double = 0.06180000,
    val solBalance: Double = 1.12400000,
    val usdtBalance: Double = 84.50,
    val kasBalance: Double = 850.25,
    val totalHashesCalculated: Long = 28495020L,
    val acceptedShares: Int = 684,
    val rejectedShares: Int = 3,
    val rigLevel: Int = 2,
    val powerWatts: Int = 230,
    val temperatureC: Int = 59,
    val fanSpeedPercent: Int = 68,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // "BINANCE_PAY", "BINANCE_BEP20", "BLOCKCHAIN_ONCHAIN"
    val platformName: String, // "Binance" or "Blockchain"
    val coinSymbol: String,
    val amount: Double,
    val usdValue: Double,
    val recipientAddress: String,
    val network: String,
    val feeAmount: Double,
    val txHash: String,
    val status: String, // "COMPLETED", "PROCESSING"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "rig_upgrades")
data class RigUpgradeEntity(
    @PrimaryKey val id: String,
    val nameAr: String,
    val nameEn: String,
    val descriptionAr: String,
    val category: String, // "GPU", "ASIC", "COOLING", "POWER"
    val hashrateBoostMh: Double,
    val costUsdt: Double,
    val level: Int,
    val maxLevel: Int,
    val isUnlocked: Boolean
)

@Entity(tableName = "saved_addresses")
data class SavedAddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val label: String,
    val platform: String, // "BINANCE" or "BLOCKCHAIN"
    val coinSymbol: String,
    val network: String,
    val address: String
)

enum class CryptoCurrency(
    val symbol: String,
    val nameAr: String,
    val nameEn: String,
    val currentPriceUsd: Double,
    val minPayout: Double,
    val defaultNetwork: String,
    val blockchainNetworks: List<String>,
    val binanceSupported: Boolean = true
) {
    BTC(
        symbol = "BTC",
        nameAr = "بيتكوين",
        nameEn = "Bitcoin",
        currentPriceUsd = 88450.0,
        minPayout = 0.0005,
        defaultNetwork = "Bitcoin (Native SegWit)",
        blockchainNetworks = listOf("Bitcoin (Native SegWit)", "Bitcoin (Taproot)", "BNB Smart Chain (BEP-20)")
    ),
    ETH(
        symbol = "ETH",
        nameAr = "إيثيريوم",
        nameEn = "Ethereum",
        currentPriceUsd = 3120.0,
        minPayout = 0.008,
        defaultNetwork = "Ethereum Mainnet (ERC-20)",
        blockchainNetworks = listOf("Ethereum Mainnet (ERC-20)", "Arbitrum One", "Optimism", "BNB Smart Chain (BEP-20)")
    ),
    SOL(
        symbol = "SOL",
        nameAr = "سولانا",
        nameEn = "Solana",
        currentPriceUsd = 184.5,
        minPayout = 0.1,
        defaultNetwork = "Solana Network (SPL)",
        blockchainNetworks = listOf("Solana Network (SPL)", "BNB Smart Chain (BEP-20)")
    ),
    USDT(
        symbol = "USDT",
        nameAr = "تيذر دولار",
        nameEn = "Tether USD",
        currentPriceUsd = 1.0,
        minPayout = 5.0,
        defaultNetwork = "TRON (TRC-20)",
        blockchainNetworks = listOf("TRON (TRC-20)", "BNB Smart Chain (BEP-20)", "Ethereum (ERC-20)", "Polygon")
    ),
    KAS(
        symbol = "KAS",
        nameAr = "كاسبا",
        nameEn = "Kaspa",
        currentPriceUsd = 0.145,
        minPayout = 50.0,
        defaultNetwork = "Kaspa Native (kHeavyHash)",
        blockchainNetworks = listOf("Kaspa Native (kHeavyHash)", "BNB Smart Chain (BEP-20)")
    );

    companion object {
        fun fromSymbol(symbol: String): CryptoCurrency {
            return entries.firstOrNull { it.symbol.equals(symbol, ignoreCase = true) } ?: BTC
        }
    }
}
