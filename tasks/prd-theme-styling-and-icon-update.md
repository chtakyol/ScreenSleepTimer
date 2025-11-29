# PRD: Theme Styling, App Icon & Splash Screen Update

## Introduction/Overview

This feature focuses on applying the existing Material Design 3 theme consistently across the SleepTimer app, updating the app icon and splash screen with new branding assets, and fixing hardcoded colors in the onboarding screens. The app already has a well-defined theme in `Theme.kt` with both light and dark color schemes, but some screens (particularly onboarding) are not using these theme colors properly, resulting in visual inconsistency.

**Problem:**
- Onboarding screens use hardcoded colors instead of MaterialTheme colors, breaking theme consistency
- The new app icon is in the project but not being used
- No modern splash screen implementation (Android 12+ SplashScreen API)
- Need to audit entire app to ensure all screens use theme colors properly

**Goal:** Create a visually consistent app experience that properly leverages the existing Material Design 3 theme, updates branding assets, and provides a polished first impression with a modern splash screen.

## Goals

1. **Theme Consistency**: Ensure all screens (especially onboarding) use MaterialTheme colors instead of hardcoded values
2. **Brand Update**: Replace current app icon with new icon assets throughout the app
3. **Modern UX**: Implement Android 12+ SplashScreen API for smooth app launch experience
4. **Accessibility**: Maintain proper contrast ratios and readability in both light and dark themes
5. **Maintainability**: Establish pattern of using theme colors that developers can follow

## User Stories

### As a user:
- I want the app to have consistent colors throughout so the experience feels polished and professional
- I want to see the updated app icon that reflects the current branding
- I want a smooth, modern splash screen when launching the app
- I want the app to look good in both light and dark mode with proper contrast

### As a developer:
- I want to easily identify where theme colors should be used instead of hardcoded colors
- I want a clear example of how to properly use MaterialTheme in Compose
- I want the splash screen to work on both Android 12+ and older versions

## Functional Requirements

### 1. App Icon Update

**FR-1.1**: Replace all app icon assets with the new icon that already exists in the project
- Update launcher icon (round and square variants)
- Update all density variants (mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi)
- Update adaptive icon (foreground and background)

**FR-1.2**: Update the app icon reference in onboarding screens
- Currently uses `R.drawable.app_logo` in `OnboardingPage1.kt`
- Should reference the new icon asset

**FR-1.3**: Ensure icon displays correctly in:
- App launcher (home screen)
- Recent apps/task switcher
- Settings → Apps list
- Onboarding first screen

### 2. Splash Screen Implementation

**FR-2.1**: Implement Android 12+ SplashScreen API
- Use `androidx.core:core-splashscreen` library
- Configure splash screen theme in `themes.xml`
- Set splash screen icon to the new app icon
- Configure background color using theme primary color

**FR-2.2**: Handle splash screen dismissal properly
- Dismiss splash screen after app initialization completes
- Ensure smooth transition to main content
- Handle both cold start and warm start scenarios

**FR-2.3**: Maintain backward compatibility
- Splash screen should work on Android 12+ with new API
- Use existing `Theme.SleepTimer.Splash` for older Android versions
- Ensure consistent visual experience across all Android versions

**FR-2.4**: Configure splash screen branding
- Icon should be centered
- Background should use theme colors (not hardcoded)
- Icon should have proper sizing (follow Android guidelines)

### 3. Onboarding Color Fixes

**FR-3.1**: Audit onboarding screen files for hardcoded colors
- Check: `OnboardingPage.kt`, `OnboardingPage1.kt`, `OnboardingPage2.kt`, `OnboardingPage3.kt`, `OnboardingScreen.kt`
- Identify all hardcoded `Color()` values
- Document current vs expected theme colors

**FR-3.2**: Replace hardcoded colors with MaterialTheme colors
- Text colors should use `MaterialTheme.colorScheme.onSurface`, `onBackground`, `onPrimary`, etc.
- Background colors should use `MaterialTheme.colorScheme.surface`, `background`, etc.
- Button colors should use `MaterialTheme.colorScheme.primary`, `secondary`, etc.
- Ensure proper contrast ratios are maintained

**FR-3.3**: Test onboarding in both light and dark themes
- Verify text is readable in light mode
- Verify text is readable in dark mode
- Check all interactive elements (buttons) are visible and accessible
- Ensure icons/images work well with both themes

**FR-3.4**: Update onboarding page indicators (if present)
- Use theme colors for active/inactive indicators
- Ensure visibility in both themes

### 4. App-Wide Theme Audit

