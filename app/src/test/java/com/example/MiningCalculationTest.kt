package com.example

import com.example.data.model.DifficultyLevel
import com.example.util.BitcoinAddressValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.security.MessageDigest

class MiningCalculationTest {

    @Test
    fun testDoubleSha256Computation() {
        val md = MessageDigest.getInstance("SHA-256")
        val input = "Bitcoin Mining Lab Genesis".toByteArray()
        val firstRound = md.digest(input)
        md.reset()
        val doubleHash = md.digest(firstRound)

        assertEquals(32, doubleHash.size)
        val hex = doubleHash.joinToString("") { "%02x".format(it) }
        assertEquals(64, hex.length)
        assertTrue(hex.isNotEmpty())
    }

    @Test
    fun testDifficultyLevelsConfigured() {
        for (level in DifficultyLevel.entries) {
            assertTrue("Bits should be positive", level.bits > 0)
            assertTrue("Prefix should not be empty", level.hexPrefix.isNotEmpty())
            assertTrue("Estimated hashes should be positive", level.estimatedHashes > 0)
            assertTrue("Arabic name must be present", level.nameAr.isNotEmpty())
        }
        assertEquals(DifficultyLevel.VERY_EASY, DifficultyLevel.fromId(1))
        assertEquals(DifficultyLevel.EXTREME, DifficultyLevel.fromId(5))
    }

    @Test
    fun testBitcoinAddressValidatorSegWit() {
        val segwitAddress = "bc1qxy2kgdygjrsqtzq2n0yrf2493p83kkfjhx0wlh"
        val info = BitcoinAddressValidator.validateAddress(segwitAddress)
        assertTrue("Native SegWit address should be valid", info.isValid)
        assertTrue("Should be marked as SegWit", info.isSegwit)
        assertTrue(info.typeDescription.contains("SegWit", ignoreCase = true))
    }

    @Test
    fun testBitcoinAddressValidatorTaproot() {
        val taprootAddress = "bc1p5d7rjq7g6rd22r325ep4sq6hgzerxtxazzvxwdncxqmrd6gtde3hp5gedz"
        val info = BitcoinAddressValidator.validateAddress(taprootAddress)
        assertTrue("Taproot address should be valid", info.isValid)
        assertTrue("Should be marked as SegWit", info.isSegwit)
        assertTrue(info.typeDescription.contains("Taproot", ignoreCase = true))
    }

    @Test
    fun testBitcoinAddressValidatorLegacy() {
        val legacyAddress = "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa"
        val info = BitcoinAddressValidator.validateAddress(legacyAddress)
        assertTrue("Genesis legacy address should be valid", info.isValid)
        assertFalse("Legacy should not be marked as Native SegWit", info.isSegwit)
        assertTrue(info.typeDescription.contains("Legacy", ignoreCase = true))
    }

    @Test
    fun testBitcoinAddressValidatorInvalid() {
        val invalidAddress = "invalid_not_a_btc_address_123"
        val info = BitcoinAddressValidator.validateAddress(invalidAddress)
        assertFalse("Random string should be invalid", info.isValid)
    }

    @Test
    fun testDifficultyLevelTargetPrefixMatching() {
        val easy = DifficultyLevel.VERY_EASY
        assertEquals("00", easy.hexPrefix)
        val medium = DifficultyLevel.MEDIUM
        assertEquals("0000", medium.hexPrefix)
    }
}
