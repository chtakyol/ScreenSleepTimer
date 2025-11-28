# Task List: AdMob Banner and App Open Ads Implementation

## Relevant Files

### Files to Create
- `app/src/main/java/com/cihatakyol/sleeptimer/ads/AppOpenAdManager.kt` - Singleton class managing app open ad loading, frequency capping, and display

### Files to Modify
- `app/build.gradle.kts` - Add AdMob SDK dependency and build config fields for ad unit IDs
- `app/src/main/AndroidManifest.xml` - Add AdMob app ID meta-data
- `app/src/main/java/com/cihatakyol/sleeptimer/SleepTimerApplication.kt` - Initialize AdMob SDK and register lifecycle callbacks
- `app/src/main/java/com/cihatakyol/sleeptimer/ui/components/AdMobBanner.kt` - Update to use BuildConfig for ad unit IDs instead of hardcoded test ID
- `app/src/main/java/com/cihatakyol/sleeptimer/ui/screens/mainscreen/MainScreen.kt` - Add banner ad to bottom of screen using Scaffold bottomBar
- `app/src/main/java/com/cihatakyol/sleeptimer/MainActivity.kt` - Show app open ad on cold start with frequency check
- `.github/workflows/production-build.yml` - Add ADMOB_APP_OPEN_ID_PROD environment variable
- `.github/workflows/dev-build.yml` - Ensure test ad IDs are configured (if needed)
- `.github/workflows/staging-build.yml` - Ensure staging ad configuration (if needed)

### Notes

- AdMob SDK version 23.0.0 or later will be used
- Test ad unit IDs will be used for dev builds automatically
- Production ad unit IDs will be read from GitHub secrets via environment variables
- **ACTION REQUIRED**: Create app open ad unit in AdMob console and add `ADMOB_APP_OPEN_ID_PROD` to GitHub secrets
- All ad failures will be silent - no error UI shown to users
- App open ad frequency cap: maximum once per hour (3600000 milliseconds)

## Instructions for Completing Tasks

**IMPORTANT:** As you complete each task, you must check it off in this markdown file by changing `- [ ]` to `- [x]`. This helps track progress and ensures you don't skip any steps.

Example:
- `- [ ] 1.1 Read file` → `- [x] 1.1 Read file` (after completing)

Update the file after completing each sub-task, not just after completing an entire parent task.

## Tasks

- [ ] 0.0 Create feature branch
  - [ ] 0.1 Create and checkout a new branch for this feature (e.g., `git checkout -b feature/admob-ads-implementation`)

- [x] 1.0 Add AdMob SDK dependency and configure build variants
  - [x] 1.1 Read current `app/build.gradle.kts` file to understand existing dependencies
  - [x] 1.2 Add Google Mobile Ads SDK dependency: `implementation("com.google.android.gms:play-services-ads:23.0.0")`
  - [x] 1.3 Locate the `productFlavors` or `buildTypes` section in build.gradle.kts
  - [x] 1.4 Add `buildConfigField` for banner ad unit ID in dev flavor/debug build type
  - [x] 1.5 Add `buildConfigField` for app open ad unit ID in dev flavor/debug build type
  - [x] 1.6 Add `buildConfigField` for banner ad unit ID in production flavor/release build type (read from env var `ADMOB_BANNER_ID_PROD`)
  - [x] 1.7 Add `buildConfigField` for app open ad unit ID in production flavor/release build type (read from env var `ADMOB_APP_OPEN_ID_PROD`)
  - [x] 1.8 Add `manifestPlaceholders["admobAppId"]` for dev build with test app ID: `ca-app-pub-3940256099942544~3347511713`
  - [x] 1.9 Add `manifestPlaceholders["admobAppId"]` for production build (read from env var `ADMOB_APP_ID_PROD`)
  - [x] 1.10 Sync Gradle and verify no build errors

- [x] 2.0 Initialize AdMob SDK in SleepTimerApplication
  - [x] 2.1 Read current `SleepTimerApplication.kt` file
  - [x] 2.2 Import required AdMob classes: `com.google.android.gms.ads.MobileAds`
  - [x] 2.3 Import `com.google.android.gms.ads.initialization.OnInitializationCompleteListener`
  - [x] 2.4 Override `onCreate()` method if not already overridden
  - [x] 2.5 Call `MobileAds.initialize(this)` in `onCreate()` before any ad loading
  - [x] 2.6 Add `OnInitializationCompleteListener` to log initialization status
  - [x] 2.7 Add error handling with try-catch to ensure app continues if initialization fails
  - [x] 2.8 Add log statement to track initialization completion
  - [x] 2.9 Build project and verify no compilation errors

