# PRD: AdMob Banner and App Open Ads Implementation

## Introduction/Overview

This feature adds monetization to the SleepTimer app through Google AdMob integration. The implementation includes:
1. **Banner Ad**: A persistent banner ad displayed at the bottom of the main timer screen
2. **App Open Ad**: Full-screen ads shown when users launch the app, with frequency capping (maximum once per hour)

**Problem:** The app currently has no monetization strategy, limiting revenue potential.

**Goal:** Implement non-intrusive ads that generate revenue while maintaining a good user experience. The ads should be implemented as reusable, copy-paste composables that can be easily integrated into any screen.

## Implementation Checklist (High-Level)

### Phase 1: Setup & Configuration
- [ ] Add AdMob SDK dependency to build.gradle.kts
- [ ] Configure build variants with ad unit IDs (test vs production)
- [ ] Add AdMob app ID to AndroidManifest.xml
- [ ] Initialize MobileAds SDK in SleepTimerApplication

### Phase 2: Banner Ad
- [ ] Update existing AdMobBanner.kt with build variant logic
- [ ] Remove hardcoded test ID from AdMobBanner.kt
- [ ] Add banner ad to MainScreen.kt (bottom position)
- [ ] Test banner ad displays correctly

### Phase 3: App Open Ad
- [ ] Create AppOpenAdManager.kt singleton class
- [ ] Implement frequency capping with SharedPreferences
- [ ] Add app open ad loading to SleepTimerApplication
- [ ] Add app open ad display logic to MainActivity
- [ ] Test app open ad frequency cap (1 hour)

### Phase 4: GitHub Actions & Secrets
- [ ] Create app open ad unit in AdMob console
- [ ] Add ADMOB_APP_OPEN_ID_PROD to GitHub secrets
- [ ] Update production-build.yml workflow to include app open ad ID
- [ ] Update dev-build.yml and staging-build.yml workflows if needed

### Phase 5: Testing & Validation
- [ ] Test banner ad in dev build (test ads)
- [ ] Test app open ad in dev build (test ads)
- [ ] Test frequency capping works correctly
- [ ] Test silent failure handling (airplane mode)
- [ ] Build production variant and verify correct ad IDs used

## Goals

1. **Revenue Generation**: Create a sustainable monetization stream through AdMob ads
2. **User Experience**: Implement ads that don't disrupt the core timer functionality
3. **Code Reusability**: Create composable components that can be easily copied and used in other screens
4. **Build Variant Support**: Automatically use test ad units in dev builds and production ad units in production builds
5. **Reliability**: Load ads immediately on app start to maximize fill rates
6. **Frequency Control**: Prevent app open ad fatigue by limiting to once per hour

## User Stories

### As a user:
- I want to see a banner ad at the bottom of the main screen that doesn't interfere with timer controls
- I want app open ads to appear when I launch the app, but not too frequently
- I want ads to load quickly without making the app feel sluggish
- I want the app to work normally even if ads fail to load

### As a developer:
- I want a reusable AdMobBanner composable I can copy-paste into any screen
- I want automatic test/production ad unit switching based on build variant
- I want ads to load immediately on app start for optimal performance
- I want silent failure handling so users aren't disrupted by ad loading issues

## Functional Requirements

### FR-1: AdMob SDK Integration

**FR-1.1**: Add Google Mobile Ads SDK dependency to `app/build.gradle.kts`
- [ ] Use version `23.0.0` or later (latest stable)
- [ ] Add dependency: `implementation("com.google.android.gms:play-services-ads:23.0.0")`

**FR-1.2**: Initialize Mobile Ads SDK in SleepTimerApplication class
- [ ] Call `MobileAds.initialize(context)` in `SleepTimerApplication.onCreate()`
- [ ] Initialize before any ad loading attempts
- [ ] Handle initialization asynchronously
- [ ] Note: Use existing `SleepTimerApplication.kt` class (Hilt-enabled)

**FR-1.3**: Add AdMob App ID to AndroidManifest.xml
- [ ] Use test app ID for dev builds: `ca-app-pub-3940256099942544~3347511713`
- [ ] Use production app ID from GitHub secrets: `${{ secrets.ADMOB_APP_ID_PROD }}`
- [ ] Add meta-data tag inside `<application>` tag
- [ ] Configure via `manifestPlaceholders` in build.gradle.kts

