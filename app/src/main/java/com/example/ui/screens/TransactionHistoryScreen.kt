package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TransactionEntity
import com.example.ui.MiningUiState
import com.example.ui.MiningViewModel
import com.example.ui.components.TransactionReceiptModal
import com.example.ui.theme.BinanceYellow
import com.example.ui.theme.BlockchainEmerald
import com.example.ui.theme.CryptoBorder
import com.example.ui.theme.CryptoDarkCard
import com.example.ui.theme.CryptoDarkSurfaceVariant
import com.example.ui.theme.CryptoTextMuted
import com.example.ui.theme.CryptoTextPrimary
import com.example.ui.theme.CryptoTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionHistoryScreen(
    uiState: MiningUiState,
    viewModel: MiningViewModel,
    modifier: Modifier = Modifier
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    var selectedFilter by remember { mutableStateOf("ALL") }
    var selectedTxForModal by remember { mutableStateOf<TransactionEntity?>(null) }

    val filteredList = when (selectedFilter) {
        "BINANCE" -> uiState.transactions.filter { it.platformName.equals("Binance", ignoreCase = true) }
        "BLOCKCHAIN" -> uiState.transactions.filter { it.platformName.equals("Blockchain", ignoreCase = true) }
        else -> uiState.transactions
    }

    if (selectedTxForModal != null) {
        TransactionReceiptModal(
            transaction = selectedTxForModal!!,
            onDismiss = { selectedTxForModal = null }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "سجل السحوبات والمعاملات",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = CryptoTextPrimary
                )
                Text(
                    text = "توثيق كامل لجميع عمليات تحويل أرباح التعدين إلى بايننس والبلوكشاين",
                    style = MaterialTheme.typography.bodySmall,
                    color = CryptoTextSecondary
                )
            }
        }

        // Filter chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == "ALL",
                    onClick = { selectedFilter = "ALL" },
                    label = { Text("جميع السحوبات (${uiState.transactions.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CryptoDarkSurfaceVariant,
                        selectedLabelColor = BinanceYellow
                    )
                )

                FilterChip(
                    selected = selectedFilter == "BINANCE",
                    onClick = { selectedFilter = "BINANCE" },
                    label = { Text("منصة بايننس") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BinanceYellow.copy(alpha = 0.2f),
                        selectedLabelColor = BinanceYellow
                    )
                )

                FilterChip(
                    selected = selectedFilter == "BLOCKCHAIN",
                    onClick = { selectedFilter = "BLOCKCHAIN" },
                    label = { Text("البلوكشاين") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BlockchainEmerald.copy(alpha = 0.2f),
                        selectedLabelColor = BlockchainEmerald
                    )
                )
            }
        }

        if (filteredList.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    colors = CardDefaults.cardColors(containerColor = CryptoDarkCard)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            tint = CryptoTextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "لا توجد معاملات مسجلة في هذا القسم",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CryptoTextSecondary
                        )
                    }
                }
            }
        } else {
            items(filteredList) { tx ->
                val isBinance = tx.platformName.equals("Binance", ignoreCase = true)
                val accentColor = if (isBinance) BinanceYellow else BlockchainEmerald
                val dateFormat = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale.getDefault())
                val formattedDate = dateFormat.format(Date(tx.timestamp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CryptoBorder, RoundedCornerShape(16.dp))
                        .clickable { selectedTxForModal = tx },
                    colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(accentColor.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isBinance) Icons.Default.Security else Icons.Default.Hub,
                                        contentDescription = null,
                                        tint = accentColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = if (isBinance) "تحويل إلى Binance" else "تحويل إلى Blockchain",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = CryptoTextPrimary
                                    )
                                    Text(
                                        text = tx.network,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CryptoTextSecondary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "-${String.format(Locale.US, "%.6f", tx.amount)} ${tx.coinSymbol}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = accentColor
                                )
                                Text(
                                    text = "≈ $${String.format(Locale.US, "%.2f", tx.usdValue)}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CryptoTextMuted
                                )
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 10.dp),
                            color = CryptoBorder.copy(alpha = 0.5f)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = BlockchainEmerald,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "مكتمل على السلسلة",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = BlockchainEmerald
                                )
                            }
                            Text(
                                text = formattedDate,
                                style = MaterialTheme.typography.labelSmall,
                                color = CryptoTextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // TxHash Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(CryptoDarkSurfaceVariant.copy(alpha = 0.6f))
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "TX: ${tx.txHash}",
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = FontFamily.Monospace,
                                color = CryptoTextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { clipboardManager.setText(AnnotatedString(tx.txHash)) },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "نسخ الهاش",
                                    tint = accentColor,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
