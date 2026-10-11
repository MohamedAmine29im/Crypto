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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.model.Screen
import com.example.ui.BenchmarkViewModel
import com.example.ui.screens.AddressValidatorScreen
import com.example.ui.screens.ApkInstallGuideScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EducationalLabScreen
import com.example.ui.screens.HardwareTelemetryScreen
import com.example.ui.theme.BitcoinGold
import com.example.ui.theme.BlockchainEmerald
import com.example.ui.theme.CryptoBorder
import com.example.ui.theme.CryptoDarkBackground
import com.example.ui.theme.CryptoDarkCard
import com.example.ui.theme.CryptoDarkSurface
import com.example.ui.theme.CryptoRed
import com.example.ui.theme.CryptoTextMuted
import com.example.ui.theme.CryptoTextPrimary
import com.example.ui.theme.CryptoTextSecondary
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: BenchmarkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    BitcoinMiningLabApp(viewModel = viewModel)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Stop benchmark when leaving foreground to protect user's battery and CPU
        viewModel.stopBenchmark()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BitcoinMiningLabApp(viewModel: BenchmarkViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.alertMessage) {
        uiState.alertMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissAlert()
        }
    }

    // Handle Android system back button
    BackHandler(enabled = uiState.currentScreen != Screen.DASHBOARD) {
        viewModel.setScreen(Screen.DASHBOARD)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = CryptoDarkBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.btc_lab_icon_1791708221833),
                            contentDescription = "Bitcoin Mining Lab Logo",
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Column {
                            Text(
                                text = "Bitcoin Mining Lab",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = CryptoTextPrimary
                            )
                            Text(
                                text = "Samsung Galaxy S21+ 5G",
                                style = MaterialTheme.typography.labelSmall,
                                color = CryptoTextSecondary
                            )
                        }
                    }
                },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (uiState.engineState.isRunning) BlockchainEmerald.copy(alpha = 0.2f) else CryptoDarkCard,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .border(
                                1.dp,
                                if (uiState.engineState.isRunning) BlockchainEmerald else CryptoBorder,
                                RoundedCornerShape(12.dp)
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (uiState.engineState.isRunning) BlockchainEmerald else CryptoTextMuted)
                            )
                            Text(
                                text = if (uiState.engineState.isRunning) "نشط" else "متوقف",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (uiState.engineState.isRunning) BlockchainEmerald else CryptoTextMuted
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CryptoDarkSurface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = CryptoDarkSurface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .navigationBarsPadding()
                    .border(1.dp, CryptoBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                NavigationBarItem(
                    selected = uiState.currentScreen == Screen.DASHBOARD,
                    onClick = { viewModel.setScreen(Screen.DASHBOARD) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "المختبر",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "المختبر",
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BitcoinGold,
                        selectedTextColor = BitcoinGold,
                        indicatorColor = BitcoinGold.copy(alpha = 0.15f),
                        unselectedIconColor = CryptoTextMuted,
                        unselectedTextColor = CryptoTextMuted
                    ),
                    modifier = Modifier.testTag("nav_item_dashboard")
                )

                NavigationBarItem(
                    selected = uiState.currentScreen == Screen.HARDWARE,
                    onClick = { viewModel.setScreen(Screen.HARDWARE) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Thermostat,
                            contentDescription = "العتاد والحرارة",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "العتاد",
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BlockchainEmerald,
                        selectedTextColor = BlockchainEmerald,
                        indicatorColor = BlockchainEmerald.copy(alpha = 0.15f),
                        unselectedIconColor = CryptoTextMuted,
                        unselectedTextColor = CryptoTextMuted
                    ),
                    modifier = Modifier.testTag("nav_item_hardware")
                )

                NavigationBarItem(
                    selected = uiState.currentScreen == Screen.VALIDATOR,
                    onClick = { viewModel.setScreen(Screen.VALIDATOR) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "فاحص العناوين",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "العناوين",
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BitcoinGold,
                        selectedTextColor = BitcoinGold,
                        indicatorColor = BitcoinGold.copy(alpha = 0.15f),
                        unselectedIconColor = CryptoTextMuted,
                        unselectedTextColor = CryptoTextMuted
                    ),
                    modifier = Modifier.testTag("nav_item_validator")
                )

                NavigationBarItem(
                    selected = uiState.currentScreen == Screen.EDUCATION,
                    onClick = { viewModel.setScreen(Screen.EDUCATION) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "دليل التعدين",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "الدليل",
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BitcoinGold,
                        selectedTextColor = BitcoinGold,
                        indicatorColor = BitcoinGold.copy(alpha = 0.15f),
                        unselectedIconColor = CryptoTextMuted,
                        unselectedTextColor = CryptoTextMuted
                    ),
                    modifier = Modifier.testTag("nav_item_education")
                )

                NavigationBarItem(
                    selected = uiState.currentScreen == Screen.INSTALL_GUIDE,
                    onClick = { viewModel.setScreen(Screen.INSTALL_GUIDE) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "تثبيت APK",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "تثبيت APK",
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BlockchainEmerald,
                        selectedTextColor = BlockchainEmerald,
                        indicatorColor = BlockchainEmerald.copy(alpha = 0.15f),
                        unselectedIconColor = CryptoTextMuted,
                        unselectedTextColor = CryptoTextMuted
                    ),
                    modifier = Modifier.testTag("nav_item_install_guide")
                )
            }
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = uiState.currentScreen,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "screen_transition",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { targetScreen ->
            when (targetScreen) {
                Screen.DASHBOARD -> DashboardScreen(
                    uiState = uiState,
                    viewModel = viewModel
                )
                Screen.HARDWARE -> HardwareTelemetryScreen(
                    uiState = uiState,
                    viewModel = viewModel
                )
                Screen.VALIDATOR -> AddressValidatorScreen(
                    uiState = uiState,
                    viewModel = viewModel
                )
                Screen.EDUCATION -> EducationalLabScreen()
                Screen.INSTALL_GUIDE -> ApkInstallGuideScreen()
            }
        }
    }
}