### FR-2: Banner Ad Implementation

**FR-2.1**: Update existing `AdMobBanner` composable component in `ui/components/AdMobBanner.kt`
- [ ] Component should be self-contained and copy-paste ready
- [ ] Accept optional `Modifier` parameter for positioning flexibility
- [ ] Use `AndroidView` to wrap native AdView
- [ ] Default size: `AdSize.BANNER` (320x50 dp)
- [ ] Note: File already exists, needs build variant logic added

**FR-2.2**: Implement automatic ad unit ID selection based on build variant
- [ ] Dev build: Use test banner ad unit `ca-app-pub-3940256099942544/6300978111`
- [ ] Production build: Use production banner ad unit from GitHub secrets: `${{ secrets.ADMOB_BANNER_ID_PROD }}`
- [ ] Use `BuildConfig.BANNER_AD_UNIT_ID` to access the ID
- [ ] Remove hardcoded test ID from current AdMobBanner.kt

**FR-2.3**: Add banner ad to main timer screen (MainScreen.kt)
- [ ] Position: Fixed at bottom of screen
- [ ] Always visible (not dismissible)
- [ ] Should not overlap with timer controls or buttons
- [ ] Use `Scaffold` with `bottomBar` parameter for positioning

**FR-2.4**: Configure banner ad loading
- [ ] Load ad immediately when component is composed
- [ ] Use `AdRequest.Builder().build()` for standard ad request
- [ ] No special targeting or configuration needed

**FR-2.5**: Handle banner ad lifecycle
- [ ] Ad should reload automatically if screen is recomposed
- [ ] Clean up ad resources when component is disposed
- [ ] No error UI shown if ad fails to load (silent failure)

### FR-3: App Open Ad Implementation

**FR-3.1**: Create `AppOpenAdManager` singleton class in `ads/AppOpenAdManager.kt`
- [ ] Manage app open ad loading and showing
- [ ] Track last ad show time for frequency capping
- [ ] Handle ad preloading on app start
- [ ] Make it a singleton using `object` keyword

**FR-3.2**: Implement ad unit ID selection for app open ads
- [ ] Dev build: Use test app open ad unit `ca-app-pub-3940256099942544/9257395921`
- [ ] Production build: Create new GitHub secret `ADMOB_APP_OPEN_ID_PROD` (NOT yet configured)
- [ ] Use `BuildConfig.APP_OPEN_AD_UNIT_ID` to access the ID
- [ ] Note: App open ad ID is NOT in current GitHub secrets, needs to be added

**FR-3.3**: Implement frequency capping logic using SharedPreferences
- [ ] Maximum frequency: Once per hour (3600000 milliseconds)
- [ ] Store last ad show timestamp in SharedPreferences
- [ ] Key name: `last_app_open_ad_timestamp`
- [ ] Check elapsed time before showing ad
- [ ] If less than 1 hour since last show, skip ad display

**FR-3.4**: Load app open ad on app start
- [ ] Preload ad in `SleepTimerApplication.onCreate()`
- [ ] Load immediately after MobileAds SDK initialization
- [ ] Keep ad loaded and ready to show
- [ ] Call `AppOpenAdManager.loadAd(context)` from Application

**FR-3.5**: Show app open ad on cold start
- [ ] Trigger on app launch (cold start only, not on every resume)
- [ ] Check frequency cap before showing
- [ ] Show ad only if frequency cap allows and ad is loaded
- [ ] If ad not loaded or frequency cap exceeded, skip silently
- [ ] Call from MainActivity.onCreate()

**FR-3.6**: Handle app open ad lifecycle
- [ ] Reload new ad after current ad is dismissed
- [ ] Preload next ad in background for future launches
- [ ] Track loading state (loading, loaded, failed)
- [ ] Implement FullScreenContentCallback for ad events

**FR-3.7**: Implement activity lifecycle observation
- [ ] Use `ActivityLifecycleCallbacks` registered in SleepTimerApplication
- [ ] Show ad when app comes to foreground (if frequency allows)
- [ ] Don't show ad when switching between app activities
- [ ] Track whether app is in foreground or background

### FR-4: Build Variant Configuration

