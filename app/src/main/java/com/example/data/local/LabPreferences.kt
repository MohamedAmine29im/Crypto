package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.DifficultyLevel

class LabPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("btc_lab_prefs", Context.MODE_PRIVATE)

    var difficultyLevel: DifficultyLevel
        get() = DifficultyLevel.fromId(prefs.getInt(KEY_DIFFICULTY, DifficultyLevel.EASY.id))
        set(value) = prefs.edit().putInt(KEY_DIFFICULTY, value.id).apply()

    var activeThreads: Int
        get() = prefs.getInt(KEY_THREADS, 2)
        set(value) = prefs.edit().putInt(KEY_THREADS, value).apply()

    var maxTempThresholdC: Double
        get() = prefs.getFloat(KEY_MAX_TEMP, 42.0f).toDouble()
        set(value) = prefs.edit().putFloat(KEY_MAX_TEMP, value.toFloat()).apply()

    var throttleDelayMs: Long
        get() = prefs.getLong(KEY_THROTTLE_DELAY, 5L)
        set(value) = prefs.edit().putLong(KEY_THROTTLE_DELAY, value).apply()

    var savedDisplayAddress: String
        get() = prefs.getString(KEY_SAVED_ADDRESS, "bc1qxy2kgdygjrsqtzq2n0yrf2493p83kkfjhx0wlh") ?: ""
        set(value) = prefs.edit().putString(KEY_SAVED_ADDRESS, value).apply()

    var hasSeenDisclaimer: Boolean
        get() = prefs.getBoolean(KEY_HAS_SEEN_DISCLAIMER, false)
        set(value) = prefs.edit().putBoolean(KEY_HAS_SEEN_DISCLAIMER, value).apply()

    var vibrateOnSolved: Boolean
        get() = prefs.getBoolean(KEY_VIBRATE_ON_SOLVED, true)
        set(value) = prefs.edit().putBoolean(KEY_VIBRATE_ON_SOLVED, value).apply()

    companion object {
        private const val KEY_DIFFICULTY = "difficulty_level"
        private const val KEY_THREADS = "active_threads"
        private const val KEY_MAX_TEMP = "max_temp_threshold"
        private const val KEY_THROTTLE_DELAY = "throttle_delay"
        private const val KEY_SAVED_ADDRESS = "saved_display_address"
        private const val KEY_HAS_SEEN_DISCLAIMER = "has_seen_disclaimer"
        private const val KEY_VIBRATE_ON_SOLVED = "vibrate_on_solved"
    }
}
