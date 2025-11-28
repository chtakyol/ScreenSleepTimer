# SleepTimer Theme Usage Guide

## Introduction

This guide explains how to properly use Material Design 3 theming in the SleepTimer app. Following these guidelines ensures visual consistency, proper accessibility, and seamless theme switching between light and dark modes.

## Table of Contents

- [Quick Start](#quick-start)
- [Color Roles Explained](#color-roles-explained)
- [Common Patterns](#common-patterns)
- [Do's and Don'ts](#dos-and-donts)
- [Testing Theme Changes](#testing-theme-changes)
- [Dynamic Colors](#dynamic-colors)
- [Accessibility Considerations](#accessibility-considerations)

---

## Quick Start

### The Golden Rule

**Always use `MaterialTheme.colorScheme` colors, NEVER hardcoded `Color()` values.**

```kotlin
// ✅ GOOD - Uses theme colors
Text(
    text = "Hello",
    color = MaterialTheme.colorScheme.onSurface
)

// ❌ BAD - Hardcoded color breaks theme
Text(
    text = "Hello",
    color = Color(0xFF000000)
)
```

### Applying the Theme

The `SleepTimerTheme` composable wraps your content and provides theming:

```kotlin
@Composable
fun MyScreen() {
    SleepTimerTheme {
        // Your content automatically has access to theme colors
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ScreenContent()
        }
    }
}
```

---

## Color Roles Explained

Material Design 3 uses semantic color roles instead of specific color names. This allows the same code to work perfectly in both light and dark themes.

### Primary Colors

**When to use:** Main actions, important UI elements, brand identity

| Color Role | Purpose | Example Usage |
|------------|---------|---------------|
| `primary` | Main brand color for key actions | FAB buttons, primary action buttons, key interactive elements |
| `onPrimary` | Text/icons that appear on primary color | Text on primary buttons, icons on primary surfaces |
| `primaryContainer` | Less emphasized primary color | Tonal buttons, chips, highlighted containers |
| `onPrimaryContainer` | Text/icons on primary containers | Text on tonal buttons |

**Light Theme:** Primary = Deep Blue (#1A237E)
**Dark Theme:** Primary = Soft Blue (#6B8AFE)

```kotlin
// Primary button example
Button(
    onClick = { /* action */ },
    colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    )
) {
    Text("Start Timer")
}

// Tonal button example
Button(
    onClick = { /* action */ },
    colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    )
) {
    Text("Secondary Action")
}
```

### Secondary Colors

**When to use:** Complementary actions, secondary UI elements

| Color Role | Purpose | Example Usage |
|------------|---------|---------------|
| `secondary` | Complementary brand color | Secondary buttons, filter chips, accents |
| `onSecondary` | Text/icons on secondary color | Text on secondary buttons |
| `secondaryContainer` | Less emphasized secondary color | Secondary tonal buttons, tags |
| `onSecondaryContainer` | Text/icons on secondary containers | Text on secondary tonal elements |

**Light Theme:** Secondary = Deep Teal (#004D40)
**Dark Theme:** Secondary = Teal Accent (#03DAC6)

```kotlin
// Secondary action example
OutlinedButton(
    onClick = { /* action */ },
    colors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.secondary
    )
) {
    Text("Cancel")
}
```

### Tertiary Colors

**When to use:** Additional variety, special accents, third-level actions

| Color Role | Purpose | Example Usage |
|------------|---------|---------------|
| `tertiary` | Additional accent color | Special highlights, badges, info chips |
| `onTertiary` | Text/icons on tertiary color | Text on tertiary elements |
| `tertiaryContainer` | Less emphasized tertiary color | Info containers, special cards |
| `onTertiaryContainer` | Text/icons on tertiary containers | Text on info containers |

**Light Theme:** Tertiary = Deep Purple (#4527A0)
**Dark Theme:** Tertiary = Soft Purple (#B39DDB)

### Surface & Background Colors

**Key Concept:** Background is behind all content, Surface is for elevated components.

| Color Role | Purpose | Example Usage |
|------------|---------|---------------|
| `background` | Main app background | Screen backgrounds, behind all content |
| `onBackground` | Text/icons on background | Body text directly on screen background |
| `surface` | Background for components | Cards, dialogs, bottom sheets, top bars |
| `onSurface` | Text/icons on surface | Most common text color, icons on cards |
| `surfaceVariant` | Subtle surface differentiation | Input field backgrounds, dividers, subtle containers |
| `onSurfaceVariant` | Text/icons on surface variants | Secondary text, disabled text, placeholders |

**Light Theme:**
- Background = Light Gray (#F5F5F5)
- Surface = White

**Dark Theme:**
- Background = True Dark (#121212)
- Surface = Slightly Lighter Dark (#1E1E1E)

```kotlin
// Main screen background
Surface(
    modifier = Modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
) {
    // Body text on background
    Text(
        text = "Main content",
        color = MaterialTheme.colorScheme.onBackground
    )
}

// Card with surface
Card(
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
    )
) {
    // Text on card surface
    Text(
        text = "Card content",
        color = MaterialTheme.colorScheme.onSurface
    )
}

// Input field with surface variant
OutlinedTextField(
    value = text,
    onValueChange = { text = it },
    colors = TextFieldDefaults.colors(
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
)
```

### Error Colors

**When to use:** Error states, validation messages, destructive actions

| Color Role | Purpose | Example Usage |
|------------|---------|---------------|
| `error` | Error state color | Error icons, destructive buttons, alerts |
| `onError` | Text/icons on error color | Text on error buttons |
| `errorContainer` | Background for error messages | Error message containers, alert backgrounds |
| `onErrorContainer` | Text in error messages | Error message text |

**Light Theme:** Error = Red (#B00020)
**Dark Theme:** Error = Soft Red (#CF6679)

```kotlin
// Error message
Card(
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.errorContainer
    )
) {
    Text(
        text = "Timer failed to start",
        color = MaterialTheme.colorScheme.onErrorContainer
    )
}

// Destructive action button
Button(
    onClick = { /* delete */ },
    colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.error,
        contentColor = MaterialTheme.colorScheme.onError
    )
) {
    Text("Delete Timer")
}
```

---

## Common Patterns

### Pattern 1: Text on Surface (Most Common)

```kotlin
// Heading text on card
Text(
    text = "Sleep Timer",
    style = MaterialTheme.typography.headlineMedium,
    color = MaterialTheme.colorScheme.onSurface
)

// Body text on card
Text(
    text = "Set a timer to automatically turn off your device",
    style = MaterialTheme.typography.bodyMedium,
    color = MaterialTheme.colorScheme.onSurface
)
```

### Pattern 2: Secondary/Disabled Text

```kotlin
// Less emphasized text
Text(
    text = "Optional description",
    style = MaterialTheme.typography.bodySmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant
)

// Disabled state
Text(
    text = "Disabled feature",
    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
)
```

### Pattern 3: Gradients with Theme Colors

```kotlin
Box(
    modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.background,
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    MaterialTheme.colorScheme.background
                )
            )
        )
)
```

### Pattern 4: Icons with Theme Colors

```kotlin
Icon(
    imageVector = Icons.Default.Timer,
    contentDescription = "Timer",
    tint = MaterialTheme.colorScheme.primary
)

// Icon on primary button (automatically gets onPrimary color)
Button(onClick = { }) {
    Icon(
        imageVector = Icons.Default.PlayArrow,
        contentDescription = null
    )
    Spacer(Modifier.width(8.dp))
    Text("Start")
}
```

### Pattern 5: Custom Composables

When creating custom composables, always use MaterialTheme colors:

```kotlin
@Composable
fun TimerCard(
    title: String,
    duration: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = duration,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
```

### Pattern 6: Page Indicators

```kotlin
@Composable
fun PagerIndicator(currentPage: Int, pageCount: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(pageCount) { index ->
            val color = if (index == currentPage) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            }

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )

            if (index < pageCount - 1) {
                Spacer(Modifier.width(8.dp))
            }
        }
    }
}
```

---

## Do's and Don'ts

### ✅ DO

1. **Always use MaterialTheme.colorScheme colors**
   ```kotlin
   color = MaterialTheme.colorScheme.onSurface
   ```

2. **Use semantic color roles based on purpose**
   ```kotlin
   // For primary action
   containerColor = MaterialTheme.colorScheme.primary

   // For error state
   containerColor = MaterialTheme.colorScheme.errorContainer
   ```

3. **Use alpha for transparency**
   ```kotlin
   color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
   ```

4. **Test in both light and dark themes**
   - Change system theme settings
   - Verify all screens are readable
   - Check contrast ratios

5. **Use onX colors for content on X backgrounds**
   ```kotlin
   // Text on primary button
   Button(
       colors = ButtonDefaults.buttonColors(
           containerColor = MaterialTheme.colorScheme.primary,
           contentColor = MaterialTheme.colorScheme.onPrimary // Correct!
       )
   )
   ```

### ❌ DON'T

1. **Never hardcode Color() values in UI code**
   ```kotlin
   // ❌ BAD
   color = Color(0xFF000000)
   color = Color.White
   color = Color.Black
   ```

2. **Don't reference theme colors directly (use via MaterialTheme)**
   ```kotlin
   // ❌ BAD
   import com.cihatakyol.sleeptimer.ui.theme.DarkPrimary
   color = DarkPrimary

   // ✅ GOOD
   color = MaterialTheme.colorScheme.primary
   ```

3. **Don't use wrong onX colors for backgrounds**
   ```kotlin
   // ❌ BAD - onPrimary on surface background
   Text(
       text = "Text",
       color = MaterialTheme.colorScheme.onPrimary // Wrong!
   )

   // ✅ GOOD - onSurface for surface background
   Text(
       text = "Text",
       color = MaterialTheme.colorScheme.onSurface
   )
   ```

4. **Don't assume theme - let MaterialTheme decide**
   ```kotlin
   // ❌ BAD - assumes dark theme
   val isDark = true
   color = if (isDark) Color.White else Color.Black

   // ✅ GOOD - theme-aware
   color = MaterialTheme.colorScheme.onSurface
   ```

5. **Don't create custom color constants for UI elements**
   ```kotlin
   // ❌ BAD
   val ButtonBackgroundColor = Color(0xFF1A237E)

   // ✅ GOOD
   containerColor = MaterialTheme.colorScheme.primary
   ```

---

## Testing Theme Changes

### Manual Testing Checklist

1. **Switch system theme:**
   - Settings → Display → Dark theme (or use Quick Settings)
   - Or use Android Studio emulator quick settings

2. **Navigate through all screens:**
   - Onboarding flow
   - Main timer screen
   - Settings screens
   - Dialogs and bottom sheets

3. **Check for issues:**
   - Is all text readable?
   - Are buttons visible?
   - Do icons display correctly?
   - Are interactive elements easy to identify?
   - Is contrast sufficient?

### Light Mode Testing

1. Enable light theme in system settings
2. Launch app and verify:
   - Background is light gray (#F5F5F5)
   - Text is dark and readable
   - Primary buttons are deep blue (#1A237E)
   - Cards and surfaces are white

### Dark Mode Testing

1. Enable dark theme in system settings
2. Launch app and verify:
   - Background is true dark (#121212)
   - Text is light and readable
   - Primary buttons are soft blue (#6B8AFE)
   - Cards and surfaces are slightly lighter (#1E1E1E)
   - No pure white that causes eye strain

### Testing Dynamic Colors (Android 12+)

1. Change wallpaper on Android 12+ device
2. Notice theme colors adapt to wallpaper
3. Verify app still looks good with dynamic colors
4. Test on Android 11 to verify fallback works

---

## Dynamic Colors

### What are Dynamic Colors?

On Android 12+, Material You introduces dynamic colors that adapt to the user's wallpaper, creating a personalized color scheme.

### How Dynamic Colors Work in SleepTimer

```kotlin
SleepTimerTheme(
    dynamicColor: Boolean = true // Enabled by default
) {
    // Content
}
```

- **Android 12+:** Uses `dynamicDarkColorScheme` / `dynamicLightColorScheme` from wallpaper
- **Android 11 and below:** Falls back to custom color scheme (deep blue / soft blue)

### When to Disable Dynamic Colors

Most apps should keep dynamic colors enabled. Consider disabling only if:
- Your brand identity requires specific colors
- Testing specific color combinations
- You need consistent screenshots

```kotlin
SleepTimerTheme(dynamicColor = false) {
    // Forces custom color scheme even on Android 12+
}
```

---

## Accessibility Considerations

### Contrast Ratios

Material Design 3 ensures all color combinations meet WCAG AA standards:
- **Normal text:** 4.5:1 contrast ratio minimum
- **Large text:** 3:1 contrast ratio minimum

Our theme colors already meet these requirements when used correctly.

### Testing Accessibility

1. **Use Android Accessibility Scanner:**
   - Install from Play Store
   - Run on your screens
   - Fix any contrast warnings

2. **Enable TalkBack:**
   - Settings → Accessibility → TalkBack
   - Navigate through app with screen reader
   - Ensure all elements are properly labeled

3. **Test with different text sizes:**
   - Settings → Display → Font size
   - Try largest setting
   - Verify no text gets cut off

### Best Practices

1. Always use recommended on-X colors:
   ```kotlin
   // ✅ Proper contrast guaranteed
   Surface(color = MaterialTheme.colorScheme.primary) {
       Text(color = MaterialTheme.colorScheme.onPrimary)
   }
   ```

2. Don't reduce opacity too much:
   ```kotlin
   // ❌ May fail contrast ratio
   color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)

   // ✅ Better
   color = MaterialTheme.colorScheme.onSurfaceVariant
   ```

3. Provide sufficient touch targets (48dp minimum):
   ```kotlin
   IconButton(
       onClick = { },
       modifier = Modifier.size(48.dp) // Good touch target
   ) {
       Icon(...)
   }
   ```

---

## Examples from SleepTimer App

### Onboarding Screen (Good Example)

From `OnboardingScreen.kt`:

```kotlin
// Gradient background using theme colors
val backgroundColor = MaterialTheme.colorScheme.background
val primaryColor = MaterialTheme.colorScheme.primary
val gradientColors = listOf(
    backgroundColor,
    primaryColor.copy(alpha = 0.3f),
    backgroundColor
)

Box(
    modifier = Modifier.background(
        brush = Brush.verticalGradient(colors = gradientColors)
    )
)
```

```kotlin
// Page indicators using theme colors
val activeColor = MaterialTheme.colorScheme.primary
val inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

val color = if (currentPage == iteration) {
    activeColor
} else {
    inactiveColor
}
```

### Onboarding Page Component (Good Example)

From `OnboardingPage.kt`:

```kotlin
@Composable
fun OnboardingPage(
    icon: ImageVector,
    title: String,
    message: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    Column {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Button(onClick = onButtonClick) {
            Text(buttonText)
        }
    }
}
```

---

## Quick Reference

| Scenario | Color Role to Use |
|----------|-------------------|
| Body text on screen | `onSurface` |
| Body text on card | `onSurface` |
| Secondary/helper text | `onSurfaceVariant` |
| Screen background | `background` |
| Card background | `surface` |
| Primary button background | `primary` |
| Primary button text | `onPrimary` |
| Tonal button background | `primaryContainer` |
| Tonal button text | `onPrimaryContainer` |
| Error message background | `errorContainer` |
| Error message text | `onErrorContainer` |
| Icons (general) | `onSurface` or `primary` |
| Disabled text | `onSurface.copy(alpha = 0.38f)` |
| Dividers | `surfaceVariant` or `onSurface.copy(alpha = 0.12f)` |

---

## Additional Resources

- [Material Design 3 Color System](https://m3.material.io/styles/color/overview)
- [Material Design 3 Compose Documentation](https://developer.android.com/jetpack/compose/themes/material3)
- [WCAG Contrast Guidelines](https://www.w3.org/WAI/WCAG21/Understanding/contrast-minimum.html)

---

**Last Updated:** 2025-11-28
**Maintained By:** SleepTimer Development Team
