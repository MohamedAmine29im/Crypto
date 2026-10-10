package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CryptoCurrency
import com.example.ui.MiningUiState
import com.example.ui.MiningViewModel
import com.example.ui.Screen
import com.example.ui.components.TransactionReceiptModal
import com.example.ui.components.WithdrawConfirmModal
import com.example.ui.screens.HardwareUpgradesScreen
import com.example.ui.screens.MarketCalculatorScreen
import com.example.ui.screens.MiningDashboardScreen
import com.example.ui.screens.TransactionHistoryScreen
import com.example.ui.screens.WithdrawTransferScreen
import com.example.ui.theme.BinanceYellow
import com.example.ui.theme.BlockchainEmerald
import com.example.ui.theme.CryptoBorder
import com.example.ui.theme.CryptoDarkBackground
import com.example.ui.theme.CryptoDarkCard
import com.example.ui.theme.CryptoDarkSurface
import com.example.ui.theme.CryptoDarkSurfaceVariant
import com.example.ui.theme.CryptoTextMuted
import com.example.ui.theme.CryptoTextPrimary
import com.example.ui.theme.CryptoTextSecondary
import com.example.ui.theme.MyApplicationTheme
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val viewModel: MiningViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    CryptoMiningApp(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoMiningApp(viewModel: MiningViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle snackbar messages
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    // Handle back button: if not on MINING screen, return to MINING screen
    if (uiState.currentScreen != Screen.MINING) {
        BackHandler {
            viewModel.setScreen(Screen.MINING)
        }
    }

    // Modal dialogs
    if (uiState.isConfirmDialogOpen) {
        val amount = uiState.withdrawAmountText.toDoubleOrNull() ?: 0.0
        val platform = if (uiState.withdrawTab == com.example.ui.WithdrawTab.BINANCE) "Binance" else "Blockchain"
        val isBinancePay = uiState.binanceMode == com.example.ui.BinanceTransferMode.BINANCE_PAY
        val fee = if (platform == "Binance" && isBinancePay) 0.0 else when (uiState.selectedCoin) {
            CryptoCurrency.BTC -> 0.00003 * uiState.gasSpeed.multiplier
            CryptoCurrency.ETH -> 0.0012 * uiState.gasSpeed.multiplier
            CryptoCurrency.SOL -> 0.005 * uiState.gasSpeed.multiplier
            CryptoCurrency.USDT -> 1.0 * uiState.gasSpeed.multiplier
            CryptoCurrency.KAS -> 1.0 * uiState.gasSpeed.multiplier
        }

        WithdrawConfirmModal(
            platform = platform,
            coin = uiState.selectedCoin,
            amount = amount,
            recipient = uiState.destinationAddressText,
            network = uiState.selectedNetwork,
            fee = fee,
            isProcessing = uiState.isProcessing,
            onConfirm = { viewModel.confirmWithdrawal() },
            onDismiss = { viewModel.dismissConfirmDialog() }
        )
    }

    if (uiState.isReceiptDialogOpen && uiState.lastCompletedTx != null) {
        TransactionReceiptModal(
            transaction = uiState.lastCompletedTx!!,
            onDismiss = { viewModel.dismissReceiptDialog() }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = CryptoDarkBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.crypto_mine_icon_1791643225731),
                            contentDescription = "App Icon",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, BinanceYellow.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        )
                        Column {
                            Text(
                                text = "CryptoMine Pro",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                color = BinanceYellow
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (uiState.profile.isMining) BlockchainEmerald else CryptoTextMuted)
                                )
                                Text(
                                    text = if (uiState.profile.isMining) "متصل بالسيرفر (Pool Online)" else "التعدين متوقف",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CryptoTextSecondary
                                )
                            }
                        }
                    }
                },
                actions = {
                    // Quick balance display
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = CryptoDarkCard,
                        modifier = Modifier
                            .border(1.dp, CryptoBorder, RoundedCornerShape(12.dp))
                            .padding(end = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CurrencyBitcoin,
                                contentDescription = null,
                                tint = BinanceYellow,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${String.format(Locale.US, "%.4f", uiState.profile.btcBalance)} BTC",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = CryptoTextPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CryptoDarkBackground,
                    titleContentColor = CryptoTextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = CryptoDarkSurface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .border(1.dp, CryptoBorder, RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
            ) {
                NavigationBarItem(
                    selected = uiState.currentScreen == Screen.MINING,
                    onClick = { viewModel.setScreen(Screen.MINING) },
                    icon = { Icon(Icons.Default.Speed, contentDescription = "التعدين") },
                    label = { Text("التعدين", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BinanceYellow,
                        selectedTextColor = BinanceYellow,
                        unselectedIconColor = CryptoTextSecondary,
                        unselectedTextColor = CryptoTextSecondary,
                        indicatorColor = BinanceYellow.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_mining")
                )

                NavigationBarItem(
                    selected = uiState.currentScreen == Screen.TRANSFER,
                    onClick = { viewModel.setScreen(Screen.TRANSFER) },
                    icon = { Icon(Icons.Default.Send, contentDescription = "التحويل والسحب") },
                    label = { Text("التحويل", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BinanceYellow,
                        selectedTextColor = BinanceYellow,
                        unselectedIconColor = CryptoTextSecondary,
                        unselectedTextColor = CryptoTextSecondary,
                        indicatorColor = BinanceYellow.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_transfer")
                )

                NavigationBarItem(
                    selected = uiState.currentScreen == Screen.HARDWARE,
                    onClick = { viewModel.setScreen(Screen.HARDWARE) },
                    icon = { Icon(Icons.Default.Memory, contentDescription = "العتاد") },
                    label = { Text("العتاد", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BinanceYellow,
                        selectedTextColor = BinanceYellow,
                        unselectedIconColor = CryptoTextSecondary,
                        unselectedTextColor = CryptoTextSecondary,
                        indicatorColor = BinanceYellow.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_hardware")
                )

                NavigationBarItem(
                    selected = uiState.currentScreen == Screen.TRANSACTIONS,
                    onClick = { viewModel.setScreen(Screen.TRANSACTIONS) },
                    icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "السجل") },
                    label = { Text("السجل", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BinanceYellow,
                        selectedTextColor = BinanceYellow,
                        unselectedIconColor = CryptoTextSecondary,
                        unselectedTextColor = CryptoTextSecondary,
                        indicatorColor = BinanceYellow.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_history")
                )

                NavigationBarItem(
                    selected = uiState.currentScreen == Screen.MARKET,
                    onClick = { viewModel.setScreen(Screen.MARKET) },
                    icon = { Icon(Icons.Default.TrendingUp, contentDescription = "السوق") },
                    label = { Text("السوق", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BinanceYellow,
                        selectedTextColor = BinanceYellow,
                        unselectedIconColor = CryptoTextSecondary,
                        unselectedTextColor = CryptoTextSecondary,
                        indicatorColor = BinanceYellow.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_market")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = uiState.currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { targetScreen ->
                when (targetScreen) {
                    Screen.MINING -> MiningDashboardScreen(uiState = uiState, viewModel = viewModel)
                    Screen.TRANSFER -> WithdrawTransferScreen(uiState = uiState, viewModel = viewModel)
                    Screen.HARDWARE -> HardwareUpgradesScreen(uiState = uiState, viewModel = viewModel)
                    Screen.TRANSACTIONS -> TransactionHistoryScreen(uiState = uiState, viewModel = viewModel)
                    Screen.MARKET -> MarketCalculatorScreen(uiState = uiState, viewModel = viewModel)
                }
            }
        }
    }
}
