package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CryptoColorScheme = darkColorScheme(
    primary = BinanceYellow,
    onPrimary = CryptoDarkBackground,
    primaryContainer = CryptoDarkSurfaceVariant,
    onPrimaryContainer = BinanceYellow,
    secondary = BlockchainEmerald,
    onSecondary = CryptoDarkBackground,
    secondaryContainer = CryptoDarkCard,
    onSecondaryContainer = BlockchainEmerald,
    tertiary = BitcoinGold,
    onTertiary = CryptoDarkBackground,
    background = CryptoDarkBackground,
    onBackground = CryptoTextPrimary,
    surface = CryptoDarkSurface,
    onSurface = CryptoTextPrimary,
    surfaceVariant = CryptoDarkSurfaceVariant,
    onSurfaceVariant = CryptoTextSecondary,
    outline = CryptoBorder,
    error = CryptoRed,
    onError = CryptoTextPrimary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek crypto dark theme
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = CryptoDarkBackground.toArgb()
                window.navigationBarColor = CryptoDarkBackground.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = CryptoColorScheme,
        typography = Typography,
        content = content
    )
}
