package com.example

import com.example.data.model.CryptoCurrency
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MiningCalculationTest {

    @Test
    fun testCryptoCurrencyLookup() {
        val btc = CryptoCurrency.fromSymbol("BTC")
        assertEquals(CryptoCurrency.BTC, btc)
        assertEquals("بيتكوين", btc.nameAr)

        val eth = CryptoCurrency.fromSymbol("eth")
        assertEquals(CryptoCurrency.ETH, eth)

        val unknown = CryptoCurrency.fromSymbol("XYZ")
        assertEquals(CryptoCurrency.BTC, unknown)
    }

    @Test
    fun testMinPayoutsArePositive() {
        for (coin in CryptoCurrency.entries) {
            assertTrue("${coin.symbol} min payout should be positive", coin.minPayout > 0.0)
            assertTrue("${coin.symbol} price in USD should be positive", coin.currentPriceUsd > 0.0)
            assertTrue("${coin.symbol} must have network options", coin.blockchainNetworks.isNotEmpty())
        }
    }

    @Test
    fun testHashrateRewardEstimation() {
        val hashrateMh = 100.0
        val basePerMhUsd = 0.00000035
        val rewardUsd = hashrateMh * basePerMhUsd
        assertTrue(rewardUsd > 0.0)

        val btcReward = rewardUsd / CryptoCurrency.BTC.currentPriceUsd
        assertTrue(btcReward > 0.0)
    }
}
