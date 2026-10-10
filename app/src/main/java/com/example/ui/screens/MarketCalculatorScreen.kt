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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CryptoCurrency
import com.example.ui.MiningUiState
import com.example.ui.MiningViewModel
import com.example.ui.theme.BinanceYellow
import com.example.ui.theme.BitcoinGold
import com.example.ui.theme.BlockchainEmerald
import com.example.ui.theme.CryptoBorder
import com.example.ui.theme.CryptoDarkCard
import com.example.ui.theme.CryptoDarkSurface
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoTextMuted
import com.example.ui.theme.CryptoTextPrimary
import com.example.ui.theme.CryptoTextSecondary
import com.example.ui.theme.EthereumCyan
import com.example.ui.theme.KaspaCyan
import com.example.ui.theme.SolanaPurple
import java.util.Locale

@Composable
fun MarketCalculatorScreen(
    uiState: MiningUiState,
    viewModel: MiningViewModel,
    modifier: Modifier = Modifier
) {
    var customHashrateMh by remember { mutableFloatStateOf(uiState.profile.baseHashrate.toFloat()) }
    val activeCoin = CryptoCurrency.fromSymbol(uiState.profile.activeCoinSymbol)

    // Projected calculations
    val hourlyUsd = customHashrateMh * 0.00000035 * 3600
    val dailyUsd = hourlyUsd * 24
    val weeklyUsd = dailyUsd * 7
    val monthlyUsd = dailyUsd * 30

    val dailyCrypto = dailyUsd / activeCoin.currentPriceUsd
    val monthlyCrypto = monthlyUsd / activeCoin.currentPriceUsd

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "أسعار العملات وحاسبة الأرباح",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = CryptoTextPrimary
                )
                Text(
                    text = "متابعة مباشرة لأسعار العملات وحساب العوائد التقديرية للتعدين",
                    style = MaterialTheme.typography.bodySmall,
                    color = CryptoTextSecondary
                )
            }
        }

        // Live Market Prices Ticker
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CryptoBorder, RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "أسعار السوق الحالية (Live Rates):",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = CryptoTextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    for (coin in CryptoCurrency.entries) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when (coin) {
                                                CryptoCurrency.BTC -> BitcoinGold
                                                CryptoCurrency.ETH -> EthereumCyan
                                                CryptoCurrency.SOL -> SolanaPurple
                                                CryptoCurrency.USDT -> BlockchainEmerald
                                                CryptoCurrency.KAS -> KaspaCyan
                                            }
                                        )
                                )
                                Text(
                                    text = "${coin.nameAr} (${coin.symbol})",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = CryptoTextPrimary
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "$${String.format(Locale.US, "%,.2f", coin.currentPriceUsd)}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = CryptoTextPrimary
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = BlockchainEmerald.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "+3.4%",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = BlockchainEmerald,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Profitability Calculator Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BinanceYellow.copy(alpha = 0.5f), RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = null,
                                tint = BinanceYellow
                            )
                            Text(
                                text = "حاسبة أرباح التعدين",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = BinanceYellow
                            )
                        }
                        Text(
                            text = "${customHashrateMh.toInt()} MH/s",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = BlockchainEmerald
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "اسحب الشريط لتعديل معدل الهاش ريت:",
                        style = MaterialTheme.typography.bodySmall,
                        color = CryptoTextSecondary
                    )

                    Slider(
                        value = customHashrateMh,
                        onValueChange = { customHashrateMh = it },
                        valueRange = 20f..800f,
                        colors = SliderDefaults.colors(
                            thumbColor = BinanceYellow,
                            activeTrackColor = BinanceYellow,
                            inactiveTrackColor = CryptoBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Yield matrix
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        YieldBox(
                            modifier = Modifier.weight(1f),
                            period = "يومياً (24h)",
                            usd = String.format(Locale.US, "$%.2f", dailyUsd),
                            crypto = "${String.format(Locale.US, "%.5f", dailyCrypto)} ${activeCoin.symbol}"
                        )
                        YieldBox(
                            modifier = Modifier.weight(1f),
                            period = "أسبوعياً",
                            usd = String.format(Locale.US, "$%.2f", weeklyUsd),
                            crypto = "${String.format(Locale.US, "%.4f", dailyCrypto * 7)} ${activeCoin.symbol}"
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    YieldBox(
                        modifier = Modifier.fillMaxWidth(),
                        period = "شهرياً (30 يوم)",
                        usd = String.format(Locale.US, "$%.2f USD", monthlyUsd),
                        crypto = "${String.format(Locale.US, "%.4f", monthlyCrypto)} ${activeCoin.symbol}",
                        isHighlighted = true
                    )
                }
            }
        }

        // How it works info card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CryptoBorder, RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = CryptoDarkSurface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = null,
                            tint = BinanceYellow,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "كيف يتم تحويل أرباح التعدين؟",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = CryptoTextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• منصة Binance: يمكنك التحويل بدون رسوم (0%) فورياً باستخدام معرّف Binance Pay أو معرّف المستخدم (UID)، أو عبر شبكة BNB Smart Chain.\n" +
                                "• البلوكشاين: يمكنك التحويل لأي محفظة باردة أو ساخنة (مثل Trust Wallet أو Ledger) مباشرة عبر عنوان البلوكشاين الخاص بك.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CryptoTextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun YieldBox(
    modifier: Modifier = Modifier,
    period: String,
    usd: String,
    crypto: String,
    isHighlighted: Boolean = false
) {
    Surface(
        modifier = modifier
            .border(
                1.dp,
                if (isHighlighted) BinanceYellow else CryptoBorder,
                RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        color = if (isHighlighted) BinanceYellow.copy(alpha = 0.1f) else CryptoDarkSurface
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = period,
                style = MaterialTheme.typography.labelSmall,
                color = CryptoTextSecondary
            )
            Text(
                text = usd,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = if (isHighlighted) BinanceYellow else CryptoTextPrimary
            )
            Text(
                text = crypto,
                style = MaterialTheme.typography.labelSmall,
                color = BlockchainEmerald
            )
        }
    }
}
