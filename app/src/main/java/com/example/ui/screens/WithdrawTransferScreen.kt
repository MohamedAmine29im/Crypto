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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CryptoCurrency
import com.example.ui.BinanceTransferMode
import com.example.ui.GasSpeed
import com.example.ui.MiningUiState
import com.example.ui.MiningViewModel
import com.example.ui.WithdrawTab
import com.example.ui.components.CoinSelectionChip
import com.example.ui.theme.BinanceYellow
import com.example.ui.theme.BlockchainEmerald
import com.example.ui.theme.CryptoBorder
import com.example.ui.theme.CryptoDarkCard
import com.example.ui.theme.CryptoDarkSurface
import com.example.ui.theme.CryptoDarkSurfaceVariant
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoTextMuted
import com.example.ui.theme.CryptoTextPrimary
import com.example.ui.theme.CryptoTextSecondary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithdrawTransferScreen(
    uiState: MiningUiState,
    viewModel: MiningViewModel,
    modifier: Modifier = Modifier
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val currentCoin = uiState.selectedCoin
    val isBinance = uiState.withdrawTab == WithdrawTab.BINANCE
    val accentColor = if (isBinance) BinanceYellow else BlockchainEmerald

    val coinBalance = when (currentCoin) {
        CryptoCurrency.BTC -> uiState.profile.btcBalance
        CryptoCurrency.ETH -> uiState.profile.ethBalance
        CryptoCurrency.SOL -> uiState.profile.solBalance
        CryptoCurrency.USDT -> uiState.profile.usdtBalance
        CryptoCurrency.KAS -> uiState.profile.kasBalance
    }

    var networkDropdownExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "سحب وتحويل الأرباح",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = CryptoTextPrimary
                )
                Text(
                    text = "تحويل مباشر وآمن إلى منصة Binance أو أي محفظة بلوكشاين",
                    style = MaterialTheme.typography.bodySmall,
                    color = CryptoTextSecondary
                )
            }
        }

        // Top Platform Switcher Tabs (Binance vs Blockchain)
        item {
            TabRow(
                selectedTabIndex = if (isBinance) 0 else 1,
                containerColor = CryptoDarkCard,
                contentColor = CryptoTextPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[if (isBinance) 0 else 1]),
                        color = accentColor,
                        height = 3.dp
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, CryptoBorder, RoundedCornerShape(14.dp))
            ) {
                Tab(
                    selected = isBinance,
                    onClick = { viewModel.setWithdrawTab(WithdrawTab.BINANCE) },
                    modifier = Modifier.testTag("tab_binance")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(BinanceYellow)
                        )
                        Text(
                            text = "منصة بايننس (Binance)",
                            fontWeight = if (isBinance) FontWeight.Bold else FontWeight.Normal,
                            color = if (isBinance) BinanceYellow else CryptoTextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }

                Tab(
                    selected = !isBinance,
                    onClick = { viewModel.setWithdrawTab(WithdrawTab.BLOCKCHAIN) },
                    modifier = Modifier.testTag("tab_blockchain")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(BlockchainEmerald)
                        )
                        Text(
                            text = "البلوكشاين (Web3)",
                            fontWeight = if (!isBinance) FontWeight.Bold else FontWeight.Normal,
                            color = if (!isBinance) BlockchainEmerald else CryptoTextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Platform specific info banner
        item {
            if (isBinance) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BinanceYellow.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = BinanceYellow.copy(alpha = 0.08f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BinanceYellow.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = BinanceYellow,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "تحويل بايننس المباشر (Binance Pay / UID)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = BinanceYellow
                            )
                            Text(
                                text = "رسوم تحويل 0%، سرعة إيداع فورية خلال ثوانٍ معدودة مباشرة لحسابك.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CryptoTextSecondary
                            )
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BlockchainEmerald.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = BlockchainEmerald.copy(alpha = 0.08f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BlockchainEmerald.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Hub,
                                contentDescription = null,
                                tint = BlockchainEmerald,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "تحويل على شبكة البلوكشاين اللامركزية",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = BlockchainEmerald
                            )
                            Text(
                                text = "إرسال إلى محفظة TrustWallet، MetaMask، Ledger أو أي عنوان على السلسلة.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CryptoTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Coin Selection
        item {
            Column {
                Text(
                    text = "اختر العملة المراد سحبها:",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = CryptoTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(CryptoCurrency.entries) { coin ->
                        CoinSelectionChip(
                            coin = coin,
                            isSelected = coin == currentCoin,
                            onClick = { viewModel.selectCoin(coin) }
                        )
                    }
                }
            }
        }

        // If Binance: Mode Switcher (Binance Pay ID vs BEP-20)
        if (isBinance) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (uiState.binanceMode == BinanceTransferMode.BINANCE_PAY)
                            BinanceYellow.copy(alpha = 0.2f) else CryptoDarkCard,
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                1.dp,
                                if (uiState.binanceMode == BinanceTransferMode.BINANCE_PAY) BinanceYellow else CryptoBorder,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { viewModel.setBinanceMode(BinanceTransferMode.BINANCE_PAY) }
                    ) {
                        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Binance Pay / UID",
                                fontWeight = FontWeight.Bold,
                                color = if (uiState.binanceMode == BinanceTransferMode.BINANCE_PAY) BinanceYellow else CryptoTextPrimary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "0% رسوم (فوري)",
                                style = MaterialTheme.typography.labelSmall,
                                color = BlockchainEmerald
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (uiState.binanceMode == BinanceTransferMode.BEP20_CHAIN)
                            BinanceYellow.copy(alpha = 0.2f) else CryptoDarkCard,
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                1.dp,
                                if (uiState.binanceMode == BinanceTransferMode.BEP20_CHAIN) BinanceYellow else CryptoBorder,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { viewModel.setBinanceMode(BinanceTransferMode.BEP20_CHAIN) }
                    ) {
                        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "عنوان إيداع BEP-20",
                                fontWeight = FontWeight.Bold,
                                color = if (uiState.binanceMode == BinanceTransferMode.BEP20_CHAIN) BinanceYellow else CryptoTextPrimary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "شبكة BNB Smart Chain",
                                style = MaterialTheme.typography.labelSmall,
                                color = CryptoTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Network Selection Dropdown (Especially for Blockchain)
        if (!isBinance) {
            item {
                Column {
                    Text(
                        text = "شبكة البلوكشاين (Network):",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = CryptoTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    ExposedDropdownMenuBox(
                        expanded = networkDropdownExpanded,
                        onExpandedChange = { networkDropdownExpanded = !networkDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = uiState.selectedNetwork,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = networkDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CryptoDarkCard,
                                unfocusedContainerColor = CryptoDarkCard,
                                focusedBorderColor = BlockchainEmerald,
                                unfocusedBorderColor = CryptoBorder,
                                focusedTextColor = CryptoTextPrimary,
                                unfocusedTextColor = CryptoTextPrimary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = networkDropdownExpanded,
                            onDismissRequest = { networkDropdownExpanded = false },
                            modifier = Modifier.background(CryptoDarkCard)
                        ) {
                            for (network in currentCoin.blockchainNetworks) {
                                DropdownMenuItem(
                                    text = { Text(network, color = CryptoTextPrimary) },
                                    onClick = {
                                        viewModel.setSelectedNetwork(network)
                                        networkDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Destination Input (Binance ID or Blockchain Address)
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBinance) {
                            if (uiState.binanceMode == BinanceTransferMode.BINANCE_PAY)
                                "معرّف بايننس (Binance User ID / Pay ID):"
                            else "عنوان إيداع بايننس:"
                        } else {
                            "عنوان محفظة ${currentCoin.nameAr} على البلوكشاين:"
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = CryptoTextPrimary
                    )
                    TextButton(
                        onClick = {
                            val clip = clipboardManager.getText()?.text
                            if (!clip.isNullOrBlank()) {
                                viewModel.setDestinationAddress(clip.trim())
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentPaste,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("لصق من الحافظة", color = accentColor, style = MaterialTheme.typography.labelSmall)
                    }
                }

                OutlinedTextField(
                    value = uiState.destinationAddressText,
                    onValueChange = { viewModel.setDestinationAddress(it) },
                    placeholder = {
                        Text(
                            text = if (isBinance) {
                                if (uiState.binanceMode == BinanceTransferMode.BINANCE_PAY)
                                    "مثال: 829401773 أو pay@binance"
                                else "مثال: 0x71C...B29"
                            } else {
                                "أدخل عنوان المحفظة على البلوكشاين..."
                            },
                            color = CryptoTextMuted,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("destination_address_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CryptoDarkCard,
                        unfocusedContainerColor = CryptoDarkCard,
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = CryptoBorder,
                        focusedTextColor = CryptoTextPrimary,
                        unfocusedTextColor = CryptoTextPrimary
                    ),
                    singleLine = true
                )

                // Quick address chips from saved
                if (uiState.savedAddresses.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(uiState.savedAddresses) { saved ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = CryptoDarkSurfaceVariant,
                                modifier = Modifier
                                    .border(1.dp, CryptoBorder, RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setDestinationAddress(saved.address) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = null,
                                        tint = accentColor,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = saved.label,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CryptoTextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Amount Input with percentage chips
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CryptoBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = CryptoDarkCard),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "المبلغ المراد سحبه:",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = CryptoTextPrimary
                        )
                        Text(
                            text = "المتاح: ${String.format(Locale.US, "%.6f", coinBalance)} ${currentCoin.symbol}",
                            style = MaterialTheme.typography.labelSmall,
                            color = accentColor
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.withdrawAmountText,
                        onValueChange = { viewModel.setWithdrawAmount(it) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        trailingIcon = {
                            Text(
                                text = currentCoin.symbol,
                                color = accentColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                        },
                        placeholder = {
                            Text("0.00", color = CryptoTextMuted)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("withdraw_amount_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CryptoDarkSurface,
                            unfocusedContainerColor = CryptoDarkSurface,
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = CryptoBorder,
                            focusedTextColor = CryptoTextPrimary,
                            unfocusedTextColor = CryptoTextPrimary
                        )
                    )

                    // USD estimate
                    val enteredAmount = uiState.withdrawAmountText.toDoubleOrNull() ?: 0.0
                    val usdEstimate = enteredAmount * currentCoin.currentPriceUsd
                    if (enteredAmount > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "≈ $${String.format(Locale.US, "%,.2f", usdEstimate)} USD",
                            style = MaterialTheme.typography.labelSmall,
                            color = CryptoTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Percentage chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (pct in listOf(25, 50, 75)) {
                            OutlinedButton(
                                onClick = { viewModel.fillPercentageAmount(pct) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("$pct%", color = CryptoTextSecondary, fontSize = 12.sp)
                            }
                        }
                        Button(
                            onClick = { viewModel.fillMaxAmount() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor.copy(alpha = 0.2f)),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("الكل MAX", color = accentColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Gas fee speed selector for Blockchain
        if (!isBinance) {
            item {
                Column {
                    Text(
                        text = "سرعة معالجة وتأكيد البلوكشاين (Gas Fee):",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = CryptoTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        GasSpeed.entries.forEach { speed ->
                            val isSelected = uiState.gasSpeed == speed
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) BlockchainEmerald.copy(alpha = 0.15f) else CryptoDarkCard,
                                modifier = Modifier
                                    .weight(1f)
                                    .border(
                                        1.dp,
                                        if (isSelected) BlockchainEmerald else CryptoBorder,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { viewModel.setGasSpeed(speed) }
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = speed.labelAr,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        color = if (isSelected) BlockchainEmerald else CryptoTextPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Submit Withdrawal Button
        item {
            Button(
                onClick = { viewModel.openConfirmWithdrawDialog() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_withdrawal_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    contentColor = if (isBinance) Color.Black else Color.Black
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isBinance) "تأكيد التحويل إلى Binance Pay" else "بث المعاملة إلى البلوكشاين",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
