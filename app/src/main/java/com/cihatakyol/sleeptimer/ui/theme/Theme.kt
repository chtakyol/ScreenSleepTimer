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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Material Design 3 theme implementation for SleepTimer app.
 *
 * ## Theme Structure
 *
 * This theme follows Material Design 3 guidelines with two color schemes:
 * - **Light Theme**: For bright environments with dark text on light backgrounds
 * - **Dark Theme**: For low-light environments with light text on dark backgrounds
 *
 * On Android 12+, dynamic colors are supported, allowing the theme to adapt to
 * the user's wallpaper colors for a personalized experience.
 *
 * ## Color Roles
 *
 * Material Design 3 defines several color roles. Here's when to use each:
 *
 * ### Primary Colors
 * - **primary**: Main brand color for key actions (e.g., FABs, primary buttons)
 * - **onPrimary**: Text/icons on primary color backgrounds
 * - **primaryContainer**: Tonal variant of primary for less emphasis
 * - **onPrimaryContainer**: Text/icons on primary container backgrounds
 *
 * ### Secondary Colors
 * - **secondary**: Complementary color for secondary actions
 * - **onSecondary**: Text/icons on secondary color backgrounds
 * - **secondaryContainer**: Tonal variant of secondary
 * - **onSecondaryContainer**: Text/icons on secondary container backgrounds
 *
 * ### Tertiary Colors
 * - **tertiary**: Additional accent color for variety
 * - **onTertiary**: Text/icons on tertiary color backgrounds
 * - **tertiaryContainer**: Tonal variant of tertiary
 * - **onTertiaryContainer**: Text/icons on tertiary container backgrounds
 *
 * ### Surface & Background Colors
 * - **background**: Main app background (behind all content)
 * - **onBackground**: Text/icons on background
 * - **surface**: Background for elevated components (cards, dialogs, sheets)
 * - **onSurface**: Text/icons on surface (most common for body text)
 * - **surfaceVariant**: Subtle surface differentiation (input fields, dividers)
 * - **onSurfaceVariant**: Lower emphasis text/icons on surface variants
 *
 * **Key difference**: Use `background` for main screens, `surface` for cards/dialogs.
 *
 * ### Error Colors
 * - **error**: Error states, destructive actions
 * - **onError**: Text/icons on error color backgrounds
 * - **errorContainer**: Background for error messages
 * - **onErrorContainer**: Text in error messages
 *
 * ## Usage Examples
 *
 * ```kotlin
 * // Text on surface (most common)
 * Text(
 *     text = "Hello",
 *     color = MaterialTheme.colorScheme.onSurface
 * )
 *
 * // Text on primary button
 * Button(onClick = {}) {
 *     Text("Click me") // onPrimary color applied automatically
 * }
 *
 * // Card with custom background
 * Card(
 *     colors = CardDefaults.cardColors(
 *         containerColor = MaterialTheme.colorScheme.surface
 *     )
 * ) {
 *     Text(
 *         text = "Card content",
 *         color = MaterialTheme.colorScheme.onSurface
 *     )
 * }
 *
 * // Gradient using theme colors
 * Box(
 *     modifier = Modifier.background(
 *         brush = Brush.verticalGradient(
 *             colors = listOf(
 *                 MaterialTheme.colorScheme.background,
 *                 MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
 *             )
 *         )
 *     )
 * )
 * ```
 *
 * ## Dynamic Colors (Android 12+)
 *
 * When `dynamicColor = true` and running on Android 12+, the theme automatically
 * adapts to the user's wallpaper colors using Material You dynamic theming.
 * On older versions, the custom color scheme is used.
 *
 * See docs/THEME_USAGE_GUIDE.md for comprehensive usage guidelines.
 */

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer
)

/**
 * SleepTimer app theme composable.
 *
 * Wraps app content with Material Design 3 theming, handling both light/dark modes
 * and dynamic color support on Android 12+.
 *
 * @param darkTheme Whether to use dark theme. Defaults to system setting via [isSystemInDarkTheme].
 * @param dynamicColor Whether to enable Material You dynamic colors on Android 12+.
 *                     When true, colors adapt to user's wallpaper. Defaults to true.
 *                     Falls back to custom color scheme on Android 11 and below.
 * @param content The composable content to apply theming to.
 *
 * @sample
 * ```kotlin
 * SleepTimerTheme {
 *     Surface {
 *         TimerScreen()
 *     }
 * }
 *
 * // Force light theme
 * SleepTimerTheme(darkTheme = false) {
 *     OnboardingScreen()
 * }
 *
 * // Disable dynamic colors
 * SleepTimerTheme(dynamicColor = false) {
 *     SettingsScreen()
 * }
 * ```
 */
@Composable
fun SleepTimerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true, // Enabled by default for consistent dark theme
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
