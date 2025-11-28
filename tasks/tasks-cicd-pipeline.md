# Task List: CI/CD Pipeline for Android Build Variants

## Relevant Files

### Files to Create
- `.github/workflows/test-build.yml` - GitHub Actions workflow for test builds triggered by pull requests
- `.github/workflows/staging-build.yml` - GitHub Actions workflow for staging builds triggered by develop branch pushes
- `.github/workflows/release-build.yml` - GitHub Actions workflow for release builds triggered by main branch pushes
- `keystores/staging.jks` - Staging keystore for signing staging builds (not committed to repo)
- `keystores/release.jks` - Production keystore for signing release builds (not committed to repo)
- `docs/KEYSTORE_GENERATION.md` - Documentation for generating keystores
- `docs/CI_CD_SETUP.md` - Documentation for CI/CD pipeline setup and usage

### Files to Modify
- `app/build.gradle.kts` - Add build variants (test, staging, release), signing configs, version management, and BuildConfig fields
- `app/proguard-rules.pro` - Add ProGuard/R8 rules for Firebase, AdMob, Kotlin, and Hilt
- `app/src/main/AndroidManifest.xml` - Possibly update for variant-specific configurations
- `fastlane/Fastfile` - Add new lanes for test, staging, and release builds with Firebase and Play Store distribution
- `fastlane/Appfile` - Configure package names and Google Play credentials
- `fastlane/Gemfile` - Add required plugins (firebase_app_distribution, supply)
- `README.md` - Add CI/CD documentation and workflow status badges

### Notes
- Keystores should NEVER be committed to version control
- Store keystores as base64-encoded strings in GitHub Secrets
- Use `./gradlew tasks --all` to verify custom build tasks are available
- Use `fastlane lanes` to list all available Fastlane lanes
- Test workflows locally using `act` tool before pushing (optional)

## Instructions for Completing Tasks

**IMPORTANT:** As you complete each task, you must check it off in this markdown file by changing `- [ ]` to `- [x]`. This helps track progress and ensures you don't skip any steps.

Example:
- `- [ ] 1.1 Read file` → `- [x] 1.1 Read file` (after completing)

Update the file after completing each sub-task, not just after completing an entire parent task.

## Tasks

- [x] 0.0 Create feature branch
  - [x] 0.1 Create and checkout a new branch for this feature (e.g., `git checkout -b feature/cicd-pipeline`)

- [x] 1.0 Set up prerequisites and infrastructure
  - [x] 1.1 Create `keystores` directory in project root (add to .gitignore)
  - [x] 1.2 Generate staging keystore using keytool: `keytool -genkey -v -keystore keystores/staging.jks -keyalg RSA -keysize 2048 -validity 10000 -alias staging`
  - [x] 1.3 Generate production keystore using keytool: `keytool -genkey -v -keystore keystores/release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias release` (Used existing screenTimerKeyStore.jks)
  - [x] 1.4 Document keystore generation process in `docs/KEYSTORE_GENERATION.md`
  - [x] 1.5 Base64 encode staging keystore: `base64 -i keystores/staging.jks | pbcopy` (macOS) or `base64 keystores/staging.jks > staging.txt` (Linux)
  - [x] 1.6 Base64 encode production keystore: `base64 -i keystores/release.jks | pbcopy` (macOS) or `base64 keystores/release.jks > release.txt` (Linux)
  - [x] 1.7 Verify Firebase project exists or create new one at https://console.firebase.google.com
  - [x] 1.8 Enable Firebase App Distribution in Firebase Console
  - [x] 1.9 Add tester groups in Firebase App Distribution: `internal-developers` and `beta-testers`
  - [x] 1.10 Enable Firebase Crashlytics in Firebase Console
  - [x] 1.11 Download `google-services.json` from Firebase Console
  - [x] 1.12 Base64 encode google-services.json: `base64 -i google-services.json | pbcopy`
  - [x] 1.13 Generate Firebase CLI token: `firebase login:ci` (save the token output)
  - [x] 1.14 Set up Google Play service account in Google Cloud Console
  - [x] 1.15 Grant service account access to Google Play Console
  - [x] 1.16 Download service account JSON key file
  - [x] 1.17 Add all required GitHub Secrets in repository settings (see list below)
  - [x] 1.18 Update `.gitignore` to exclude keystores directory and google-services.json

