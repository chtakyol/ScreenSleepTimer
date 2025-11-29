package com.ToolCompany.screentimer.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Color palette for SleepTimer app following Material Design 3 guidelines.
 *
 * This file defines all theme colors for both dark and light modes. Always use
 * MaterialTheme.colorScheme colors instead of referencing these values directly.
 *
 * Example usage:
 * ```
 * Text(
 *     text = "Hello",
 *     color = MaterialTheme.colorScheme.onSurface // Good
 *     // color = DarkOnSurface // Bad - don't use directly
 * )
 * ```
 *
 * See docs/THEME_USAGE_GUIDE.md for detailed usage guidelines.
 */

// ============================================================================
// Dark Theme Colors
// ============================================================================

/** Primary brand color for dark theme - Soft blue used for main actions and emphasis.
 * Use for: Primary buttons, FABs, prominent interactive elements.
 * Access via: MaterialTheme.colorScheme.primary */
val DarkPrimary = Color(0xFF6B8AFE)

/** Text/icon color that appears on primary color backgrounds in dark theme.
 * Use for: Text on primary buttons, icons on primary surfaces.
 * Access via: MaterialTheme.colorScheme.onPrimary */
val DarkOnPrimary = Color.White

/** Container color for primary elements in dark theme - Deep blue for less emphasis than primary.
 * Use for: Tonal primary buttons, chips, highlighted containers.
 * Access via: MaterialTheme.colorScheme.primaryContainer */
val DarkPrimaryContainer = Color(0xFF1A237E)

/** Text/icon color for primary containers in dark theme.
 * Use for: Text on primaryContainer backgrounds.
 * Access via: MaterialTheme.colorScheme.onPrimaryContainer */
val DarkOnPrimaryContainer = Color(0xFFE8EAF6)

/** Secondary brand color for dark theme - Teal accent for secondary actions.
 * Use for: Secondary buttons, complementary UI elements, alternative highlights.
 * Access via: MaterialTheme.colorScheme.secondary */
val DarkSecondary = Color(0xFF03DAC6)

/** Text/icon color on secondary color backgrounds in dark theme.
 * Use for: Text on secondary buttons, icons on secondary surfaces.
 * Access via: MaterialTheme.colorScheme.onSecondary */
val DarkOnSecondary = Color.Black

/** Container color for secondary elements in dark theme.
 * Use for: Tonal secondary buttons, complementary containers.
 * Access via: MaterialTheme.colorScheme.secondaryContainer */
val DarkSecondaryContainer = Color(0xFF004D40)

/** Text/icon color for secondary containers in dark theme.
 * Use for: Text on secondaryContainer backgrounds.
 * Access via: MaterialTheme.colorScheme.onSecondaryContainer */
val DarkOnSecondaryContainer = Color(0xFFB2DFDB)

/** Tertiary brand color for dark theme - Soft purple for tertiary actions.
 * Use for: Tertiary buttons, additional accent elements, variety in UI.
 * Access via: MaterialTheme.colorScheme.tertiary */
val DarkTertiary = Color(0xFFB39DDB)

/** Text/icon color on tertiary color backgrounds in dark theme.
 * Use for: Text on tertiary buttons, icons on tertiary surfaces.
 * Access via: MaterialTheme.colorScheme.onTertiary */
val DarkOnTertiary = Color.Black

/** Container color for tertiary elements in dark theme.
 * Use for: Tonal tertiary buttons, additional containers.
 * Access via: MaterialTheme.colorScheme.tertiaryContainer */
val DarkTertiaryContainer = Color(0xFF4527A0)

/** Text/icon color for tertiary containers in dark theme.
 * Use for: Text on tertiaryContainer backgrounds.
 * Access via: MaterialTheme.colorScheme.onTertiaryContainer */
val DarkOnTertiaryContainer = Color(0xFFEDE7F6)

