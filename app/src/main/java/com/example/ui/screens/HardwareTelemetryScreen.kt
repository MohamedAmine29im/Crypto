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
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.BenchmarkUiState
import com.example.ui.BenchmarkViewModel
import com.example.ui.theme.BitcoinGold
import com.example.ui.theme.BlockchainEmerald
import com.example.ui.theme.CryptoBorder
import com.example.ui.theme.CryptoDarkCard
import com.example.ui.theme.CryptoDarkSurface
import com.example.ui.theme.CryptoRed
import com.example.ui.theme.CryptoTextPrimary
import com.example.ui.theme.CryptoTextSecondary
import java.util.Locale

@Composable
fun HardwareTelemetryScreen(
    uiState: BenchmarkUiState,
    viewModel: BenchmarkViewModel,
    modifier: Modifier = Modifier
) {
    val hardware = uiState.hardwareStatus

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
                    text = "مراقبة العتاد وحماية الجهاز",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = CryptoTextPrimary
                )
                Text(
                    text = "مُهيأ لهاتف Samsung Galaxy S21+ 5G مع حماية حرارية متقدمة",
                    style = MaterialTheme.typography.bodySmall,
                    color = CryptoTextSecondary
                )
            }
        }

        // Device Profile Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CryptoBorder, RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(BitcoinGold.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhoneAndroid,
                            contentDescription = null,
                            tint = BitcoinGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Samsung Galaxy S21+ 5G",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = CryptoTextPrimary
                        )
                        Text(
                            text = "المعالج: 8 أنوية (Octa-Core CPU) • ذاكرة RAM عالية السرعة",
                            style = MaterialTheme.typography.bodySmall,
                            color = CryptoTextSecondary
                        )
                        Text(
                            text = "البطارية: 4800 mAh Li-Ion مع مستشعر حراري مدمج",
                            style = MaterialTheme.typography.labelSmall,
                            color = BlockchainEmerald
                        )
                    }
                }
            }
        }

        // Temperature & Battery Gauges
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Temperature Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, CryptoBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("درجة الحرارة", style = MaterialTheme.typography.bodySmall, color = CryptoTextSecondary)
                            Icon(
                                imageVector = Icons.Default.Thermostat,
                                contentDescription = null,
                                tint = if (hardware.temperatureC >= 40.0) CryptoRed else BlockchainEmerald,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "${hardware.temperatureC}°C",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                            color = if (hardware.temperatureC >= 40.0) CryptoRed else BlockchainEmerald
                        )
                        Text(
                            text = if (hardware.temperatureC < 37.0) "الحرارة ممتازة ومثالية" else if (hardware.temperatureC < 41.0) "حرارة معتدلة أثناء الضغط" else "حرارة مرتفعة - تحذير",
                            style = MaterialTheme.typography.labelSmall,
                            color = CryptoTextSecondary
                        )
                    }
                }

                // Battery Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, CryptoBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("مستوى البطارية", style = MaterialTheme.typography.bodySmall, color = CryptoTextSecondary)
                            Icon(
                                imageVector = Icons.Default.BatteryChargingFull,
                                contentDescription = null,
                                tint = if (hardware.isCharging) BlockchainEmerald else BitcoinGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "${hardware.batteryPercent}%",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                            color = CryptoTextPrimary
                        )
                        Text(
                            text = hardware.chargingSource,
                            style = MaterialTheme.typography.labelSmall,
                            color = CryptoTextSecondary,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // CPU Threads Selector
        item {
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
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Memory, contentDescription = null, tint = BitcoinGold)
                            Text(
                                text = "عدد خيوط المعالجة النشطة (Threads)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = CryptoTextPrimary
                            )
                        }
                        Text(
                            text = "${hardware.activeThreads} من أصل ${hardware.availableCores}",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = BitcoinGold
                        )
                    }

                    Text(
                        text = "يُنصح بتخصيص 2 إلى 4 خيوط لتحقيق أفضل توازن بين السرعة والحفاظ على برودة الهاتف.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CryptoTextSecondary
                    )

                    Slider(
                        value = hardware.activeThreads.toFloat(),
                        onValueChange = { viewModel.setThreads(it.toInt()) },
                        valueRange = 1f..hardware.availableCores.toFloat(),
                        steps = hardware.availableCores - 2,
                        colors = SliderDefaults.colors(
                            thumbColor = BitcoinGold,
                            activeTrackColor = BitcoinGold
                        )
                    )
                }
            }
        }

        // Thermal Safety Cutoff
        item {
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
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = BlockchainEmerald)
                            Text(
                                text = "حد الإيقاف التلقائي للحرارة",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = CryptoTextPrimary
                            )
                        }
                        Text(
                            text = "${String.format(Locale.US, "%.1f", hardware.maxTempThresholdC)}°C",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = BlockchainEmerald
                        )
                    }

                    Text(
                        text = "يقوم التطبيق بإيقاف الاختبار فوراً وبشكل آلي إذا وصلت حرارة البطارية إلى هذا الحد لحماية الجهاز.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CryptoTextSecondary
                    )

                    Slider(
                        value = hardware.maxTempThresholdC.toFloat(),
                        onValueChange = { viewModel.setMaxTempThreshold(it.toDouble()) },
                        valueRange = 38f..45f,
                        colors = SliderDefaults.colors(
                            thumbColor = BlockchainEmerald,
                            activeTrackColor = BlockchainEmerald
                        )
                    )
                }
            }
        }

        // Throttle Delay (Cooling delay)
        item {
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
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "فاصل التبريد بين الدفعات (Throttle)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = CryptoTextPrimary
                        )
                        Text(
                            text = "${hardware.throttleDelayMs} ms",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = BitcoinGold
                        )
                    }

                    Text(
                        text = "إضافة فترات توقف وجيزة (5ms - 20ms) تسمح لمعالج الهاتف بتبريد نفسه ومنع تفعيل اختناق المعالج الحراري (Thermal Throttling).",
                        style = MaterialTheme.typography.bodySmall,
                        color = CryptoTextSecondary
                    )

                    Slider(
                        value = hardware.throttleDelayMs.toFloat(),
                        onValueChange = { viewModel.setThrottleDelay(it.toLong()) },
                        valueRange = 0f..30f,
                        colors = SliderDefaults.colors(
                            thumbColor = BitcoinGold,
                            activeTrackColor = BitcoinGold
                        )
                    )
                }
            }
        }
    }
}
