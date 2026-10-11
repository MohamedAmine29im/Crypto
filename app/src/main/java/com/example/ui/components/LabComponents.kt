package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.benchmark.BenchmarkEngineState
import com.example.data.model.CandidateBlock
import com.example.data.model.DeviceHardwareStatus
import com.example.data.model.DifficultyLevel
import com.example.ui.theme.BitcoinGold
import com.example.ui.theme.BlockchainEmerald
import com.example.ui.theme.CryptoBorder
import com.example.ui.theme.CryptoDarkCard
import com.example.ui.theme.CryptoDarkSurface
import com.example.ui.theme.CryptoRed
import com.example.ui.theme.CryptoTextMuted
import com.example.ui.theme.CryptoTextPrimary
import com.example.ui.theme.CryptoTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HeroBannerCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .border(1.dp, BitcoinGold.copy(alpha = 0.5f), RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.btc_lab_banner_1791708252178),
                contentDescription = "Bitcoin Mining Research Lab Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.35f),
                                Color.Black.copy(alpha = 0.88f)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BitcoinGold.copy(alpha = 0.25f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = BitcoinGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "اختبار SHA-256 محلي آمن",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = BitcoinGold
                            )
                        }
                    }
                    Text(
                        text = "Samsung S21+ 5G Lab",
                        style = MaterialTheme.typography.labelSmall,
                        color = CryptoTextSecondary
                    )
                }

                Column {
                    Text(
                        text = "Bitcoin Mining Lab",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                        color = CryptoTextPrimary,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "مختبر حقيقي لقياس سرعة التجزئة الرياضية وإثبات العمل (Proof-of-Work)",
                        style = MaterialTheme.typography.bodySmall,
                        color = CryptoTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun EducationalNoticeCard(
    onLearnMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BitcoinGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(BitcoinGold.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = BitcoinGold,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "تنبيه الشفافية والمصداقية التقنية",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = BitcoinGold
                )
            }

            Text(
                text = "هذا التطبيق يقوم بحسابات خوارزمية SHA-256 حقيقية داخل معالج الهاتف لتقييم الأداء محلياً للأغراض العلمية. هو ليس تعديناً لبيتكوين حقيقي على الشبكة العالمية، ولا يوهم المستخدم بأي أرصدة أو أرباح مالية زائفة.",
                style = MaterialTheme.typography.bodySmall,
                color = CryptoTextSecondary,
                lineHeight = 19.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onLearnMoreClick,
                    modifier = Modifier.testTag("learn_more_button")
                ) {
                    Text(
                        text = "قراءة التفاصيل التقنية للشبكة ➔",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = BitcoinGold
                    )
                }
            }
        }
    }
}

