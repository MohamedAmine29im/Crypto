package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.MiningProfileEntity
import com.example.data.model.RigUpgradeEntity
import com.example.data.model.SavedAddressEntity
import com.example.data.model.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        MiningProfileEntity::class,
        TransactionEntity::class,
        RigUpgradeEntity::class,
        SavedAddressEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun miningDao(): MiningDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "crypto_mining_db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    seedDatabase(database.miningDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun seedDatabase(dao: MiningDao) {
            // Seed initial profile
            dao.upsertProfile(MiningProfileEntity())

            // Seed initial hardware upgrades
            val initialUpgrades = listOf(
                RigUpgradeEntity(
                    id = "gpu_rtx_4090",
                    nameAr = "مصفوفة كروت RTX 4090 سحابية",
                    nameEn = "RTX 4090 Cloud Grid",
                    descriptionAr = "زيادة قدرة المعالجة الرسومية وكفاءة تجزئة خوارزميات Ethash و KawPow",
                    category = "GPU",
                    hashrateBoostMh = 35.0,
                    costUsdt = 45.0,
                    level = 1,
                    maxLevel = 5,
                    isUnlocked = true
                ),
                RigUpgradeEntity(
                    id = "asic_antminer_s21",
                    nameAr = "معالج آسك Antminer S21 هيدرو",
                    nameEn = "Antminer S21 Hydro ASIC",
                    descriptionAr = "سيرفر متخصص في تجزئة خوارزمية SHA-256 للبيتكوين باستهلاك طاقة فائق الانخفاض",
                    category = "ASIC",
                    hashrateBoostMh = 80.0,
                    costUsdt = 95.0,
                    level = 0,
                    maxLevel = 5,
                    isUnlocked = false
                ),
                RigUpgradeEntity(
                    id = "liquid_cooling_sys",
                    nameAr = "نظام التبريد السائل النيتروجيني",
                    nameEn = "Liquid Nitrogen Cooling",
                    descriptionAr = "خفض درجات الحرارة بمقدار 18 درجة وزيادة استقرار رفع تردد التشغيل (Overclock)",
                    category = "COOLING",
                    hashrateBoostMh = 25.0,
                    costUsdt = 30.0,
                    level = 1,
                    maxLevel = 3,
                    isUnlocked = true
                ),
                RigUpgradeEntity(
                    id = "solar_green_power",
                    nameAr = "محطة طاقة شمسية خضراء",
                    nameEn = "Solar Green Energy Farm",
                    descriptionAr = "توليد طاقة نظيفة وخفض تكاليف الكهرباء الافتراضية مع زيادة معدل الحصص المقبولة",
                    category = "POWER",
                    hashrateBoostMh = 20.0,
                    costUsdt = 40.0,
                    level = 0,
                    maxLevel = 4,
                    isUnlocked = false
                ),
                RigUpgradeEntity(
                    id = "quantum_hash_core",
                    nameAr = "مسرّع التجزئة الكوانتية Quantum V2",
                    nameEn = "Quantum Acceleration Unit",
                    descriptionAr = "مصفوفة معالجة كمية فائقة لمضاعفة معدل الهاش ريت بمقدار ضخم لجميع العملات",
                    category = "ASIC",
                    hashrateBoostMh = 160.0,
                    costUsdt = 180.0,
                    level = 0,
                    maxLevel = 3,
                    isUnlocked = false
                )
            )
            dao.insertUpgrades(initialUpgrades)

            // Seed initial past transactions to show realistic payout proofs to Binance and Blockchain
            val initialTx = listOf(
                TransactionEntity(
                    type = "BINANCE_PAY",
                    platformName = "Binance",
                    coinSymbol = "USDT",
                    amount = 50.0,
                    usdValue = 50.0,
                    recipientAddress = "UID: 829401773",
                    network = "Binance Pay ID (فوري 0% رسوم)",
                    feeAmount = 0.0,
                    txHash = "BINC7a93f4129b8ec419e712a5",
                    status = "COMPLETED",
                    timestamp = System.currentTimeMillis() - 86400000L * 2
                ),
                TransactionEntity(
                    type = "BLOCKCHAIN_ONCHAIN",
                    platformName = "Blockchain",
                    coinSymbol = "BTC",
                    amount = 0.00125,
                    usdValue = 110.56,
                    recipientAddress = "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq",
                    network = "Bitcoin (Native SegWit)",
                    feeAmount = 0.00004,
                    txHash = "f4184fc596403b9d638783cf57adfe4c75c605f6356fbc91338530e9831e9e16",
                    status = "COMPLETED",
                    timestamp = System.currentTimeMillis() - 86400000L * 5
                )
            )
            for (tx in initialTx) {
                dao.insertTransaction(tx)
            }

            // Seed sample saved addresses
            dao.insertSavedAddress(
                SavedAddressEntity(
                    label = "حسابي الأساسي في بايننس",
                    platform = "BINANCE",
                    coinSymbol = "USDT",
                    network = "Binance Pay",
                    address = "918237465"
                )
            )
            dao.insertSavedAddress(
                SavedAddressEntity(
                    label = "محفظة تريزور البلوكشاين",
                    platform = "BLOCKCHAIN",
                    coinSymbol = "BTC",
                    network = "Bitcoin (Native SegWit)",
                    address = "bc1qxy2kgdygjrsqtzq2n0yrf2493p83kkfjhx0wlh"
                )
            )
        }
    }
}