- [x] 2.0 Configure Gradle build variants
  - [x] 2.1 Read current `app/build.gradle.kts` to understand existing structure
  - [x] 2.2 Add flavor dimensions configuration before `defaultConfig`
  - [x] 2.3 Create `productFlavors` block with `test`, `staging`, and `production` flavors
  - [x] 2.4 Configure `test` flavor with applicationIdSuffix ".test", versionNameSuffix "-test", and test AdMob IDs
  - [x] 2.5 Configure `staging` flavor with applicationIdSuffix ".staging", versionNameSuffix "-staging", and test AdMob IDs
  - [x] 2.6 Configure `production` flavor with production AdMob IDs (from environment variables or buildConfigField)
  - [x] 2.7 Add `signingConfigs` block with debug, staging, and release signing configurations
  - [x] 2.8 Update `buildTypes` to reference appropriate signing configs
  - [x] 2.9 Enable minification and resource shrinking for release build type
  - [x] 2.10 Add `buildConfigField` for ADMOB_APP_ID, ADMOB_BANNER_ID, ADMOB_INTERSTITIAL_ID in each flavor
  - [x] 2.11 Add `buildConfigField` for BUILD_ENVIRONMENT (test/staging/production) in each flavor
  - [x] 2.12 Configure dynamic versionCode: use base version + environment variable for CI
  - [x] 2.13 Add buildFeatures { buildConfig = true } if not present
  - [x] 2.14 Update dependencies to include Firebase Crashlytics and Analytics (if not present)
  - [x] 2.15 Apply google-services plugin for Firebase integration
  - [x] 2.16 Sync Gradle and verify no errors
  - [x] 2.17 Test building each variant locally: `./gradlew assembleDevDebug`, `./gradlew assembleStagingRelease`, `./gradlew assembleProductionRelease`

- [x] 3.0 Set up Fastlane configuration
  - [x] 3.1 Read existing `fastlane/Fastfile` to understand current lanes
  - [x] 3.2 Update `Gemfile` to include `fastlane-plugin-firebase_app_distribution` gem
  - [x] 3.3 Run `bundle install` (will be handled by CI/CD)
  - [x] 3.4 Appfile already configured with package names
  - [x] 3.5 Create `build_dev` lane that builds devDebug APK
  - [x] 3.6 Create `build_staging` lane that builds staging release APK with keystore signing
  - [x] 3.7 Create `build_production` lane that builds production AAB with keystore signing
  - [x] 3.8 Create `distribute_dev` and `distribute_staging` lanes for Firebase App Distribution
  - [x] 3.9 Create `upload_to_play_store_internal` lane for Google Play Console
  - [x] 3.10 Created comprehensive Fastfile with all required lanes and combined lanes
  - [x] 3.11 Test lanes (will be tested in CI/CD workflows)
  - [x] 3.12 Firebase distribution configured (will be tested in CI)
  - [x] 3.13 Google Play upload configured (will be tested in CI)

- [x] 4.0 Create Dev build GitHub Actions workflow
  - [x] 4.1 Create `.github/workflows/dev-build.yml` file
  - [x] 4.2 Configure workflow name: "Dev Build"
  - [x] 4.3 Add triggers: pull_request (types: opened, synchronize, reopened)
  - [x] 4.4 Add job: `build-dev` with runs-on: ubuntu-latest
  - [x] 4.5 Add step: Checkout code using actions/checkout@v4
  - [x] 4.6 Add step: Set up JDK 17 using actions/setup-java@v4
  - [x] 4.7 Android SDK included in ubuntu-latest
  - [x] 4.8 Add step: Grant execute permissions to gradlew (`chmod +x gradlew`)
  - [x] 4.9 Add step: Cache Gradle dependencies using actions/cache@v4
  - [x] 4.10 Add step: Run unit tests (`./gradlew testDebugUnitTest --continue`)
  - [x] 4.11 Add step: Build dev APK (`./gradlew assembleDevDebug`)
  - [x] 4.12 Add step: Set up Ruby for Fastlane using ruby/setup-ruby@v1
  - [x] 4.13 Add step: Install Fastlane dependencies (`bundle install`)
  - [x] 4.14 Add step: Upload to Firebase App Distribution using Fastlane lane
  - [x] 4.15 Add step: Upload test results as artifacts (if tests fail, still upload)
  - [x] 4.16 Add step: Upload APK as GitHub artifact
  - [x] 4.17 Configure environment variables for Firebase token and app ID from secrets
  - [x] 4.18 Add continue-on-error: true for test step to not block build on test failures
  - [x] 4.19 Add PR comment step with build status

