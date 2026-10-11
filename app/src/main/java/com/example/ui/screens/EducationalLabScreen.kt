package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BitcoinGold
import com.example.ui.theme.BlockchainEmerald
import com.example.ui.theme.CryptoBorder
import com.example.ui.theme.CryptoDarkCard
import com.example.ui.theme.CryptoTextPrimary
import com.example.ui.theme.CryptoTextSecondary

@Composable
fun EducationalLabScreen(
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
                    text = "دليل التعدين وإثبات العمل (SHA-256)",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = CryptoTextPrimary
                )
                Text(
                    text = "شرح تقني وعلمي شفاف لكيفية عمل شبكة البيتكوين وحقيقة تعدين الهواتف",
                    style = MaterialTheme.typography.bodySmall,
                    color = CryptoTextSecondary
                )
            }
        }

        // Section 1: What is Proof-of-Work?
        item {
            EducationalSectionCard(
                icon = Icons.Default.Calculate,
                title = "ما هي خوارزمية إثبات العمل (Proof-of-Work)؟",
                accentColor = BitcoinGold,
                content = "في شبكة البيتكوين، يُطلب من المعدّنين إيجاد رقم عشوائي (Nonce) بحيث ينتج عن تطبيق خوارزمية SHA-256 مرتين (Double SHA-256) على ترويسة الكتلة رقم يبدأ بعدد محدد من الأصفار.\n\nهذا الرقم العشوائي لا يمكن التنبؤ به رياضياً، والوسيلة الوحيدة لإيجاده هي تجربة مليارات المحاولات في الثانية حتى العثور على حل يطابق صعوبة الشبكة الحالية."
            )
        }

        // Section 2: Reality of Smartphone Mining vs ASICs
        item {
            EducationalSectionCard(
                icon = Icons.Default.Speed,
                title = "لماذا لا يمكن للهواتف تعدين بيتكوين حقيقي؟",
                accentColor = BlockchainEmerald,
                content = "تبلغ القوة الحسابية لشبكة البيتكوين العالمية اليوم أكثر من 700 إكساهاش في الثانية (700,000,000,000,000,000,000 H/s)، وتُشغَّل بواسطة أجهزة متخصصة خارقة تُعرف بـ ASICs (مثل Antminer S21).\n\nهاتف ذكي متطور مثل Samsung Galaxy S21+ 5G يستطيع تحقيق حوالي 150 إلى 300 ألف تجزئة في الثانية (150 - 300 kH/s). بالمقارنة مع قوة الشبكة، فإن نسبة فوز هاتف بكتلة بيتكوين هي 1 إلى عدة تريليونات، واستهلاك الكهرباء والحرارة يفوق أي جدوى اقتصادية."
            )
        }

        // Section 3: Difficulty Adjustment
        item {
            EducationalSectionCard(
                icon = Icons.Default.Lock,
                title = "آلية ضبط الصعوبة التلقائية (Difficulty Adjustment)",
                accentColor = BitcoinGold,
                content = "تقوم شبكة البيتكوين بضبط الصعوبة الرياضية كل 2016 كتلة (أسبوعين تقريباً). إذا زاد عدد المعدنين في العالم، تزيد الشبكة عدد الأصفار المطلوبة في بداية الهاش لضمان بقاء وقت إصدار كل كتلة حوالي 10 دقائق دائماً.\n\nفي هذا التطبيق، قمنا بمحاكاة هذه الآلية محلياً بخيارات تبدأ من 8 بت (صفرين hex) وتصل إلى 24 بت لترى كيف تتغير سرعة حل الكتل مع زيادة الصعوبة."
            )
        }

        // Section 4: Zero Scam / Transparency Guarantee
        item {
            EducationalSectionCard(
                icon = Icons.Default.VerifiedUser,
                title = "ميثاق الأمان والشفافية التامة 🛡️",
                accentColor = BlockchainEmerald,
                content = "الكثير من التطبيقات في المتاجر تدعي كذباً أنها 'تعدن البيتكوين مجاناً على الهاتف' أو تطلب إيداعات وسحوبات وهمية مع اشتراكات مدفوعة.\n\nتطبيق Bitcoin Mining Lab مجاني 100%، مفتوح وشفاف، لا يحتوي على أي إعلانات خفية أو رسوم أو اشتراكات، ولا يقوم بتعدين مخفي في الخلفية. هدفه الوحيد هو تقديم اختبار مرجعي حقيقي لأداء معالج الهاتف والتعريف بتكنولوجيا البلوكشاين بأعلى معايير النزاهة العلمية."
            )
        }
    }
}

@Composable
private fun EducationalSectionCard(
    icon: ImageVector,
    title: String,
    accentColor: androidx.compose.ui.graphics.Color,
    content: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CryptoBorder, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = CryptoTextPrimary
                )
            }

            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = CryptoTextSecondary,
                lineHeight = 22.sp
            )
        }
    }
}
