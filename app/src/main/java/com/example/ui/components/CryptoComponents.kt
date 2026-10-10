package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CryptoCurrency
import com.example.data.model.TransactionEntity
import com.example.ui.theme.BinanceYellow
import com.example.ui.theme.BitcoinGold
import com.example.ui.theme.BlockchainEmerald
import com.example.ui.theme.CryptoBorder
import com.example.ui.theme.CryptoDarkCard
import com.example.ui.theme.CryptoDarkSurface
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoRed
import com.example.ui.theme.CryptoTextMuted
import com.example.ui.theme.CryptoTextPrimary
import com.example.ui.theme.CryptoTextSecondary
import com.example.ui.theme.EthereumCyan
import com.example.ui.theme.KaspaCyan
import com.example.ui.theme.SolanaPurple
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String,
    icon: @Composable () -> Unit,
    accentColor: Color = BinanceYellow
) {
    Card(
        modifier = modifier
            .border(1.dp, CryptoBorder.copy(alpha = 0.6f), RoundedCornerShape(16.dp)),
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
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = CryptoTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = CryptoTextPrimary
                )
                Text(
                    text = unit,
                    style = MaterialTheme.typography.labelSmall,
                    color = accentColor,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}

@Composable
fun AnimatedMiningPulseCore(
    modifier: Modifier = Modifier,
    isMining: Boolean,
    isTurbo: Boolean,
    hashrate: Double,
    activeCoin: CryptoCurrency
) {
    val infiniteTransition = rememberInfiniteTransition(label = "miningPulse")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isTurbo) 3500 else 7000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    val primaryColor = when (activeCoin) {
        CryptoCurrency.BTC -> BitcoinGold
        CryptoCurrency.ETH -> EthereumCyan
        CryptoCurrency.SOL -> SolanaPurple
        CryptoCurrency.USDT -> BlockchainEmerald
        CryptoCurrency.KAS -> KaspaCyan
    }

    Box(
        modifier = modifier
            .size(220.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val outerRadius = size.minDimension / 2 - 8.dp.toPx()

            // Outer glowing ring
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = if (isMining) 0.25f * pulseAlpha else 0.05f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = outerRadius + 20.dp.toPx()
                ),
                radius = outerRadius + 20.dp.toPx(),
                center = center
            )

            // Outer tech circle border
            drawCircle(
                color = if (isMining) primaryColor.copy(alpha = 0.3f) else CryptoBorder,
                radius = outerRadius,
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )

            // Segmented arc ring (active when mining)
            if (isMining) {
                val sweep = if (isTurbo) 280f else 220f
                drawArc(
                    brush = Brush.sweepGradient(
                        listOf(primaryColor, BinanceYellow, primaryColor)
                    ),
                    startAngle = rotation,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round),
                    size = androidx.compose.ui.geometry.Size(size.width - 24.dp.toPx(), size.height - 24.dp.toPx()),
                    topLeft = Offset(12.dp.toPx(), 12.dp.toPx())
                )
            }

            // Inner dark core
            drawCircle(
                color = CryptoDarkCard,
                radius = outerRadius - 20.dp.toPx(),
                center = center
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = activeCoin.symbol,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                color = primaryColor,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (isMining) String.format(Locale.US, "%.1f", hashrate) else "0.0",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = CryptoTextPrimary
            )
            Text(
                text = "MH/s",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = if (isMining) BlockchainEmerald else CryptoTextMuted
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isMining) (if (isTurbo) BinanceYellow.copy(alpha = 0.2f) else BlockchainEmerald.copy(alpha = 0.2f))
                else CryptoTextMuted.copy(alpha = 0.15f)
            ) {
                Text(
                    text = if (!isMining) "متوقف مؤقتاً" else if (isTurbo) "🔥 توربو أقصى" else "● تعدين نشط",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (!isMining) CryptoTextMuted else if (isTurbo) BinanceYellow else BlockchainEmerald,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun CoinSelectionChip(
    coin: CryptoCurrency,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val chipBorder = if (isSelected) BinanceYellow else CryptoBorder
    val chipBg = if (isSelected) BinanceYellow.copy(alpha = 0.15f) else CryptoDarkCard

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = chipBg,
        modifier = Modifier
            .border(1.dp, chipBorder, RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
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
                text = coin.symbol,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = if (isSelected) BinanceYellow else CryptoTextPrimary
            )
            Text(
                text = coin.nameAr,
                style = MaterialTheme.typography.labelSmall,
                color = CryptoTextSecondary
            )
        }
    }
}

