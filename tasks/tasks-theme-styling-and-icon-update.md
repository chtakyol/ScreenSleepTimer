# Task List: Theme Styling, App Icon & Splash Screen Update

## Relevant Files

### Files to Create
- `app/src/main/res/values/splash.xml` - Splash screen theme configuration
- `docs/THEME_USAGE_GUIDE.md` - Theme usage guidelines and patterns for developers

### Files to Modify
- `app/build.gradle.kts` - Add SplashScreen API dependency
- `app/src/main/java/com/cihatakyol/sleeptimer/MainActivity.kt` - Install splash screen API
- `app/src/main/res/values/themes.xml` - Configure splash screen theme
- `app/src/main/res/mipmap-mdpi/ic_launcher.png` - Replace with new icon (mdpi)
- `app/src/main/res/mipmap-hdpi/ic_launcher.png` - Replace with new icon (hdpi)
- `app/src/main/res/mipmap-xhdpi/ic_launcher.png` - Replace with new icon (xhdpi)
- `app/src/main/res/mipmap-xxhdpi/ic_launcher.png` - Replace with new icon (xxhdpi)
- `app/src/main/res/mipmap-xxxhdpi/ic_launcher.png` - Replace with new icon (xxxhdpi)
- `app/src/main/res/mipmap-mdpi/ic_launcher_round.png` - Replace with new round icon (mdpi)
- `app/src/main/res/mipmap-hdpi/ic_launcher_round.png` - Replace with new round icon (hdpi)
- `app/src/main/res/mipmap-xhdpi/ic_launcher_round.png` - Replace with new round icon (xhdpi)
- `app/src/main/res/mipmap-xxhdpi/ic_launcher_round.png` - Replace with new round icon (xxhdpi)
- `app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png` - Replace with new round icon (xxxhdpi)
- `app/src/main/res/drawable/ic_launcher_foreground.xml` - Update adaptive icon foreground
- `app/src/main/res/drawable/ic_launcher_background.xml` - Update adaptive icon background
- `app/src/main/java/com/cihatakyol/sleeptimer/ui/screens/onboarding/OnboardingPage1.kt` - Update icon reference
- `app/src/main/java/com/cihatakyol/sleeptimer/ui/screens/onboarding/OnboardingPage2.kt` - Fix any hardcoded colors
- `app/src/main/java/com/cihatakyol/sleeptimer/ui/screens/onboarding/OnboardingPage3.kt` - Fix any hardcoded colors
- `app/src/main/java/com/cihatakyol/sleeptimer/ui/screens/onboarding/OnboardingScreen.kt` - Fix any hardcoded colors
- `app/src/main/java/com/cihatakyol/sleeptimer/ui/screens/onboarding/OnboardingPage.kt` - Fix any hardcoded colors
- `app/src/main/java/com/cihatakyol/sleeptimer/ui/theme/Color.kt` - Add documentation comments
- `app/src/main/java/com/cihatakyol/sleeptimer/ui/theme/Theme.kt` - Add usage guidelines

### Notes

- The new app icon should already exist in the project - need to identify and use it
- Splash screen will use the new app icon
- All onboarding screens should be audited for hardcoded colors
- Main app screens will be audited in a separate phase
- Theme colors already defined in `Color.kt` and `Theme.kt`

## Instructions for Completing Tasks

**IMPORTANT:** As you complete each task, you must check it off in this markdown file by changing `- [ ]` to `- [x]`. This helps track progress and ensures you don't skip any steps.

Example:
- `- [ ] 1.1 Read file` → `- [x] 1.1 Read file` (after completing)

Update the file after completing each sub-task, not just after completing an entire parent task.

## Tasks

- [x] 0.0 Create feature branch
  - [x] 0.1 Create and checkout a new branch for this feature (e.g., `git checkout -b feature/theme-styling-icon-update`)

- [ ] 1.0 Locate and verify new app icon assets
  - [ ] 1.1 Search project for new icon files (check `res/drawable/`, `res/mipmap/`, etc.)
  - [ ] 1.2 Identify which icon is the "new" icon (look for recently added files or files not currently referenced)
  - [ ] 1.3 Verify icon exists in all required formats (PNG for mipmap, XML for adaptive)
  - [ ] 1.4 Document the path/name of the new icon files for reference
  - [ ] 1.5 Check if icon follows Android adaptive icon guidelines (foreground + background)

