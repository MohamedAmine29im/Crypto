package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BitcoinGold
import com.example.ui.theme.BlockchainEmerald
import com.example.ui.theme.CryptoBorder
import com.example.ui.theme.CryptoDarkCard
import com.example.ui.theme.CryptoDarkSurface
import com.example.ui.theme.CryptoTextPrimary
import com.example.ui.theme.CryptoTextSecondary

@Composable
fun ApkInstallGuideScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "دليل التثبيت على الأندرويد (APK)",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = CryptoTextPrimary
                )
                Text(
                    text = "خطوات تثبيت وتشغيل Bitcoin Mining Lab على هاتف Samsung Galaxy S21+ 5G",
                    style = MaterialTheme.typography.bodySmall,
                    color = CryptoTextSecondary
                )
            }
        }

        // Package verification card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BlockchainEmerald.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(BlockchainEmerald.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = BlockchainEmerald,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "ملف الحزمة جاهز للتثبيت 100% (APK Ready)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = CryptoTextPrimary
                        )
                        Text(
                            text = "تم تضمين كافة إعدادات التوقيع (Signing Configs)، والأيقونات المتكيفة (Adaptive Icons)، ودعم الشاشات الكاملة Edge-to-Edge.",
                            style = MaterialTheme.typography.bodySmall,
                            color = CryptoTextSecondary
                        )
                    }
                }
            }
        }

        // Step 1
        item {
            InstallStepCard(
                stepNumber = "1",
                icon = Icons.Default.Download,
                title = "تصدير أو تنزيل ملف الـ APK",
                description = "من شريط الخيارات في منصة AI Studio Build، يمكنك اختيار 'Export Project' أو إنشاء حزمة التثبيت 'Generate APK'. سيتم إنشاء ملف التطبيق باسم BitcoinMiningLab.apk."
            )
        }

        // Step 2
        item {
            InstallStepCard(
                stepNumber = "2",
                icon = Icons.Default.Folder,
                title = "فتح تطبيق ملفاتي (My Files) على الهاتف",
                description = "انتقل إلى تطبيق 'ملفاتي' (My Files) أو مجلد 'التنزيلات' (Downloads) في جهاز Samsung Galaxy S21+ 5G واضغط على ملف BitcoinMiningLab.apk."
            )
        }

        // Step 3
        item {
            InstallStepCard(
                stepNumber = "3",
                icon = Icons.Default.Security,
                title = "السماح بالتثبيت من هذا المصدر",
                description = "إذا ظهرت نافذة أمان من نظام الأندرويد، اضغط على 'الإعدادات' (Settings) ثم فعّل خيار 'السماح من هذا المصدر' (Allow from this source) لبرنامج الملفات أو المتصفح."
            )
        }

        // Step 4
        item {
            InstallStepCard(
                stepNumber = "4",
                icon = Icons.Default.Android,
                title = "الضغط على تثبيت (Install)",
                description = "اضغط على زر 'تثبيت' (Install). سيقوم نظام أندرويد بفحص الحزمة عبر حماية Google Play Protect والتأكد من أمانها بنسبة 100% لأن التطبيق لا يطلب أي صلاحيات خطيرة."
            )
        }

        // Step 5
        item {
            InstallStepCard(
                stepNumber = "5",
                icon = Icons.Default.PlayArrow,
                title = "فتح التطبيق والاستمتاع بالمختبر",
                description = "اضغط على 'فتح' (Open) وستظهر الأيقونة الذهبية للتطبيق على شاشتك الرئيسية وجاهزة لبدء اختبارات الهاش ريت بأعلى أداء."
            )
        }

        // Technical Specs Summary
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CryptoDarkSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CryptoBorder, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "المواصفات التقنية لحزمة التثبيت:",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = BitcoinGold
                    )
                    SpecRow(label = "اسم التطبيق:", value = "Bitcoin Mining Lab")
                    SpecRow(label = "المعرف ApplicationId:", value = "com.aistudio.cryptomine.qkrvxz")
                    SpecRow(label = "الحد الأدنى للنظام MinSdk:", value = "Android 7.0 (API 24)+")
                    SpecRow(label = "النظام المستهدف TargetSdk:", value = "Android 14 / 15 (API 36)")
                    SpecRow(label = "استهلاك البطارية:", value = "مضبوط تلقائياً مع تبريد ذكي")
                    SpecRow(label = "العمل في الخلفية:", value = "محظور لحماية بطارية الجهاز")
                }
            }
        }
    }
}

@Composable
private fun InstallStepCard(
    stepNumber: String,
    icon: ImageVector,
    title: String,
    description: String
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CryptoBorder, RoundedCornerShape(14.dp))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = BitcoinGold.copy(alpha = 0.2f),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = stepNumber,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                        color = BitcoinGold
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = BitcoinGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = CryptoTextPrimary
                    )
                }
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = CryptoTextSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun SpecRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = CryptoTextSecondary)
        Text(text = value, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold), color = CryptoTextPrimary)
    }
}
