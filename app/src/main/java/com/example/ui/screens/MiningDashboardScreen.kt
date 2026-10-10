package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CryptoCurrency
import com.example.ui.MiningUiState
import com.example.ui.MiningViewModel
import com.example.ui.Screen
import com.example.ui.components.AnimatedMiningPulseCore
import com.example.ui.components.CoinSelectionChip
import com.example.ui.components.MetricCard
import com.example.ui.theme.BinanceYellow
import com.example.ui.theme.BitcoinGold
import com.example.ui.theme.BlockchainEmerald
import com.example.ui.theme.CryptoBorder
import com.example.ui.theme.CryptoDarkCard
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoRed
import com.example.ui.theme.CryptoTextMuted
import com.example.ui.theme.CryptoTextPrimary
import com.example.ui.theme.CryptoTextSecondary
import java.util.Locale

@Composable
fun MiningDashboardScreen(
    uiState: MiningUiState,
    viewModel: MiningViewModel,
    modifier: Modifier = Modifier
) {
    val activeCoin = CryptoCurrency.fromSymbol(uiState.profile.activeCoinSymbol)
    val totalUsdValue = (uiState.profile.btcBalance * CryptoCurrency.BTC.currentPriceUsd) +
            (uiState.profile.ethBalance * CryptoCurrency.ETH.currentPriceUsd) +
            (uiState.profile.solBalance * CryptoCurrency.SOL.currentPriceUsd) +
            (uiState.profile.usdtBalance * CryptoCurrency.USDT.currentPriceUsd) +
            (uiState.profile.kasBalance * CryptoCurrency.KAS.currentPriceUsd)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero visual card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .border(1.dp, CryptoBorder, RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.crypto_mining_hero_1791643242804),
                        contentDescription = "Mining Rig Connectivity",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Dark gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                androidx.compose.ui.graphics.Brush.verticalGradient(
                                    listOf(
                                        androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.4f),
                                        androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.85f)
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
                                color = BinanceYellow.copy(alpha = 0.25f)
                            ) {
                                Text(
                                    text = "⚡ شبكة سحابية نشطة",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = BinanceYellow,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = "Binance ⇄ Web3 Ledger",
                                style = MaterialTheme.typography.labelSmall,
                                color = CryptoTextSecondary
                            )
                        }

                        Column {
                            Text(
                                text = "محطة تعدين العملات الرقمية",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = CryptoTextPrimary
                            )
                            Text(
                                text = "تعدين آلي ذكي مع إمكانية التحويل الفوري للأرباح",
                                style = MaterialTheme.typography.bodySmall,
                                color = CryptoTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Animated Mining Core
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CryptoBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedMiningPulseCore(
                        isMining = uiState.profile.isMining,
                        isTurbo = uiState.profile.isTurbo,
                        hashrate = uiState.liveHashrate,
                        activeCoin = activeCoin
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Coin selector row
                    Text(
                        text = "العملة الجاري تعدينها حالياً:",
                        style = MaterialTheme.typography.bodySmall,
                        color = CryptoTextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(CryptoCurrency.entries) { coin ->
                            CoinSelectionChip(
                                coin = coin,
                                isSelected = coin.symbol == uiState.profile.activeCoinSymbol,
                                onClick = { viewModel.selectMiningCoin(coin) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mining Control Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.toggleMining() },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("toggle_mining_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (uiState.profile.isMining) CryptoRed else BlockchainEmerald,
                                contentColor = CryptoTextPrimary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = if (uiState.profile.isMining) Icons.Default.Stop else Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (uiState.profile.isMining) "إيقاف مؤقت" else "بدء التعدين",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = { viewModel.toggleTurbo() },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .border(
                                    1.dp,
                                    if (uiState.profile.isTurbo) BinanceYellow else CryptoBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .testTag("toggle_turbo_button"),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (uiState.profile.isTurbo) BinanceYellow else CryptoTextSecondary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = null,
                                tint = if (uiState.profile.isTurbo) BinanceYellow else CryptoTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (uiState.profile.isTurbo) "التوربو نشط 🔥" else "تفعيل التوربو",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Unmined Balances & Quick Transfer CTA Card
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
                        Column {
                            Text(
                                text = "إجمالي الأرباح المتاحة للسحب",
                                style = MaterialTheme.typography.bodySmall,
                                color = CryptoTextSecondary
                            )
                            Text(
                                text = "$${String.format(Locale.US, "%,.2f", totalUsdValue)} USD",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                                color = BinanceYellow
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = BlockchainEmerald.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "جاهز للتحويل ✓",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = BlockchainEmerald,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Individual coin mini chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CoinBalanceMini(coin = "BTC", balance = uiState.profile.btcBalance)
                        CoinBalanceMini(coin = "ETH", balance = uiState.profile.ethBalance)
                        CoinBalanceMini(coin = "SOL", balance = uiState.profile.solBalance)
                        CoinBalanceMini(coin = "USDT", balance = uiState.profile.usdtBalance)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Big CTA to withdraw
                    Button(
                        onClick = { viewModel.setScreen(Screen.TRANSFER) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("go_to_withdraw_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BinanceYellow,
                            contentColor = androidx.compose.ui.graphics.Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "تحويل الأرباح الآن إلى بايننس / بلوكشاين",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Hardware Telemetry Metrics (4-grid)
        item {
            Text(
                text = "بيانات ومؤشرات العتاد الحيّة",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = CryptoTextPrimary,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    title = "درجة الحرارة",
                    value = "${uiState.profile.temperatureC}°",
                    unit = "C",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Thermostat,
                            contentDescription = null,
                            tint = if (uiState.profile.temperatureC > 65) CryptoRed else BinanceYellow,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    accentColor = if (uiState.profile.temperatureC > 65) CryptoRed else BinanceYellow
                )

                MetricCard(
                    modifier = Modifier.weight(1f),
                    title = "استهلاك الطاقة",
                    value = "${uiState.profile.powerWatts}",
                    unit = "W",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ElectricBolt,
                            contentDescription = null,
                            tint = BlockchainEmerald,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    accentColor = BlockchainEmerald
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    title = "سرعة المراوح",
                    value = "${uiState.profile.fanSpeedPercent}%",
                    unit = "RPM",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Air,
                            contentDescription = null,
                            tint = BinanceYellow,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    accentColor = BinanceYellow
                )

                MetricCard(
                    modifier = Modifier.weight(1f),
                    title = "الحصص المقبولة",
                    value = "${uiState.profile.acceptedShares}",
                    unit = "Shares",
                    icon = {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = BlockchainEmerald,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    accentColor = BlockchainEmerald
                )
            }
        }
    }
}

@Composable
private fun CoinBalanceMini(coin: String, balance: Double) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = coin,
            style = MaterialTheme.typography.labelSmall,
            color = CryptoTextSecondary
        )
        Text(
            text = String.format(Locale.US, if (coin == "USDT") "%.1f" else "%.4f", balance),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = CryptoTextPrimary
        )
    }
}