**FR-4.1**: Audit main app screens for theme color usage
- Check main timer screen
- Check settings screens
- Check any dialogs or bottom sheets
- Document findings

**FR-4.2**: Fix any identified theme color issues
- Replace hardcoded colors with MaterialTheme colors
- Follow the same pattern as onboarding fixes
- Prioritize screens with most visual inconsistencies

**FR-4.3**: Verify dynamic color support (Android 12+)
- Theme already supports dynamic colors (`dynamicColor: Boolean = true`)
- Test that dynamic colors work correctly when enabled
- Ensure fallback to custom theme colors works on older versions

**FR-4.4**: Test across different devices and Android versions
- Test on Android 12+ with dynamic colors enabled
- Test on Android 11 and below with static theme
- Verify both light and dark modes
- Check on different screen sizes

### 5. Theme Documentation

**FR-5.1**: Add code comments to theme files
- Document the purpose of each color in `Color.kt`
- Add usage guidelines in `Theme.kt`
- Document when to use each color role (primary, secondary, tertiary, surface, etc.)

**FR-5.2**: Create example usage patterns
- Add comments in fixed files showing proper MaterialTheme usage
- Document common patterns (e.g., text on surface, text on primary, etc.)

## Non-Goals (Out of Scope)

1. **Creating new theme colors**: We use the existing theme defined in `Theme.kt` and `Color.kt`
2. **Redesigning UI layouts**: Only colors change, not layouts or component structure
3. **Creating a theme switcher**: App already respects system theme preference
4. **Custom theme persistence**: No user preference for light/dark theme override
5. **Animation changes**: No changes to existing animations or transitions
6. **Icon design**: Using existing new icon, not creating a new design
7. **Splash screen animations**: Basic splash screen only, no complex animations
8. **Typography changes**: Only colors change, typography remains the same

## Design Considerations

### Current Theme Structure

The app has a well-defined Material Design 3 theme:

**Light Theme Colors:**
- Primary: Deep blue (`#1A237E`)
- Secondary: Deep teal (`#004D40`)
- Tertiary: Deep purple (`#4527A0`)
- Background: Light gray (`#F5F5F5`)
- Surface: White

**Dark Theme Colors:**
- Primary: Soft blue (`#6B8AFE`)
- Secondary: Teal accent (`#03DAC6`)
- Tertiary: Soft purple (`#B39DDB`)
- Background: Dark (`#121212`)
- Surface: Slightly lighter dark (`#1E1E1E`)

### Onboarding Current State

Based on code review, `OnboardingPage.kt` already uses MaterialTheme colors:
- Title uses `MaterialTheme.colorScheme.onSurface`
- Message uses `MaterialTheme.colorScheme.onSurface`
- Button uses default Material3 Button styling

**Potential issues to investigate:**
- Background colors in parent containers
- Icon tinting or filtering
- Page indicator colors (if implemented)
- Any custom composables used in onboarding pages

### Splash Screen Design

- **Icon**: New app icon (centered)
- **Background**: `MaterialTheme.colorScheme.primary` (adaptive to theme)
- **Icon size**: 288dp (Android 12+ recommendation)
- **Brand image duration**: Until app ready (~1-2 seconds max)

### App Icon Specifications

- **Adaptive Icon**:
  - Foreground: New icon graphic
  - Background: Solid color or simple gradient
  - Safe zone: 66dp diameter circle for important content

- **Sizes**: Provide all density variants
  - mdpi: 48x48
  - hdpi: 72x72
  - xhdpi: 96x96
  - xxhdpi: 144x144
  - xxxhdpi: 192x192

## Technical Considerations

### Dependencies

**Splash Screen API:**
```kotlin
implementation "androidx.core:core-splashscreen:1.0.1"
```

### Implementation Files

**To Modify:**
1. `app/src/main/res/values/themes.xml` - Add splash screen theme
2. `app/src/main/java/com/cihatakyol/sleeptimer/MainActivity.kt` - Install splash screen
3. `app/src/main/res/mipmap-*/` - Replace icon files
4. `app/src/main/res/drawable/ic_launcher_*` - Update adaptive icon
5. `app/src/main/java/com/cihatakyol/sleeptimer/ui/screens/onboarding/` - Fix any color issues
6. Other screen files as identified in audit

### Theme Color Usage Guidelines

When fixing colors, follow these patterns:

**Text Colors:**
```kotlin
// Text on surface
color = MaterialTheme.colorScheme.onSurface

// Text on primary color background
color = MaterialTheme.colorScheme.onPrimary

// Text on background
color = MaterialTheme.colorScheme.onBackground

// Secondary/less emphasized text
color = MaterialTheme.colorScheme.onSurfaceVariant
```

