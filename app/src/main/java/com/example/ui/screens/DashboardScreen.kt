package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.Screen
import com.example.ui.BenchmarkUiState
import com.example.ui.BenchmarkViewModel
import com.example.ui.components.BenchmarkControlsRow
import com.example.ui.components.CandidateBlockCard
import com.example.ui.components.DifficultySelectorSection
import com.example.ui.components.EducationalDisclaimerDialog
import com.example.ui.components.EducationalNoticeCard
import com.example.ui.components.HardwareQuickStatusBar
import com.example.ui.components.HashrateGaugeCard
import com.example.ui.components.HeroBannerCard
import com.example.ui.theme.CryptoTextPrimary
import com.example.ui.theme.CryptoTextSecondary

@Composable
fun DashboardScreen(
    uiState: BenchmarkUiState,
    viewModel: BenchmarkViewModel,
    modifier: Modifier = Modifier
) {
    if (uiState.isDisclaimerModalVisible) {
        EducationalDisclaimerDialog(
            onDismiss = { viewModel.dismissDisclaimer() }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero visual banner
        item {
            HeroBannerCard()
        }

        // Educational notice
        item {
            EducationalNoticeCard(
                onLearnMoreClick = { viewModel.setScreen(Screen.EDUCATION) }
            )
        }

        // Quick hardware & temperature status bar
        item {
            HardwareQuickStatusBar(
                hardwareStatus = uiState.hardwareStatus,
                onOpenHardwareScreen = { viewModel.setScreen(Screen.HARDWARE) }
            )
        }

        // Live Hashrate gauge & candidate hash preview
        item {
            HashrateGaugeCard(
                engineState = uiState.engineState
            )
        }

        // Primary Controls (START / STOP / RESET)
        item {
            BenchmarkControlsRow(
                isRunning = uiState.engineState.isRunning,
                onStartClick = { viewModel.startBenchmark() },
                onStopClick = { viewModel.stopBenchmark() },
                onResetClick = { viewModel.resetStats() }
            )
        }

        // Difficulty selector
        item {
            DifficultySelectorSection(
                selectedDifficulty = uiState.engineState.selectedDifficulty,
                onSelectDifficulty = { diff -> viewModel.setDifficulty(diff) }
            )
        }

        // Solved candidate blocks section
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "الكتل التجريبية المحلولة (${uiState.engineState.solvedBlocks.size}):",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = CryptoTextPrimary
                )
                Text(
                    text = "تجزئات حققت بادئة الصعوبة المطلوبة محلياً (${uiState.engineState.selectedDifficulty.hexPrefix})",
                    style = MaterialTheme.typography.bodySmall,
                    color = CryptoTextSecondary
                )
            }
        }

        if (uiState.engineState.solvedBlocks.isEmpty()) {
            item {
                Text(
                    text = "اضغط على 'بدء الاختبار' للبحث عن كتل تجريبية تطابق صعوبة الهاش.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CryptoTextSecondary,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        } else {
            items(uiState.engineState.solvedBlocks) { block ->
                CandidateBlockCard(block = block)
            }
        }
    }
}