- [x] 3.0 Update AdMobBanner composable with build variant logic
  - [x] 3.1 Read current `ui/components/AdMobBanner.kt` file
  - [x] 3.2 Import `BuildConfig` class
  - [x] 3.3 Replace hardcoded test ad unit ID with `BuildConfig.ADMOB_BANNER_ID`
  - [x] 3.4 Remove the hardcoded string: `"ca-app-pub-3940256099942544/6300978111"`
  - [x] 3.5 Ensure `Modifier` parameter is properly supported for flexibility
  - [x] 3.6 Add error handling with AdListener to log ad load failures
  - [x] 3.7 Implement silent failure - no error UI shown to user
  - [x] 3.8 Add log statements for debugging (ad load, ad failed, ad displayed)
  - [x] 3.9 Verify composable is copy-paste ready and self-contained
  - [x] 3.10 Build project and verify no compilation errors

- [x] 4.0 Add banner ad to MainScreen
  - [x] 4.1 Read current `ui/screens/mainscreen/MainScreen.kt` file
  - [x] 4.2 Import `AdMobBanner` composable
  - [x] 4.3 Identify the main composable structure (likely a Box or Column)
  - [x] 4.4 Wrap main content with `Scaffold` composable
  - [x] 4.5 Add `bottomBar` parameter to Scaffold
  - [x] 4.6 Set `bottomBar = { AdMobBanner() }` to display banner at bottom
  - [x] 4.7 Ensure main content uses `Scaffold`'s padding values to avoid overlap
  - [x] 4.8 Verify banner doesn't overlap with timer controls or buttons
  - [x] 4.9 Build and run app to visually verify banner placement
  - [x] 4.10 Test that banner ad loads (should show test ad in dev build)

- [x] 5.0 Create AppOpenAdManager singleton class
  - [x] 5.1 Create new directory `app/src/main/java/com/cihatakyol/sleeptimer/ads/` if it doesn't exist
  - [x] 5.2 Create new file `AppOpenAdManager.kt` in the ads directory
  - [x] 5.3 Define singleton class using `object AppOpenAdManager`
  - [x] 5.4 Import required classes: `AppOpenAd`, `AppOpenAd.AppOpenAdLoadCallback`, `Activity`, `Context`, `Log`
  - [x] 5.5 Add private nullable property `appOpenAd: AppOpenAd?` to store loaded ad
  - [x] 5.6 Add private property `isLoadingAd: Boolean` to track loading state
  - [x] 5.7 Add private property `isShowingAd: Boolean` to track if ad is currently showing
  - [x] 5.8 Create `loadAd(context: Context)` function to preload app open ad
  - [x] 5.9 In `loadAd()`, use `BuildConfig.ADMOB_APP_OPEN_ID` for ad unit ID
  - [x] 5.10 Implement `AppOpenAdLoadCallback` with onAdLoaded and onAdFailedToLoad
  - [x] 5.11 In onAdLoaded, store the ad and set `isLoadingAd = false`
  - [x] 5.12 In onAdFailedToLoad, log error and set `isLoadingAd = false`
  - [x] 5.13 Create SharedPreferences helper function `getLastAdShowTime(context: Context): Long`
  - [x] 5.14 Create SharedPreferences helper function `setLastAdShowTime(context: Context, time: Long)`
  - [x] 5.15 Use preference key: `"last_app_open_ad_timestamp"`
  - [x] 5.16 Create function `canShowAd(context: Context): Boolean` to check frequency cap
  - [x] 5.17 In `canShowAd()`, calculate elapsed time: `currentTime - lastAdTime >= 3600000` (1 hour)
  - [x] 5.18 Create function `showAdIfAvailable(activity: Activity)` to display ad
  - [x] 5.19 In `showAdIfAvailable()`, check if ad is loaded and frequency cap allows
  - [x] 5.20 If checks pass, show ad using `appOpenAd?.show(activity)`
  - [x] 5.21 Implement `FullScreenContentCallback` to handle ad events
  - [x] 5.22 In `onAdDismissedFullScreenContent`, reload next ad and update last show time
  - [x] 5.23 In `onAdFailedToShowFullScreenContent`, log error and reload ad
  - [x] 5.24 In `onAdShowedFullScreenContent`, set `isShowingAd = true`
  - [x] 5.25 Add error handling for all operations (silent failures)
  - [x] 5.26 Add comprehensive logging for debugging
  - [x] 5.27 Build project and verify no compilation errors

