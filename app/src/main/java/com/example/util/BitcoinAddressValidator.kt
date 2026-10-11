package com.example.util

import com.example.data.model.BitcoinAddressInfo

object BitcoinAddressValidator {

    private val BASE58_REGEX = Regex("^[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{26,35}$")
    private val BECH32_SEGWIT_REGEX = Regex("^bc1q[02-9ac-hj-np-z]{38,58}$", RegexOption.IGNORE_CASE)
    private val BECH32M_TAPROOT_REGEX = Regex("^bc1p[02-9ac-hj-np-z]{58,62}$", RegexOption.IGNORE_CASE)
    private val TESTNET_SEGWIT_REGEX = Regex("^tb1[02-9ac-hj-np-z]{38,62}$", RegexOption.IGNORE_CASE)

    fun validateAddress(address: String): BitcoinAddressInfo {
        val trimmed = address.trim()
        if (trimmed.isEmpty()) {
            return BitcoinAddressInfo(
                rawAddress = "",
                isValid = false,
                typeDescription = "الحقل فارغ",
                isSegwit = false,
                scriptType = "-",
                securityNotes = "أدخل عنوان بيتكوين لفحصه تعليمياً."
            )
        }

        // 1. Native SegWit (Bech32) - starts with bc1q
        if (trimmed.startsWith("bc1q", ignoreCase = true) && BECH32_SEGWIT_REGEX.matches(trimmed)) {
            return BitcoinAddressInfo(
                rawAddress = trimmed,
                isValid = true,
                typeDescription = "عنوان سيغويت أصلي (Native SegWit - Bech32)",
                isSegwit = true,
                scriptType = "P2WPKH (Pay-to-Witness-Public-Key-Hash)",
                securityNotes = "عنوان حديث يوفر رسوم معاملات منخفضة ويدعم حماية ضد الأخطاء المطبعية."
            )
        }

        // 2. Taproot (Bech32m) - starts with bc1p
        if (trimmed.startsWith("bc1p", ignoreCase = true) && BECH32M_TAPROOT_REGEX.matches(trimmed)) {
            return BitcoinAddressInfo(
                rawAddress = trimmed,
                isValid = true,
                typeDescription = "عنوان تابروت أحدث ترقية (Taproot - Bech32m)",
                isSegwit = true,
                scriptType = "P2TR (Pay-to-Taproot - Schnorr Signatures)",
                securityNotes = "أحدث بنية عناوين بيتكوين تم تفعيلها بتحديث Taproot، توفر خصوصية وكفاءة أعلى للمعامِلات المعقدة."
            )
        }

        // 3. Legacy (P2PKH) - starts with 1
        if (trimmed.startsWith("1") && BASE58_REGEX.matches(trimmed)) {
            return BitcoinAddressInfo(
                rawAddress = trimmed,
                isValid = true,
                typeDescription = "عنوان تقليدي قديم (Legacy P2PKH)",
                isSegwit = false,
                scriptType = "P2PKH (Pay-to-Public-Key-Hash)",
                securityNotes = "النوع الأصلي لعناوين البيتكوين منذ إطلاق ساتوشي ناكاموتو. متوافق مع كافة المحافظ ولكن رسومه أعلى."
            )
        }

        // 4. Script / Multsig (P2SH) - starts with 3
        if (trimmed.startsWith("3") && BASE58_REGEX.matches(trimmed)) {
            return BitcoinAddressInfo(
                rawAddress = trimmed,
                isValid = true,
                typeDescription = "عنوان نصوص وبرمجيات ذكية (P2SH / Nested SegWit)",
                isSegwit = false,
                scriptType = "P2SH (Pay-to-Script-Hash)",
                securityNotes = "يُستخدم عادةً للمحافظ متعددة التوقيعات (Multi-Signature) وعناوين SegWit المغلفة للتوافق القديم."
            )
        }

        // 5. Testnet detection
        if (trimmed.startsWith("tb1", ignoreCase = true) || trimmed.startsWith("m") || trimmed.startsWith("n") || trimmed.startsWith("2")) {
            return BitcoinAddressInfo(
                rawAddress = trimmed,
                isValid = true,
                typeDescription = "عنوان شبكة الاختبار (Bitcoin Testnet)",
                isSegwit = trimmed.startsWith("tb1", ignoreCase = true),
                scriptType = "Testnet Address",
                securityNotes = "هذا العنوان مخصص لشبكة الاختبار (Testnet) وليس لشبكة البيتكوين الحقيقية الرئيسية."
            )
        }

        return BitcoinAddressInfo(
            rawAddress = trimmed,
            isValid = false,
            typeDescription = "صيغة غير صالحة",
            isSegwit = false,
            scriptType = "غير معروف",
            securityNotes = "تأكد من صحة العنوان وخلوه من المسافات. يجب أن يبدأ بـ bc1q أو bc1p أو 1 أو 3."
        )
    }
}