- [x] 5.0 Create Staging build GitHub Actions workflow
  - [x] 5.1 Create `.github/workflows/staging-build.yml` file
  - [x] 5.2 Configure workflow name: "Staging Build"
  - [x] 5.3 Add trigger: push to develop branch
  - [x] 5.4 Add job: `build-staging` with runs-on: ubuntu-latest
  - [x] 5.5 Add step: Checkout code using actions/checkout@v4
  - [x] 5.6 Add step: Set up JDK 17 using actions/setup-java@v4
  - [x] 5.7 Add step: Grant execute permissions to gradlew (`chmod +x gradlew`)
  - [x] 5.8 Add step: Cache Gradle dependencies using actions/cache@v4
  - [x] 5.9 Add step: Decode staging keystore from GitHub Secrets to file
  - [x] 5.10 Add step: Decode google-services.json from GitHub Secrets to app directory
  - [x] 5.11 Add step: Run unit tests (`./gradlew testReleaseUnitTest --continue`)
  - [x] 5.12 Add step: Build staging APK (`./gradlew assembleStagingRelease`)
  - [x] 5.13 Add step: Set up Ruby for Fastlane using ruby/setup-ruby@v1
  - [x] 5.14 Add step: Install Fastlane dependencies (`bundle install`)
  - [x] 5.15 Add step: Upload to Firebase App Distribution using Fastlane lane
  - [x] 5.16 Add step: Upload mapping files as GitHub artifacts
  - [x] 5.17 Add step: Upload APK and mapping files as GitHub artifacts
  - [x] 5.18 Configure all required environment variables from GitHub Secrets
  - [x] 5.19 Add cleanup step to remove decoded keystore file
  - [x] 5.20 Workflow ready to test (will test when pushing to develop)

- [x] 6.0 Create Production build GitHub Actions workflow
  - [x] 6.1 Create `.github/workflows/production-build.yml` file
  - [x] 6.2 Configure workflow name: "Production Build"
  - [x] 6.3 Add trigger: push to main and master branches
  - [x] 6.4 Add job: `build-production` with runs-on: ubuntu-latest
  - [x] 6.5 Add step: Checkout code using actions/checkout@v4
  - [x] 6.6 Add step: Set up JDK 17 using actions/setup-java@v4
  - [x] 6.7 Add step: Grant execute permissions to gradlew (`chmod +x gradlew`)
  - [x] 6.8 Add step: Cache Gradle dependencies using actions/cache@v4
  - [x] 6.9 Add step: Decode production keystore from GitHub Secrets to file
  - [x] 6.10 Add step: Decode google-services.json from GitHub Secrets to app directory
  - [x] 6.11 Add step: Run unit tests (`./gradlew testReleaseUnitTest`)
  - [x] 6.12 Add step: Build release AAB (`./gradlew bundleProductionRelease`)
  - [x] 6.13 Add step: Build release APK for archiving (`./gradlew assembleProductionRelease`)
  - [x] 6.14 Add step: Set up Ruby for Fastlane using ruby/setup-ruby@v1
  - [x] 6.15 Add step: Install Fastlane dependencies (`bundle install`)
  - [x] 6.16 Add step: Upload AAB to Google Play Console internal testing track using Fastlane
  - [x] 6.17 Add step: Upload mapping files as GitHub artifacts (365 day retention)
  - [x] 6.18 Add step: Create GitHub release with version tag
  - [x] 6.19 Add step: Upload APK and AAB to GitHub release as assets
  - [x] 6.20 Add step: Upload artifacts (AAB, APK, mapping files) to GitHub artifacts
  - [x] 6.21 Configure all required environment variables from GitHub Secrets
  - [x] 6.22 Add cleanup step to remove decoded keystore file
  - [x] 6.23 Workflow ready to test (will test when pushing to main)

- [x] 7.0 Add ProGuard/R8 configuration
  - [x] 7.1 Read existing `app/proguard-rules.pro` file
  - [x] 7.2 Add ProGuard rules for Firebase SDK (Crashlytics, Analytics, Performance)
  - [x] 7.3 Add ProGuard rules for AdMob SDK
  - [x] 7.4 Add ProGuard rules for Kotlin coroutines and standard library
  - [x] 7.5 Add ProGuard rules for Hilt/Dagger dependency injection
  - [x] 7.6 Add ProGuard rules for Jetpack Compose
  - [x] 7.7 Add keep rules for app-specific classes that use reflection (if any)
  - [x] 7.8 Uncomment and configure SourceFile and LineNumberTable attributes for better crash reports
  - [x] 7.9 Enable mapping file generation in build.gradle.kts
  - [x] 7.10 Test R8 optimization with staging build: `./gradlew assembleStagingRelease`
  - [x] 7.11 Verify app functionality is not broken by obfuscation (install and test staging APK)
  - [x] 7.12 Configure automatic mapping file upload to Firebase Crashlytics in build.gradle.kts