**FR-4.1**: Configure dev build variant in `app/build.gradle.kts`
- [ ] Build flavor: `dev` (already exists in project)
- [ ] Automatically use test ad unit IDs via buildConfigField
- [ ] Should work without any manual configuration changes
- [ ] Test IDs already configured in GitHub workflows

**FR-4.2**: Configure production build variant in `app/build.gradle.kts`
- [ ] Build flavor: `production` (already exists in project)
- [ ] Automatically use production ad unit IDs from environment variables
- [ ] Read from `ADMOB_APP_ID_PROD`, `ADMOB_BANNER_ID_PROD` environment variables
- [ ] Add new `ADMOB_APP_OPEN_ID_PROD` to GitHub secrets

**FR-4.3**: Create build config fields for ad unit IDs
- [ ] Add `buildConfigField` in `build.gradle.kts` for banner ad unit ID
- [ ] Add `buildConfigField` for app open ad unit ID
- [ ] Add `manifestPlaceholders` for AdMob app ID
- [ ] Access via `BuildConfig.BANNER_AD_UNIT_ID` and `BuildConfig.APP_OPEN_AD_UNIT_ID`
- [ ] No hardcoded ad unit IDs in Kotlin code
- [ ] Follow existing pattern from GitHub Actions workflow

### FR-5: Error Handling and Edge Cases

**FR-5.1**: Silent failure for ad loading errors
- [ ] If banner ad fails to load, show empty space (or hide banner container)
- [ ] No error messages shown to user
- [ ] Log errors to Logcat for debugging using `Log.e()`
- [ ] Use AdListener to detect failures

**FR-5.2**: Silent failure for app open ad errors
- [ ] If app open ad fails to load, continue app launch normally
- [ ] No error messages or loading delays
- [ ] Log errors to Logcat for debugging
- [ ] Use OnAdLoadedCallback to detect failures

**FR-5.3**: Handle no internet connection
- [ ] Banner ad: Show empty space if no internet
- [ ] App open ad: Skip if no internet, don't delay app launch
- [ ] No error messages to user
- [ ] Ads will retry automatically on reconnection

**FR-5.4**: Handle ad inventory issues
- [ ] If no ad inventory available, fail silently
- [ ] Don't block user from using the app
- [ ] Continue normal app operation
- [ ] Log "no fill" errors for monitoring

**FR-5.5**: Handle SDK initialization failures
- [ ] If MobileAds SDK fails to initialize, continue app operation
- [ ] Log initialization errors to Logcat
- [ ] Ads won't show but app remains functional
- [ ] Use OnInitializationCompleteListener to track status

## Non-Goals (Out of Scope)

1. **Interstitial Ads**: Not included in this implementation (may be added later)
2. **Rewarded Ads**: Not included in this implementation
3. **Native Ads**: Not using native ad format, only banner and app open
4. **Ad Mediation**: Not implementing mediation with other ad networks
5. **Premium/Ad-Free Version**: No paid version or in-app purchase to remove ads
6. **Advanced Targeting**: No custom audience targeting or personalization
7. **Ad Analytics**: Beyond basic AdMob console metrics, no custom analytics
8. **A/B Testing**: No ad placement or format testing
9. **Manual Ad Refresh**: Banner ad uses default auto-refresh, no manual control
10. **Ad Customization**: No custom ad sizes or styling beyond AdMob defaults

## Design Considerations

### Banner Ad Layout

**Main Screen Layout:**
```
┌─────────────────────────┐
│   Main Timer Screen     │
│                         │
│   [Time Picker]         │
│   [Controls]            │
│                         │
│   (scrollable content)  │
│                         │
├─────────────────────────┤ ← Fixed position
│   [Banner Ad 320x50]    │
└─────────────────────────┘
```

**Implementation Approach:**
- Use `Scaffold` with `bottomBar` parameter for banner ad
- Or use `Box` with `Modifier.align(Alignment.BottomCenter)`
- Ensure banner doesn't overlap timer controls
- Add padding to main content to account for banner height

### App Open Ad Flow

**Launch Flow:**
```
1. User launches app
2. MainActivity.onCreate() called
3. Check: Has 1 hour passed since last ad?
   ├─ Yes: Show app open ad → On dismiss → Show main screen
   └─ No: Skip ad → Show main screen immediately
4. Preload next app open ad in background
```