- [ ] 2.0 Update app icon across all densities and variants
  - [ ] 2.1 Replace `ic_launcher.png` in all mipmap density folders (mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi) with new icon
  - [ ] 2.2 Replace `ic_launcher_round.png` in all mipmap density folders with new round icon variant
  - [ ] 2.3 Update `ic_launcher_foreground.xml` with new icon foreground design
  - [ ] 2.4 Update `ic_launcher_background.xml` with new icon background (or solid color if applicable)
  - [ ] 2.5 Verify adaptive icon configuration in `mipmap-anydpi-v26/ic_launcher.xml`
  - [ ] 2.6 Verify adaptive icon configuration in `mipmap-anydpi-v26/ic_launcher_round.xml`
  - [ ] 2.7 Update `OnboardingPage1.kt` to reference new icon instead of `R.drawable.app_logo`
  - [ ] 2.8 Build and run app to verify icon appears correctly on launcher
  - [ ] 2.9 Test icon appearance in recent apps/task switcher
  - [ ] 2.10 Test icon appearance in Settings → Apps

- [ ] 3.0 Implement Android 12+ SplashScreen API
  - [ ] 3.1 Read current `app/build.gradle.kts` dependencies section
  - [ ] 3.2 Add SplashScreen API dependency: `implementation("androidx.core:core-splashscreen:1.0.1")`
  - [ ] 3.3 Sync Gradle and verify no conflicts
  - [ ] 3.4 Read current `app/src/main/res/values/themes.xml`
  - [ ] 3.5 Create new splash screen theme in `themes.xml` extending from `Theme.SplashScreen`
  - [ ] 3.6 Configure splash screen `windowSplashScreenBackground` using `?attr/colorPrimary`
  - [ ] 3.7 Configure splash screen `windowSplashScreenAnimatedIcon` using new app icon
  - [ ] 3.8 Set `postSplashScreenTheme` to main app theme
  - [ ] 3.9 Read current `MainActivity.kt` implementation
  - [ ] 3.10 Add `installSplashScreen()` call before `setContent` in `MainActivity.onCreate()`
  - [ ] 3.11 Import required splash screen packages
  - [ ] 3.12 Update `AndroidManifest.xml` to use splash screen theme for MainActivity (if needed)
  - [ ] 3.13 Test splash screen on Android 12+ device/emulator
  - [ ] 3.14 Test splash screen on Android 11 and below device/emulator
  - [ ] 3.15 Verify smooth transition from splash to main content
  - [ ] 3.16 Test both cold start and warm start scenarios

- [ ] 4.0 Audit and fix onboarding screen colors
  - [ ] 4.1 Read `OnboardingPage.kt` and identify any hardcoded Color() values
  - [ ] 4.2 Read `OnboardingPage1.kt` and identify any hardcoded colors
  - [ ] 4.3 Read `OnboardingPage2.kt` and identify any hardcoded colors
  - [ ] 4.4 Read `OnboardingPage3.kt` and identify any hardcoded colors
  - [ ] 4.5 Read `OnboardingScreen.kt` and identify any hardcoded colors or background colors
  - [ ] 4.6 Document all hardcoded colors found (color value, location, intended purpose)
  - [ ] 4.7 Replace hardcoded text colors with appropriate MaterialTheme colors (onSurface, onBackground, etc.)
  - [ ] 4.8 Replace hardcoded background colors with MaterialTheme colors (background, surface, etc.)
  - [ ] 4.9 Ensure any custom composables use MaterialTheme colors
  - [ ] 4.10 Check for any ColorFilter or tint modifiers that use hardcoded colors
  - [ ] 4.11 Update page indicators (if present) to use theme colors
  - [ ] 4.12 Build and run app with light theme enabled
  - [ ] 4.13 Navigate through all onboarding pages in light mode and verify readability
  - [ ] 4.14 Switch to dark theme (system settings or emulator quick settings)
  - [ ] 4.15 Navigate through all onboarding pages in dark mode and verify readability
  - [ ] 4.16 Verify buttons are visible and accessible in both themes
  - [ ] 4.17 Verify icons/images display well in both themes
  - [ ] 4.18 Take screenshots of before/after for documentation