**Background Colors:**
```kotlin
// Main background
backgroundColor = MaterialTheme.colorScheme.background

// Card/elevated surface
backgroundColor = MaterialTheme.colorScheme.surface

// Surface variant (subtle elevation)
backgroundColor = MaterialTheme.colorScheme.surfaceVariant
```

**Interactive Elements:**
```kotlin
// Primary button - use Material3 Button (already themed)
Button(onClick = {}) { Text("Label") }

// Custom buttons/containers
containerColor = MaterialTheme.colorScheme.primary
contentColor = MaterialTheme.colorScheme.onPrimary
```

### Testing Checklist

**Splash Screen:**
- [ ] Cold start shows splash screen
- [ ] Splash screen uses new icon
- [ ] Background color matches theme
- [ ] Smooth transition to main content
- [ ] Works on Android 12+
- [ ] Works on Android 11 and below
- [ ] Light mode looks good
- [ ] Dark mode looks good

**App Icon:**
- [ ] New icon visible on home screen
- [ ] Icon visible in app drawer
- [ ] Icon visible in recent apps
- [ ] Icon visible in Settings → Apps
- [ ] Round icon variant works correctly
- [ ] Adaptive icon animates properly on supported launchers

**Onboarding:**
- [ ] All text readable in light mode
- [ ] All text readable in dark mode
- [ ] Buttons visible and accessible
- [ ] Page transitions work smoothly
- [ ] Images/icons display correctly
- [ ] No hardcoded colors remain

**Main App:**
- [ ] All screens respect theme colors
- [ ] Status bar color matches theme
- [ ] Navigation bar color matches theme
- [ ] Dialogs use theme colors
- [ ] Dynamic colors work on Android 12+ (if enabled)

## Success Metrics

### Qualitative Metrics
1. **Visual Consistency**: App looks cohesive with consistent color usage across all screens
2. **Professional Appearance**: Updated icon and splash screen create polished first impression
3. **Theme Compliance**: No screens use hardcoded colors that break theme consistency
4. **Accessibility**: Proper contrast ratios maintained in both light and dark modes

### Quantitative Metrics
1. **Code Quality**: Zero hardcoded color values in UI components (except theme definition files)
2. **Device Coverage**: Splash screen and icon work correctly on 100% of target Android versions
3. **Theme Modes**: Both light and dark modes are fully functional with no visual issues
4. **Performance**: Splash screen dismisses within 2 seconds of app being ready

### Validation Criteria
1. **Color Audit**: Pass automated lint check for hardcoded colors in Composables
2. **Visual Regression**: Before/after screenshots show clear improvement in consistency
3. **Accessibility**: Pass Android accessibility scanner with no contrast issues
4. **User Testing**: Internal team confirms improved visual polish and consistency

## Open Questions

1. **Where is the new app icon located?**
   - Need path to the new icon files in the project
   - Need confirmation of which drawable/asset is the new icon

2. **Is there a preferred splash screen duration?**
   - How long should splash screen stay visible minimum?
   - Should we wait for specific initialization tasks?

3. **Are there specific screens with known color issues beyond onboarding?**
   - Should we prioritize certain screens in the audit?
   - Are there any screens that should explicitly NOT be changed?

4. **Should we update colors in AdMob ad containers?**
   - Do ad banners/interstitials need theme-aware backgrounds?
   - Should we adjust padding/spacing around ads to match theme?

5. **Dynamic color preference:**
   - Should dynamic colors be enabled by default?
   - Should we add a user preference to toggle dynamic colors?

6. **Icon variants:**
   - Do we need separate icons for different build variants (dev, staging, production)?
   - Or use the same new icon for all variants?

## Implementation Priority

### Phase 1: App Icon & Splash Screen (High Priority)
1. Identify and replace app icon assets
2. Implement SplashScreen API
3. Test across Android versions

### Phase 2: Onboarding Color Fixes (High Priority)
1. Audit onboarding screen files
2. Replace hardcoded colors with theme colors
3. Test in light and dark modes

### Phase 3: App-Wide Theme Audit (Medium Priority)
1. Audit main app screens
2. Document findings
3. Fix identified issues
4. Test comprehensively

### Phase 4: Documentation (Low Priority)
1. Add theme usage comments
2. Create usage pattern examples
3. Update developer documentation

---

**Document Version:** 1.0
**Created:** 2025-11-28
**Status:** Ready for Implementation
**Estimated Effort:** Medium (1-2 weeks)
