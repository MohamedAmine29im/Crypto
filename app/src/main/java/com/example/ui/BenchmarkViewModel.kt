package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.benchmark.BenchmarkEngineState
import com.example.data.benchmark.Sha256BenchmarkEngine
import com.example.data.local.LabPreferences
import com.example.data.model.BitcoinAddressInfo
import com.example.data.model.DeviceHardwareStatus
import com.example.data.model.DifficultyLevel
import com.example.data.model.Screen
import com.example.util.BitcoinAddressValidator
import com.example.util.DeviceHardwareMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class BenchmarkUiState(
    val engineState: BenchmarkEngineState = BenchmarkEngineState(),
    val hardwareStatus: DeviceHardwareStatus = DeviceHardwareStatus(),
    val currentScreen: Screen = Screen.DASHBOARD,
    val addressInputText: String = "",
    val addressInfo: BitcoinAddressInfo = BitcoinAddressInfo(),
    val isDisclaimerModalVisible: Boolean = false,
    val alertMessage: String? = null
)

class BenchmarkViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = LabPreferences(application)
    private val engine = Sha256BenchmarkEngine(viewModelScope)

    private val hardwareMonitor = DeviceHardwareMonitor(application) {
        // Overheat auto-shutdown callback
        engine.stopBenchmark()
        _alertMessage.value = "⚠️ تم إيقاف الاختبار تلقائياً: حرارة الجهاز تجاوزت الحد الآمن المحدد!"
    }

    private val _currentScreen = MutableStateFlow(Screen.DASHBOARD)
    private val _addressInputText = MutableStateFlow(preferences.savedDisplayAddress)
    private val _addressInfo = MutableStateFlow(BitcoinAddressValidator.validateAddress(preferences.savedDisplayAddress))
    private val _isDisclaimerModalVisible = MutableStateFlow(!preferences.hasSeenDisclaimer)
    private val _alertMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<BenchmarkUiState>

    init {
        hardwareMonitor.startMonitoring()
        hardwareMonitor.updateSettings(
            activeThreads = preferences.activeThreads,
            maxTempC = preferences.maxTempThresholdC,
            throttleDelayMs = preferences.throttleDelayMs
        )

        uiState = combine(
            engine.engineState,
            hardwareMonitor.hardwareStatus,
            _currentScreen,
            _addressInputText,
            _addressInfo,
            _isDisclaimerModalVisible,
            _alertMessage
        ) { args ->
            BenchmarkUiState(
                engineState = args[0] as BenchmarkEngineState,
                hardwareStatus = args[1] as DeviceHardwareStatus,
                currentScreen = args[2] as Screen,
                addressInputText = args[3] as String,
                addressInfo = args[4] as BitcoinAddressInfo,
                isDisclaimerModalVisible = args[5] as Boolean,
                alertMessage = args[6] as? String
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            BenchmarkUiState()
        )
    }

    fun startBenchmark() {
        val currentStatus = hardwareMonitor.hardwareStatus.value
        if (currentStatus.isOverheated) {
            _alertMessage.value = "لا يمكن بدء الاختبار: حرارة الهاتف مرتفعة حالياً (${currentStatus.temperatureC}°C). اترك الهاتف ليبرد قليلاً."
            return
        }

        engine.startBenchmark(
            difficulty = preferences.difficultyLevel,
            threads = preferences.activeThreads,
            throttleDelayMs = preferences.throttleDelayMs
        )
    }

    fun stopBenchmark() {
        engine.stopBenchmark()
    }

    fun resetStats() {
        engine.resetStats()
    }

    fun setDifficulty(difficulty: DifficultyLevel) {
        preferences.difficultyLevel = difficulty
        if (engine.engineState.value.isRunning) {
            engine.stopBenchmark()
            engine.startBenchmark(
                difficulty = difficulty,
                threads = preferences.activeThreads,
                throttleDelayMs = preferences.throttleDelayMs
            )
        } else {
            // Update engine state display
            engine.stopBenchmark()
        }
    }

    fun setThreads(threads: Int) {
        val clamped = threads.coerceIn(1, Runtime.getRuntime().availableProcessors().coerceAtLeast(1))
        preferences.activeThreads = clamped
        hardwareMonitor.updateSettings(
            activeThreads = clamped,
            maxTempC = preferences.maxTempThresholdC,
            throttleDelayMs = preferences.throttleDelayMs
        )
        if (engine.engineState.value.isRunning) {
            engine.stopBenchmark()
            engine.startBenchmark(
                difficulty = preferences.difficultyLevel,
                threads = clamped,
                throttleDelayMs = preferences.throttleDelayMs
            )
        }
    }

    fun setMaxTempThreshold(maxTemp: Double) {
        preferences.maxTempThresholdC = maxTemp
        hardwareMonitor.updateSettings(
            activeThreads = preferences.activeThreads,
            maxTempC = maxTemp,
            throttleDelayMs = preferences.throttleDelayMs
        )
    }

    fun setThrottleDelay(delayMs: Long) {
        preferences.throttleDelayMs = delayMs
        hardwareMonitor.updateSettings(
            activeThreads = preferences.activeThreads,
            maxTempC = preferences.maxTempThresholdC,
            throttleDelayMs = delayMs
        )
        if (engine.engineState.value.isRunning) {
            engine.stopBenchmark()
            engine.startBenchmark(
                difficulty = preferences.difficultyLevel,
                threads = preferences.activeThreads,
                throttleDelayMs = delayMs
            )
        }
    }

    fun onAddressInputChanged(newText: String) {
        _addressInputText.value = newText
        preferences.savedDisplayAddress = newText
        _addressInfo.value = BitcoinAddressValidator.validateAddress(newText)
    }

    fun setScreen(screen: Screen) {
        _currentScreen.value = screen
    }

    fun dismissDisclaimer() {
        preferences.hasSeenDisclaimer = true
        _isDisclaimerModalVisible.value = false
    }

    fun showDisclaimer() {
        _isDisclaimerModalVisible.value = true
    }

    fun dismissAlert() {
        _alertMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        engine.stopBenchmark()
        hardwareMonitor.stopMonitoring()
    }
}