@Composable
fun WithdrawConfirmModal(
    platform: String,
    coin: CryptoCurrency,
    amount: Double,
    recipient: String,
    network: String,
    fee: Double,
    isProcessing: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val isBinance = platform == "Binance"

    AlertDialog(
        onDismissRequest = { if (!isProcessing) onDismiss() },
        containerColor = CryptoDarkSurface,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isBinance) BinanceYellow.copy(alpha = 0.2f) else BlockchainEmerald.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isBinance) Icons.Default.Security else Icons.Default.Hub,
                        contentDescription = null,
                        tint = if (isBinance) BinanceYellow else BlockchainEmerald
                    )
                }
                Text(
                    text = "تأكيد السحب إلى $platform",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = CryptoTextPrimary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Warning note
                Surface(
                    color = CryptoDarkCard,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CryptoBorder, RoundedCornerShape(10.dp))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = if (isBinance)
                                "سيتم إيداع الرصيد مباشرة في حسابك على بايننس فور التأكيد."
                            else
                                "يرجى التأكد من تطابق شبكة البلوكشاين لتجنب فقدان الأصول.",
                            style = MaterialTheme.typography.bodySmall,
                            color = CryptoTextSecondary
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("المبلغ الإجمالي:", color = CryptoTextSecondary, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "${String.format(Locale.US, "%.6f", amount)} ${coin.symbol}",
                        color = BinanceYellow,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("القيمة التقديرية:", color = CryptoTextSecondary, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "≈ $${String.format(Locale.US, "%.2f", amount * coin.currentPriceUsd)} USD",
                        color = CryptoTextPrimary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("الشبكة المختارة:", color = CryptoTextSecondary, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        network,
                        color = CryptoTextPrimary,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("رسوم التحويل:", color = CryptoTextSecondary, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        if (fee == 0.0) "0% (مجاني تماماً)" else "${String.format(Locale.US, "%.6f", fee)} ${coin.symbol}",
                        color = if (fee == 0.0) BlockchainEmerald else CryptoTextPrimary,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Column {
                    Text("الوجهة / الحساب:", color = CryptoTextSecondary, style = MaterialTheme.typography.bodySmall)
                    Text(
                        recipient,
                        color = CryptoTextPrimary,
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (isProcessing) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = if (isBinance) BinanceYellow else BlockchainEmerald,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "جارٍ توقيع المعاملة والبث للشبكة...",
                            style = MaterialTheme.typography.bodySmall,
                            color = CryptoTextSecondary
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isProcessing,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isBinance) BinanceYellow else BlockchainEmerald,
                    contentColor = CryptoDarkSurface
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("confirm_withdraw_button")
            ) {
                Text("تأكيد وإرسال", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                enabled = !isProcessing,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("إلغاء", color = CryptoTextSecondary)
            }
        }
    )
}

@Composable
fun TransactionReceiptModal(
    transaction: TransactionEntity,
    onDismiss: () -> Unit
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val isBinance = transaction.platformName.equals("Binance", ignoreCase = true)
    val accentColor = if (isBinance) BinanceYellow else BlockchainEmerald

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CryptoDarkSurface,
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "تم التحويل بنجاح!",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = CryptoTextPrimary
                )
                Text(
                    text = "إشعار تحويل الأرباح إلى ${transaction.platformName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = CryptoTextSecondary
                )
            }
        },
        text = {
            Card(
                colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CryptoBorder, RoundedCornerShape(14.dp))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("العملة والمبلغ:", style = MaterialTheme.typography.bodySmall, color = CryptoTextSecondary)
                        Text(
                            "${String.format(Locale.US, "%.6f", transaction.amount)} ${transaction.coinSymbol}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = accentColor
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("القيمة بالدولار:", style = MaterialTheme.typography.bodySmall, color = CryptoTextSecondary)
                        Text(
                            "$${String.format(Locale.US, "%.2f", transaction.usdValue)} USD",
                            style = MaterialTheme.typography.bodySmall,
                            color = CryptoTextPrimary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("الشبكة:", style = MaterialTheme.typography.bodySmall, color = CryptoTextSecondary)
                        Text(
                            transaction.network,
                            style = MaterialTheme.typography.bodySmall,
                            color = CryptoTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("حالة المعاملة:", style = MaterialTheme.typography.bodySmall, color = CryptoTextSecondary)
                        Text(
                            "مؤكدة على السلسلة ✓",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = BlockchainEmerald
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = CryptoBorder)

                    Text("معرف المعاملة (TXID / Hash):", style = MaterialTheme.typography.labelSmall, color = CryptoTextSecondary)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CryptoDarkSurface)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = transaction.txHash,
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.labelSmall,
                            color = CryptoTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(transaction.txHash))
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "نسخ الهاش",
                                tint = accentColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = accentColor, contentColor = CryptoDarkSurface),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("تم، إغلاق الإيصال", fontWeight = FontWeight.Bold)
            }
        }
    )
}