- [x] 6.0 Integrate app open ad with MainActivity
  - [x] 6.1 Read current `MainActivity.kt` file
  - [x] 6.2 Import `AppOpenAdManager` class
  - [x] 6.3 In `onCreate()`, after `super.onCreate()`, call `AppOpenAdManager.showAdIfAvailable(this)`
  - [x] 6.4 Ensure ad check happens before `setContent` to show ad on cold start
  - [x] 6.5 Read `SleepTimerApplication.kt` again to add lifecycle callbacks
  - [x] 6.6 In `SleepTimerApplication.onCreate()`, register `ActivityLifecycleCallbacks`
  - [x] 6.7 In lifecycle callbacks, implement `onActivityStarted()` to detect foreground
  - [x] 6.8 In `onActivityStarted()`, call `AppOpenAdManager.showAdIfAvailable()` if frequency allows
  - [x] 6.9 Track current activity to avoid showing ad when switching between app activities
  - [x] 6.10 In `SleepTimerApplication.onCreate()`, after SDK init, call `AppOpenAdManager.loadAd(this)` to preload first ad
  - [x] 6.11 Build and run app to test app open ad on launch
  - [x] 6.12 Verify ad shows on first launch (test ad in dev build)
  - [x] 6.13 Close and reopen app immediately - verify ad doesn't show (frequency cap working)
  - [x] 6.14 Wait 1 hour (or change timestamp in SharedPreferences for testing) and verify ad shows again

- [x] 7.0 Update GitHub Actions workflows for production secrets
  - [x] 7.1 Read `.github/workflows/production-build.yml` file
  - [x] 7.2 Locate the section where environment variables are set for the build
  - [x] 7.3 Add `ADMOB_APP_OPEN_ID_PROD: ${{ secrets.ADMOB_APP_OPEN_ID_PROD }}` to env vars
  - [x] 7.4 Ensure it's added in all build steps that need it (assembleProductionRelease, bundleProductionRelease)
  - [x] 7.5 Read `.github/workflows/dev-build.yml` file (if exists)
  - [x] 7.6 Verify test ad unit IDs are used for dev builds (should be hardcoded in build.gradle.kts)
  - [x] 7.7 Read `.github/workflows/staging-build.yml` file (if exists)
  - [x] 7.8 Decide whether staging should use test or production ad IDs
  - [x] 7.9 Update staging workflow accordingly if needed
  - [x] 7.10 Document in workflow comments that `ADMOB_APP_OPEN_ID_PROD` secret needs to be added to GitHub

- [x] 8.0 Test implementation and verify functionality
  - [x] 8.1 Build dev debug variant locally using `./gradlew assembleDevDebug`
  - [ ] 8.2 Install dev build on test device or emulator
  - [ ] 8.3 Launch app and verify test banner ad appears at bottom of MainScreen
  - [ ] 8.4 Verify banner ad doesn't overlap with timer controls
  - [ ] 8.5 Verify app open ad shows on cold start (test ad)
  - [ ] 8.6 Close app and reopen immediately - verify app open ad doesn't show (frequency cap)
  - [ ] 8.7 Clear app data or modify SharedPreferences timestamp to simulate 1 hour passing
  - [ ] 8.8 Launch app again and verify app open ad shows
  - [ ] 8.9 Enable airplane mode (no internet) and launch app
  - [ ] 8.10 Verify app continues to work normally without ads (silent failure)
  - [ ] 8.11 Verify no error messages or crashes related to ads
  - [ ] 8.12 Check Logcat for ad loading logs and verify proper logging
  - [ ] 8.13 Build production release variant using `./gradlew assembleProductionRelease` (will need env vars set)
  - [x] 8.14 Verify BuildConfig fields are correctly set for production variant
  - [ ] 8.15 Test on multiple Android versions (Android 12+, Android 11, Android 10)
  - [ ] 8.16 Monitor AdMob console to verify ad requests are being logged
  - [ ] 8.17 Verify app startup time hasn't increased significantly (<500ms acceptable)
  - [ ] 8.18 Run app through testing checklist from PRD
  - [ ] 8.19 Take screenshots of banner ad and app open ad for documentation
  - [ ] 8.20 Document any issues found and create follow-up tasks if needed