/** Main background color for dark theme - True dark for OLED displays and battery savings.
 * Use for: Main app background, screen backgrounds.
 * Access via: MaterialTheme.colorScheme.background */
val DarkBackground = Color(0xFF121212)

/** Text/icon color that appears on background in dark theme.
 * Use for: Body text, icons on background.
 * Access via: MaterialTheme.colorScheme.onBackground */
val DarkOnBackground = Color(0xFFE0E0E0)

/** Surface color for dark theme - Slightly lighter than background for elevation.
 * Use for: Cards, dialogs, bottom sheets, top app bars.
 * Access via: MaterialTheme.colorScheme.surface */
val DarkSurface = Color(0xFF1E1E1E)

/** Text/icon color that appears on surfaces in dark theme.
 * Use for: Text on cards, icons on dialogs, most body text.
 * Access via: MaterialTheme.colorScheme.onSurface */
val DarkOnSurface = Color(0xFFE0E0E0)

/** Variant surface color for dark theme - Used for subtle differentiation.
 * Use for: Subtle backgrounds, input field backgrounds, dividers.
 * Access via: MaterialTheme.colorScheme.surfaceVariant */
val DarkSurfaceVariant = Color(0xFF2D2D2D)

/** Text/icon color for surface variants in dark theme - Lower emphasis.
 * Use for: Secondary text, disabled text, subtle icons.
 * Access via: MaterialTheme.colorScheme.onSurfaceVariant */
val DarkOnSurfaceVariant = Color(0xFFBDBDBD)

/** Error color for dark theme - Soft red for error states.
 * Use for: Error messages, error icons, destructive actions.
 * Access via: MaterialTheme.colorScheme.error */
val DarkError = Color(0xFFCF6679)

/** Text/icon color on error color backgrounds in dark theme.
 * Use for: Text on error buttons, icons on error surfaces.
 * Access via: MaterialTheme.colorScheme.onError */
val DarkOnError = Color.Black

/** Container color for error elements in dark theme.
 * Use for: Error message backgrounds, alert containers.
 * Access via: MaterialTheme.colorScheme.errorContainer */
val DarkErrorContainer = Color(0xFFB00020)

/** Text/icon color for error containers in dark theme.
 * Use for: Text on error backgrounds, error descriptions.
 * Access via: MaterialTheme.colorScheme.onErrorContainer */
val DarkOnErrorContainer = Color(0xFFFFDAD6)

// ============================================================================
// Light Theme Colors
// ============================================================================

/** Primary brand color for light theme - Deep blue for main actions and emphasis.
 * Use for: Primary buttons, FABs, prominent interactive elements.
 * Access via: MaterialTheme.colorScheme.primary */
val LightPrimary = Color(0xFF1A237E)

/** Text/icon color that appears on primary color backgrounds in light theme.
 * Use for: Text on primary buttons, icons on primary surfaces.
 * Access via: MaterialTheme.colorScheme.onPrimary */
val LightOnPrimary = Color.White

/** Container color for primary elements in light theme - Light blue for less emphasis.
 * Use for: Tonal primary buttons, chips, highlighted containers.
 * Access via: MaterialTheme.colorScheme.primaryContainer */
val LightPrimaryContainer = Color(0xFFE8EAF6)

/** Text/icon color for primary containers in light theme.
 * Use for: Text on primaryContainer backgrounds.
 * Access via: MaterialTheme.colorScheme.onPrimaryContainer */
val LightOnPrimaryContainer = Color(0xFF1A237E)

/** Secondary brand color for light theme - Deep teal for secondary actions.
 * Use for: Secondary buttons, complementary UI elements, alternative highlights.
 * Access via: MaterialTheme.colorScheme.secondary */
val LightSecondary = Color(0xFF004D40)

/** Text/icon color on secondary color backgrounds in light theme.
 * Use for: Text on secondary buttons, icons on secondary surfaces.
 * Access via: MaterialTheme.colorScheme.onSecondary */