- [ ] 5.0 Audit main app screens for theme usage
  - [ ] 5.1 List all main screens in the app (timer screen, settings, dialogs, etc.)
  - [ ] 5.2 Read main timer screen code and identify hardcoded colors
  - [ ] 5.3 Read settings screen(s) code and identify hardcoded colors
  - [ ] 5.4 Search for any dialogs or bottom sheets and check for hardcoded colors
  - [ ] 5.5 Search for any custom composables in `ui/components/` (if exists) for hardcoded colors
  - [ ] 5.6 Document findings: list files and line numbers with hardcoded colors
  - [ ] 5.7 Prioritize fixes based on visual impact and user visibility
  - [ ] 5.8 Replace hardcoded colors in main timer screen with MaterialTheme colors
  - [ ] 5.9 Replace hardcoded colors in settings screens with MaterialTheme colors
  - [ ] 5.10 Replace hardcoded colors in dialogs/bottom sheets with MaterialTheme colors
  - [ ] 5.11 Replace hardcoded colors in custom composables with MaterialTheme colors
  - [ ] 5.12 Test main app screens in light mode
  - [ ] 5.13 Test main app screens in dark mode
  - [ ] 5.14 Test on Android 12+ device with dynamic colors enabled
  - [ ] 5.15 Verify dynamic colors work correctly (colors adapt to wallpaper)
  - [ ] 5.16 Test on Android 11 device and verify static theme colors work
  - [ ] 5.17 Check status bar color matches theme background
  - [ ] 5.18 Check navigation bar color (if applicable) matches theme

- [ ] 6.0 Add theme documentation and usage guidelines
  - [ ] 6.1 Read current `Color.kt` file
  - [ ] 6.2 Add KDoc comments for each color explaining its purpose
  - [ ] 6.3 Add example usage comments (e.g., "Use DarkPrimary for primary actions in dark mode")
  - [ ] 6.4 Read current `Theme.kt` file
  - [ ] 6.5 Add KDoc comment at top explaining the theme structure
  - [ ] 6.6 Document when to use primary vs secondary vs tertiary colors
  - [ ] 6.7 Document the difference between surface and background
  - [ ] 6.8 Add comment explaining dynamic color support
  - [ ] 6.9 Create `docs/THEME_USAGE_GUIDE.md` file
  - [ ] 6.10 Write introduction section explaining Material Design 3 theming
  - [ ] 6.11 Document all color roles and when to use each
  - [ ] 6.12 Add code examples for common patterns (text on surface, text on primary, etc.)
  - [ ] 6.13 Add "Do's and Don'ts" section showing correct vs incorrect usage
  - [ ] 6.14 Document how to test theme changes in light and dark mode
  - [ ] 6.15 Add section on dynamic colors and Android 12+ support
  - [ ] 6.16 Add examples from the fixed onboarding/main screens as reference

- [ ] 7.0 Test across devices and Android versions
  - [ ] 7.1 Test on physical Android 12+ device (or emulator) with dynamic colors enabled
  - [ ] 7.2 Verify splash screen appears with correct icon and background
  - [ ] 7.3 Verify app icon is correct on home screen and launcher
  - [ ] 7.4 Navigate through entire app in light mode, check all screens
  - [ ] 7.5 Navigate through entire app in dark mode, check all screens
  - [ ] 7.6 Test on Android 11 device/emulator (pre-splash screen API)
  - [ ] 7.7 Verify splash screen still works (using fallback theme)
  - [ ] 7.8 Verify app icon displays correctly
  - [ ] 7.9 Verify theme colors work correctly without dynamic color support
  - [ ] 7.10 Test on tablet (if applicable) to verify theme scales properly
  - [ ] 7.11 Run Android Accessibility Scanner on main screens
  - [ ] 7.12 Verify no contrast ratio warnings or errors
  - [ ] 7.13 Check that all interactive elements have sufficient touch targets
  - [ ] 7.14 Test onboarding flow start to finish in both themes
  - [ ] 7.15 Test main timer functionality in both themes
  - [ ] 7.16 Test settings screens in both themes
  - [ ] 7.17 Take screenshots for all main screens in light mode
  - [ ] 7.18 Take screenshots for all main screens in dark mode
  - [ ] 7.19 Build release APK and test on physical device
  - [ ] 7.20 Verify ProGuard/R8 doesn't break theme or icon functionality
  - [ ] 7.21 Document any issues found and create follow-up tasks if needed
  - [ ] 7.22 Mark feature as complete and ready for code review
