package com.example.data.model

enum class DifficultyLevel(
    val id: Int,
    val bits: Int,
    val hexPrefix: String,
    val nameAr: String,
    val nameEn: String,
    val descriptionAr: String,
    val estimatedHashes: Long
) {
    VERY_EASY(
        id = 1,
        bits = 8,
        hexPrefix = "00",
        nameAr = "مبتدئ (8 بت)",
        nameEn = "Easy (8-bit)",
        descriptionAr = "بادئة تبدأ بـ 00 (~256 تجزئة) - فحص سريع وفوري",
        estimatedHashes = 256L
    ),
    EASY(
        id = 2,
        bits = 12,
        hexPrefix = "000",
        nameAr = "معتدل (12 بت)",
        nameEn = "Medium (12-bit)",
        descriptionAr = "بادئة تبدأ بـ 000 (~4,096 تجزئة) - يستغرق عدة ثوانٍ",
        estimatedHashes = 4096L
    ),
    MEDIUM(
        id = 3,
        bits = 16,
        hexPrefix = "0000",
        nameAr = "قياسي (16 بت)",
        nameEn = "Standard (16-bit)",
        descriptionAr = "بادئة تبدأ بـ 0000 (~65,536 تجزئة) - اختبار حقيقي لقوة المعالج",
        estimatedHashes = 65536L
    ),
    HARD(
        id = 4,
        bits = 20,
        hexPrefix = "00000",
        nameAr = "متقدم (20 بت)",
        nameEn = "Heavy (20-bit)",
        descriptionAr = "بادئة تبدأ بـ 00000 (~1.05 مليون تجزئة) - اختبار مجهد",
        estimatedHashes = 1048576L
    ),
    EXTREME(
        id = 5,
        bits = 24,
        hexPrefix = "000000",
        nameAr = "فائق (24 بت)",
        nameEn = "Extreme (24-bit)",
        descriptionAr = "بادئة تبدأ بـ 000000 (~16.7 مليون تجزئة) - اختبار تحمل",
        estimatedHashes = 16777216L
    );

    companion object {
        fun fromId(id: Int): DifficultyLevel {
            return entries.firstOrNull { it.id == id } ?: EASY
        }
    }
}

data class CandidateBlock(
    val blockIndex: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val nonce: Long,
    val hashHex: String,
    val difficultyBits: Int,
    val difficultyLabel: String,
    val hashesTried: Long,
    val durationMs: Long,
    val averageHashrateKh: Double
)

data class DeviceHardwareStatus(
    val temperatureC: Double = 33.5,
    val batteryPercent: Int = 88,
    val isCharging: Boolean = false,
    val chargingSource: String = "بطارية الهاتف (غير موصول)",
    val isOverheated: Boolean = false,
    val maxTempThresholdC: Double = 42.0,
    val activeThreads: Int = 2,
    val availableCores: Int = Runtime.getRuntime().availableProcessors(),
    val throttleDelayMs: Long = 5L
)

enum class Screen {
    DASHBOARD,
    HARDWARE,
    VALIDATOR,
    EDUCATION,
    INSTALL_GUIDE
}

data class BitcoinAddressInfo(
    val rawAddress: String = "",
    val isValid: Boolean = false,
    val typeDescription: String = "",
    val isSegwit: Boolean = false,
    val scriptType: String = "",
    val securityNotes: String = ""
)