val LightOnSecondary = Color.White

/** Container color for secondary elements in light theme.
 * Use for: Tonal secondary buttons, complementary containers.
 * Access via: MaterialTheme.colorScheme.secondaryContainer */
val LightSecondaryContainer = Color(0xFFB2DFDB)

/** Text/icon color for secondary containers in light theme.
 * Use for: Text on secondaryContainer backgrounds.
 * Access via: MaterialTheme.colorScheme.onSecondaryContainer */
val LightOnSecondaryContainer = Color(0xFF004D40)

/** Tertiary brand color for light theme - Deep purple for tertiary actions.
 * Use for: Tertiary buttons, additional accent elements, variety in UI.
 * Access via: MaterialTheme.colorScheme.tertiary */
val LightTertiary = Color(0xFF4527A0)

/** Text/icon color on tertiary color backgrounds in light theme.
 * Use for: Text on tertiary buttons, icons on tertiary surfaces.
 * Access via: MaterialTheme.colorScheme.onTertiary */
val LightOnTertiary = Color.White

/** Container color for tertiary elements in light theme.
 * Use for: Tonal tertiary buttons, additional containers.
 * Access via: MaterialTheme.colorScheme.tertiaryContainer */
val LightTertiaryContainer = Color(0xFFEDE7F6)

/** Text/icon color for tertiary containers in light theme.
 * Use for: Text on tertiaryContainer backgrounds.
 * Access via: MaterialTheme.colorScheme.onTertiaryContainer */
val LightOnTertiaryContainer = Color(0xFF4527A0)

/** Main background color for light theme - Light gray for reduced eye strain.
 * Use for: Main app background, screen backgrounds.
 * Access via: MaterialTheme.colorScheme.background */
val LightBackground = Color(0xFFF5F5F5)

/** Text/icon color that appears on background in light theme.
 * Use for: Body text, icons on background.
 * Access via: MaterialTheme.colorScheme.onBackground */
val LightOnBackground = Color(0xFF1C1B1F)

/** Surface color for light theme - White for cards and elevated elements.
 * Use for: Cards, dialogs, bottom sheets, top app bars.
 * Access via: MaterialTheme.colorScheme.surface */
val LightSurface = Color.White

/** Text/icon color that appears on surfaces in light theme.
 * Use for: Text on cards, icons on dialogs, most body text.
 * Access via: MaterialTheme.colorScheme.onSurface */
val LightOnSurface = Color(0xFF1C1B1F)

/** Variant surface color for light theme - Used for subtle differentiation.
 * Use for: Subtle backgrounds, input field backgrounds, dividers.
 * Access via: MaterialTheme.colorScheme.surfaceVariant */
val LightSurfaceVariant = Color(0xFFE0E0E0)

/** Text/icon color for surface variants in light theme - Lower emphasis.
 * Use for: Secondary text, disabled text, subtle icons.
 * Access via: MaterialTheme.colorScheme.onSurfaceVariant */
val LightOnSurfaceVariant = Color(0xFF49454F)

/** Error color for light theme - Red for error states.
 * Use for: Error messages, error icons, destructive actions.
 * Access via: MaterialTheme.colorScheme.error */
val LightError = Color(0xFFB00020)

/** Text/icon color on error color backgrounds in light theme.
 * Use for: Text on error buttons, icons on error surfaces.
 * Access via: MaterialTheme.colorScheme.onError */
val LightOnError = Color.White

/** Container color for error elements in light theme.
 * Use for: Error message backgrounds, alert containers.
 * Access via: MaterialTheme.colorScheme.errorContainer */
val LightErrorContainer = Color(0xFFFFDAD6)

/** Text/icon color for error containers in light theme.
 * Use for: Text on error backgrounds, error descriptions.
 * Access via: MaterialTheme.colorScheme.onErrorContainer */
val LightOnErrorContainer = Color(0xFFB00020)
