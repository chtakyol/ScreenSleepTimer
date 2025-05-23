package com.cihatakyol.sleeptimer.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Dark theme colors
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6B8AFE),          // Soft blue
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1A237E),  // Deep blue
    onPrimaryContainer = Color(0xFFE8EAF6),
    secondary = Color(0xFF03DAC6),        // Teal accent
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF004D40),
    onSecondaryContainer = Color(0xFFB2DFDB),
    tertiary = Color(0xFFB39DDB),         // Soft purple
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF4527A0),
    onTertiaryContainer = Color(0xFFEDE7F6),
    background = Color(0xFF121212),       // Dark background
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1E1E1E),          // Slightly lighter dark
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF2D2D2D),   // Dark surface variant
    onSurfaceVariant = Color(0xFFBDBDBD),
    error = Color(0xFFCF6679),            // Soft red
    onError = Color.Black,
    errorContainer = Color(0xFFB00020),
    onErrorContainer = Color(0xFFFFDAD6)
)

// Light theme colors (keeping for system preference)
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1A237E),          // Deep blue
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8EAF6),
    onPrimaryContainer = Color(0xFF1A237E),
    secondary = Color(0xFF004D40),        // Deep teal
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB2DFDB),
    onSecondaryContainer = Color(0xFF004D40),
    tertiary = Color(0xFF4527A0),         // Deep purple
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFEDE7F6),
    onTertiaryContainer = Color(0xFF4527A0),
    background = Color(0xFFF5F5F5),
    onBackground = Color(0xFF1C1B1F),
    surface = Color.White,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF49454F),
    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFFB00020)
)

@Composable
fun SleepTimerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled by default for consistent dark theme
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}