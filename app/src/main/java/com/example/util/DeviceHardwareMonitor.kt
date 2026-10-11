package com.example.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.example.data.model.DeviceHardwareStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DeviceHardwareMonitor(
    private val context: Context,
    private val onOverheatCutoff: () -> Unit
) {
    private val _hardwareStatus = MutableStateFlow(
        DeviceHardwareStatus(
            availableCores = Runtime.getRuntime().availableProcessors().coerceAtLeast(1)
        )
    )
    val hardwareStatus = _hardwareStatus.asStateFlow()

    private var isReceiverRegistered = false

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_BATTERY_CHANGED) {
                updateBatteryAndThermal(intent)
            }
        }
    }

    fun startMonitoring() {
        if (!isReceiverRegistered) {
            val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val stickyIntent = context.registerReceiver(batteryReceiver, filter)
            isReceiverRegistered = true
            stickyIntent?.let { updateBatteryAndThermal(it) }
        }
    }

    fun stopMonitoring() {
        if (isReceiverRegistered) {
            try {
                context.unregisterReceiver(batteryReceiver)
            } catch (e: Exception) {
                // Ignore if already unregistered
            }
            isReceiverRegistered = false
        }
    }

    fun updateSettings(activeThreads: Int, maxTempC: Double, throttleDelayMs: Long) {
        _hardwareStatus.value = _hardwareStatus.value.copy(
            activeThreads = activeThreads,
            maxTempThresholdC = maxTempC,
            throttleDelayMs = throttleDelayMs
        )
    }

    private fun updateBatteryAndThermal(intent: Intent) {
        // Battery Temperature is in tenths of a degree Celsius (e.g. 352 -> 35.2°C)
        val rawTemp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 320)
        val tempCelsius = (rawTemp / 10.0).coerceIn(15.0, 85.0)

        // Battery percentage
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPct = if (level >= 0 && scale > 0) {
            ((level / scale.toFloat()) * 100).toInt()
        } else {
            85
        }

        // Charging status
        val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
        val isCharging = plugged != 0
        val chargingSource = when (plugged) {
            BatteryManager.BATTERY_PLUGGED_AC -> "شاحن سلكي سريع (AC Charger)"
            BatteryManager.BATTERY_PLUGGED_USB -> "منفذ USB (كمبيوتر / بنك طاقة)"
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> "شاحن لاسلكي سريع (Wireless)"
            else -> "بطارية الهاتف (غير موصول بالشاحن)"
        }

        val maxThreshold = _hardwareStatus.value.maxTempThresholdC
        val isOverheated = tempCelsius >= maxThreshold

        _hardwareStatus.value = _hardwareStatus.value.copy(
            temperatureC = tempCelsius,
            batteryPercent = batteryPct,
            isCharging = isCharging,
            chargingSource = chargingSource,
            isOverheated = isOverheated
        )

        if (isOverheated) {
            onOverheatCutoff()
        }
    }
}