**Frequency Cap Storage:**
- Use SharedPreferences or DataStore
- Key: `last_app_open_ad_timestamp`
- Value: Unix timestamp (milliseconds)
- Check: `currentTime - lastAdTime >= 3600000` (1 hour in ms)

### Composable Component Structure

**AdMobBanner Composable (Copy-Paste Ready):**
```kotlin
@Composable
fun AdMobBanner(
    modifier: Modifier = Modifier
) {
    val adUnitId = if (BuildConfig.DEBUG) {
        "ca-app-pub-3940256099942544/6300978111" // Test ID
    } else {
        BuildConfig.BANNER_AD_UNIT_ID // Production ID
    }

    AndroidView(
        modifier = modifier.fillMaxWidth().height(50.dp),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                this.adUnitId = adUnitId
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}
```

**Usage Example:**
```kotlin
Scaffold(
    bottomBar = { AdMobBanner() }
) {
    // Main screen content
}
```

## Technical Considerations

### Dependencies

**Required Gradle Dependency:**
```kotlin
implementation("com.google.android.gms:play-services-ads:23.0.0")
```

**AndroidManifest.xml Configuration:**
```xml
<manifest>
    <application>
        <!-- AdMob App ID -->
        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="${admobAppId}"/>
    </application>
</manifest>
```

**Build Config Fields (build.gradle.kts):**
```kotlin
buildTypes {
    debug {
        buildConfigField("String", "BANNER_AD_UNIT_ID", "\"ca-app-pub-3940256099942544/6300978111\"")
        buildConfigField("String", "APP_OPEN_AD_UNIT_ID", "\"ca-app-pub-3940256099942544/9257395921\"")
        manifestPlaceholders["admobAppId"] = "ca-app-pub-3940256099942544~3347511713"
    }
    release {
        buildConfigField("String", "BANNER_AD_UNIT_ID", "\"YOUR_PRODUCTION_BANNER_ID\"")
        buildConfigField("String", "APP_OPEN_AD_UNIT_ID", "\"YOUR_PRODUCTION_APP_OPEN_ID\"")
        manifestPlaceholders["admobAppId"] = "YOUR_PRODUCTION_APP_ID"
    }
}
```

### Implementation Files

**Files to Create:**
1. `AdMobBanner.kt` - Reusable banner ad composable component
2. `AppOpenAdManager.kt` - Singleton class managing app open ads
3. `MyApplication.kt` - Application class for SDK initialization (if not exists)

**Files to Modify:**
1. `app/build.gradle.kts` - Add dependencies and build config fields
2. `AndroidManifest.xml` - Add AdMob app ID meta-data
3. `MainActivity.kt` - Initialize SDK and show app open ad
4. `MainScreen.kt` - Add banner ad to bottom of screen

### Performance Considerations

**Ad Loading Performance:**
- Initialize MobileAds SDK on app start (adds ~100-300ms to startup time)
- Banner ad loading is asynchronous and non-blocking
- App open ad is preloaded, so showing it is instant
- No impact on timer functionality or user interactions

**Memory Considerations:**
- Banner ad: ~10-20 MB memory footprint
- App open ad (preloaded): ~15-30 MB memory footprint
- Total ad memory impact: ~25-50 MB
- Clean up ads properly to prevent memory leaks

**Network Usage:**
- Banner ad: ~50-200 KB per ad load
- App open ad: ~300-500 KB per ad load
- Auto-refresh banner: ~50-200 KB every 30-60 seconds
- Consider user data usage, but ads are optimized by Google

### Testing Strategy

**Development Testing:**
1. Use test ad unit IDs in dev builds to avoid policy violations
2. Verify ads load and display correctly
3. Test frequency capping for app open ads
4. Test ad loading without internet connection
5. Verify silent failure handling

**Production Testing:**
1. Before release, verify production ad unit IDs are correct
2. Test on multiple devices and Android versions
3. Monitor AdMob console for ad requests and impressions
4. Verify ads don't overlap with app content
5. Check app performance with ads enabled