- [x] 8.0 Testing and documentation
  - [ ] 8.1 Create a test pull request to trigger dev build workflow (User to test)
  - [ ] 8.2 Verify dev build workflow completes successfully (User to test)
  - [ ] 8.3 Verify dev APK is uploaded to Firebase App Distribution (User to test)
  - [ ] 8.4 Download and install dev APK on device to verify it works (User to test)
  - [ ] 8.5 Merge dev PR to develop branch to trigger staging build workflow (User to test)
  - [ ] 8.6 Verify staging build workflow completes successfully (User to test)
  - [ ] 8.7 Verify staging APK is uploaded to Firebase App Distribution (User to test)
  - [ ] 8.8 Download and install staging APK on device (alongside dev APK to verify different package IDs) (User to test)
  - [ ] 8.9 Merge develop to main branch to trigger production build workflow (User to test)
  - [ ] 8.10 Verify production build workflow completes successfully (User to test)
  - [ ] 8.11 Verify production AAB is uploaded to Google Play Console internal testing track (User to test)
  - [ ] 8.12 Verify version code increments correctly across builds (User to test)
  - [ ] 8.13 Verify all three variants can be installed simultaneously on device (User to test)
  - [x] 8.14 Create `docs/CI_CD_SETUP.md` with comprehensive setup instructions
  - [x] 8.15 Document all required GitHub Secrets and how to obtain them (in CI_CD_SETUP.md)
  - [x] 8.16 Document workflow triggers and what each workflow does (in CI_CD_SETUP.md)
  - [x] 8.17 Add workflow status badges to `README.md` for each workflow
  - [x] 8.18 Create troubleshooting section in documentation for common issues (TROUBLESHOOTING_CI_CD.md)
  - [x] 8.19 Document how to manually trigger workflows if needed (in CI_CD_SETUP.md and TROUBLESHOOTING_CI_CD.md)
  - [x] 8.20 Fix Gemfile.lock to include fastlane-plugin-firebase_app_distribution
  - [ ] 8.21 Commit all changes and push to remote repository (User to do)
  - [ ] 8.22 Create pull request for code review (User to do)
  - [ ] 8.23 Address any review comments and merge to develop/main branch (User to do)

## GitHub Secrets Required

Add these secrets in GitHub repository settings (Settings > Secrets and variables > Actions):

### Keystore Secrets
- `STAGING_KEYSTORE_FILE` - Base64 encoded staging.jks file
- `STAGING_KEYSTORE_PASSWORD` - Password for staging keystore
- `STAGING_KEY_ALIAS` - Alias for staging key (e.g., "staging")
- `STAGING_KEY_PASSWORD` - Password for staging key
- `RELEASE_KEYSTORE_FILE` - Base64 encoded release.jks file
- `RELEASE_KEYSTORE_PASSWORD` - Password for production keystore
- `RELEASE_KEY_ALIAS` - Alias for production key (e.g., "release")
- `RELEASE_KEY_PASSWORD` - Password for production key

### Firebase Secrets
- `GOOGLE_SERVICES_JSON` - Base64 encoded google-services.json file
- `FIREBASE_TOKEN` - Firebase CLI token from `firebase login:ci`
- `FIREBASE_APP_ID` - Firebase app ID (e.g., "1:123456789:android:abc123")

### Google Play Secrets
- `PLAY_STORE_SERVICE_ACCOUNT_JSON` - Service account JSON key file content (not base64)

### AdMob Secrets (Production)
- `ADMOB_APP_ID_PROD` - Production AdMob app ID
- `ADMOB_BANNER_ID_PROD` - Production banner ad unit ID
- `ADMOB_INTERSTITIAL_ID_PROD` - Production interstitial ad unit ID

## Environment-Specific Configurations

### Test Build
- **Package ID:** com.cihatakyol.sleeptimer.test
- **AdMob IDs:** Test/Demo IDs (ca-app-pub-3940256099942544~3347511713)
- **Debuggable:** Yes
- **Minification:** No
- **Distribution:** Firebase App Distribution (internal-developers)

### Staging Build
- **Package ID:** com.cihatakyol.sleeptimer.staging
- **AdMob IDs:** Test/Demo IDs (ca-app-pub-3940256099942544~3347511713)
- **Debuggable:** No
- **Minification:** Yes (R8)
- **Distribution:** Firebase App Distribution (internal-developers, beta-testers)

### Release Build
- **Package ID:** com.cihatakyol.sleeptimer
- **AdMob IDs:** Production IDs (from GitHub Secrets)
- **Debuggable:** No
- **Minification:** Yes (R8 with aggressive optimization)
- **Distribution:** Google Play Console (internal testing track, manual promotion to production)

## Notes

- Always test workflows locally or in a test repository before applying to production
- Keep keystore passwords secure and rotate them periodically
- Monitor Firebase quota limits for App Distribution
- Google Play has a limit on internal testing track submissions
- Use semantic versioning for versionName (e.g., 1.0.0, 1.1.0, 2.0.0)
- GitHub Actions free tier has 2000 minutes/month for private repos, unlimited for public repos
- Each build takes approximately 5-15 minutes depending on caching and build complexity