@Composable
fun HashrateGaugeCard(
    engineState: BenchmarkEngineState,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "coreRotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (engineState.isRunning) 3000 else 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, CryptoBorder, RoundedCornerShape(20.dp))
            .testTag("hashrate_gauge_card"),
        colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Rotating ring with hashrate display
            Box(
                modifier = Modifier.size(190.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(rotation)
                        .border(
                            4.dp,
                            Brush.sweepGradient(
                                if (engineState.isRunning)
                                    listOf(BitcoinGold, BlockchainEmerald, BitcoinGold)
                                else
                                    listOf(CryptoBorder, CryptoDarkSurface, CryptoBorder)
                            ),
                            CircleShape
                        )
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (engineState.isRunning) {
                            formatHashrate(engineState.hashrateHps)
                        } else {
                            "0.0 H/s"
                        },
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.SansSerif
                        ),
                        color = if (engineState.isRunning) BitcoinGold else CryptoTextMuted
                    )
                    Text(
                        text = "Double SHA-256",
                        style = MaterialTheme.typography.labelSmall,
                        color = CryptoTextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (engineState.isRunning) BlockchainEmerald.copy(alpha = 0.2f) else CryptoBorder.copy(alpha = 0.4f)
                    ) {
                        Text(
                            text = if (engineState.isRunning) "● الاختبار نشط" else "○ الاختبار متوقف",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (engineState.isRunning) BlockchainEmerald else CryptoTextMuted,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Stats grid (Total Hashes & Elapsed Time)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CryptoDarkSurface,
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, CryptoBorder, RoundedCornerShape(12.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "إجمالي التجزئات",
                            style = MaterialTheme.typography.labelSmall,
                            color = CryptoTextSecondary
                        )
                        Text(
                            text = String.format(Locale.US, "%,d", engineState.totalHashes),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = CryptoTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CryptoDarkSurface,
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, CryptoBorder, RoundedCornerShape(12.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "الوقت المنقضي",
                            style = MaterialTheme.typography.labelSmall,
                            color = CryptoTextSecondary
                        )
                        Text(
                            text = formatElapsedTime(engineState.elapsedSeconds),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = CryptoTextPrimary
                        )
                    }
                }
            }

            // Rolling candidate hash string
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(CryptoDarkSurface)
                    .border(1.dp, CryptoBorder, RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "آخر هاش تجريبي (Candidate Hash):",
                        style = MaterialTheme.typography.labelSmall,
                        color = CryptoTextSecondary
                    )
                    Text(
                        text = "Nonce: #${engineState.currentNonce}",
                        style = MaterialTheme.typography.labelSmall,
                        color = BitcoinGold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = engineState.lastCandidateHash,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (engineState.lastCandidateHash.startsWith(engineState.selectedDifficulty.hexPrefix)) BlockchainEmerald else CryptoTextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun BenchmarkControlsRow(
    isRunning: Boolean,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (!isRunning) {
            Button(
                onClick = onStartClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlockchainEmerald,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("start_benchmark_button")
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "بدء الاختبار (START)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        } else {
            Button(
                onClick = onStopClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CryptoRed,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("stop_benchmark_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "إيقاف الاختبار (STOP)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        OutlinedButton(
            onClick = onResetClick,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .height(52.dp)
                .testTag("reset_stats_button")
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "تصفير",
                tint = CryptoTextSecondary
            )
        }
    }
}

@Composable
fun DifficultySelectorSection(
    selectedDifficulty: DifficultyLevel,
    onSelectDifficulty: (DifficultyLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "مستوى الصعوبة الرياضي:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = CryptoTextPrimary
            )
            Text(
                text = "البادئة: ${selectedDifficulty.hexPrefix}",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = BitcoinGold,
                fontFamily = FontFamily.Monospace
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(DifficultyLevel.entries) { diff ->
                val isSelected = diff == selectedDifficulty
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) BitcoinGold.copy(alpha = 0.2f) else CryptoDarkCard,
                    modifier = Modifier
                        .border(
                            1.dp,
                            if (isSelected) BitcoinGold else CryptoBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onSelectDifficulty(diff) }
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = diff.nameAr,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isSelected) BitcoinGold else CryptoTextPrimary
                        )
                        Text(
                            text = "يبدأ بـ ${diff.hexPrefix}",
                            style = MaterialTheme.typography.labelSmall,
                            color = CryptoTextSecondary,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HardwareQuickStatusBar(
    hardwareStatus: DeviceHardwareStatus,
    onOpenHardwareScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = CryptoDarkCard,
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, CryptoBorder, RoundedCornerShape(14.dp))
            .clickable { onOpenHardwareScreen() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Thermostat,
                    contentDescription = null,
                    tint = if (hardwareStatus.temperatureC >= 40.0) CryptoRed else BlockchainEmerald,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "${hardwareStatus.temperatureC}°C",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (hardwareStatus.temperatureC >= 40.0) CryptoRed else CryptoTextPrimary
                )
                Text(
                    text = "•",
                    color = CryptoTextMuted
                )
                Text(
                    text = "البطارية: ${hardwareStatus.batteryPercent}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = CryptoTextSecondary
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Memory,
                    contentDescription = null,
                    tint = BitcoinGold,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${hardwareStatus.activeThreads} خيوط معالجة",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = BitcoinGold
                )
            }
        }
    }
}

@Composable
fun CandidateBlockCard(
    block: CandidateBlock,
    modifier: Modifier = Modifier
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val formattedTime = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date(block.timestamp))

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CryptoDarkSurface),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BlockchainEmerald.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = BlockchainEmerald,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "كتلة تجريبية محلولة #${block.blockIndex}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = BlockchainEmerald
                    )
                }
                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.labelSmall,
                    color = CryptoTextMuted
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Nonce: #${block.nonce}",
                    style = MaterialTheme.typography.labelSmall,
                    color = BitcoinGold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "المدة: ${block.durationMs / 1000.0}ث",
                    style = MaterialTheme.typography.labelSmall,
                    color = CryptoTextSecondary
                )
            }

            Text(
                text = block.hashHex,
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.labelSmall,
                color = CryptoTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable {
                        clipboardManager.setText(AnnotatedString(block.hashHex))
                    }
                    .padding(6.dp)
            )
        }
    }
}

@Composable
fun EducationalDisclaimerDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CryptoDarkSurface,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = BitcoinGold
                )
                Text(
                    text = "مختبر تعليمي لأداء SHA-256",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = CryptoTextPrimary
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "أهلاً بك في تطبيق Bitcoin Mining Lab المخصص لهاتف Samsung Galaxy S21+ 5G.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = BitcoinGold
                )
                Text(
                    text = "• التطبيق مخصص لاختبار سرعة المعالج محلياً في تنفيذ التجزئة الرياضية (Double SHA-256 Proof-of-Work).\n• شبكة البيتكوين الحقيقية العالمية تعتمد على مزارع أجهزة ASIC تفوق 700 إكساهاش ولا يمكن تعدينها بالهواتف.\n• التطبيق لا يطلب أي ودائع، ولا يوهم بأي أرباح خيالية، ولا يستنزف الهاتف في الخلفية، ويحتوي على نظام حماية حرارية تلقائي.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CryptoTextSecondary,
                    lineHeight = 20.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BitcoinGold,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("dismiss_disclaimer_button")
            ) {
                Text("فهمت ذلك، المتابعة إلى المختبر", fontWeight = FontWeight.Bold)
            }
        }
    )
}

private fun formatHashrate(hps: Double): String {
    return when {
        hps >= 1_000_000 -> String.format(Locale.US, "%.2f MH/s", hps / 1_000_000.0)
        hps >= 1_000 -> String.format(Locale.US, "%.1f kH/s", hps / 1_000.0)
        else -> String.format(Locale.US, "%.0f H/s", hps)
    }
}

private fun formatElapsedTime(seconds: Long): String {
    val hrs = seconds / 3600
    val mins = (seconds % 3600) / 60
    val secs = seconds % 60
    return if (hrs > 0) {
        String.format(Locale.US, "%02d:%02d:%02d", hrs, mins, secs)
    } else {
        String.format(Locale.US, "%02d:%02d", mins, secs)
    }
}