**Test Checklist:**
- [ ] Banner ad appears at bottom of main screen
- [ ] Banner ad doesn't overlap timer controls
- [ ] App open ad shows on app launch (first time)
- [ ] App open ad doesn't show again within 1 hour
- [ ] App open ad shows again after 1 hour
- [ ] App works normally if ads fail to load
- [ ] No error messages shown to user for ad failures
- [ ] Test ad IDs used in debug builds
- [ ] Production ad IDs used in release builds
- [ ] MobileAds SDK initializes successfully

## Success Metrics

### Quantitative Metrics

1. **Ad Impression Rate**:
   - Target: >80% of app sessions show at least one ad
   - Measure via AdMob console analytics

2. **Fill Rate**:
   - Target: >90% ad request fill rate
   - Measure: (Ad impressions / Ad requests) × 100

3. **App Open Ad Frequency**:
   - Target: Maximum 1 ad per user per hour
   - Measure via custom analytics or logs

4. **Revenue**:
   - Target: Generate measurable revenue from ad impressions
   - Measure via AdMob earnings report

5. **App Performance**:
   - Target: No significant increase in app startup time (<500ms delay)
   - Measure: Firebase Performance Monitoring or manual testing

### Qualitative Metrics

1. **User Experience**:
   - Ads don't interfere with core timer functionality
   - No user complaints about intrusive ads
   - App remains responsive and fast

2. **Code Quality**:
   - AdMobBanner composable is truly copy-paste ready
   - Code is clean and well-documented
   - Build variant switching works seamlessly

3. **Reliability**:
   - App works normally even when ads fail
   - No crashes related to ad loading
   - Silent failure handling works as expected

### Validation Criteria

1. **Functional Testing**: All functional requirements pass manual testing
2. **Build Variants**: Dev and production builds use correct ad unit IDs automatically
3. **Frequency Cap**: App open ad respects 1-hour minimum interval
4. **Silent Failure**: App continues normally when ads fail to load
5. **AdMob Console**: Ad requests and impressions visible in AdMob dashboard

## GitHub Secrets Configuration

### Existing Secrets (Already Configured)
- ✅ `ADMOB_APP_ID_PROD` - AdMob application ID for production builds
- ✅ `ADMOB_BANNER_ID_PROD` - Banner ad unit ID for production builds
- ✅ `ADMOB_INTERSTITIAL_ID_PROD` - Interstitial ad unit ID (not used in this PRD, for future use)

### New Secrets Required
- ⚠️ `ADMOB_APP_OPEN_ID_PROD` - **NEEDS TO BE ADDED** - App open ad unit ID for production builds

### GitHub Actions Integration
The production build workflow (`.github/workflows/production-build.yml`) already:
- Reads AdMob secrets as environment variables
- Passes them to Gradle build process
- Needs to be updated to include `ADMOB_APP_OPEN_ID_PROD`

**Action Required**: Add `ADMOB_APP_OPEN_ID_PROD` to GitHub repository secrets before production release.

## Open Questions

1. **App Open Ad Unit ID**: ⚠️ **ACTION REQUIRED**
   - Need to create app open ad unit in AdMob console
   - Need to add `ADMOB_APP_OPEN_ID_PROD` to GitHub secrets
   - Can use test ID during development, but production ID needed before release

2. **GDPR/Privacy Compliance**: Should we implement consent management for EU users?
   - Consider Google UMP SDK for consent
   - May be required for EU traffic
   - Impact on ad personalization

3. **Ad Placement Feedback**: After initial implementation, should we gather user feedback on ad placement?
   - Consider A/B testing different banner positions
   - Monitor user retention impact

4. **Future Ad Formats**: Are there plans to add interstitial or rewarded ads later?
   - Should architecture support future ad types?
   - Plan for extensibility

5. **Analytics Integration**: Should ad events be tracked in Firebase Analytics?
   - Track ad impressions as custom events
   - Monitor correlation with user engagement

6. **Premium Version**: Is a future ad-free premium version planned?
   - Should we architect for easy premium check?
   - Plan in-app billing integration

7. **Frequency Cap Adjustment**: Should the 1-hour frequency cap be configurable?
   - Consider Firebase Remote Config for dynamic adjustment
   - A/B test different frequency caps

---

**Document Version:** 1.0
**Created:** 2025-11-28
**Status:** Ready for Implementation
**Estimated Effort:** Small-Medium (3-5 days)
**Priority:** Medium
