# CI/CD Pipeline Setup Guide

## Overview

This document describes the complete CI/CD pipeline setup for the SleepTimer Android application. The pipeline uses GitHub Actions for automation and Fastlane for build and distribution management.

## Build Variants

The application supports three build variants, each serving a different purpose in the development lifecycle:

### 1. Dev (Development)
- **Purpose**: Daily development and testing
- **Package**: `com.cihatakyol.sleeptimer.dev`
- **Trigger**: Pull requests to any branch
- **Build Type**: Debug
- **Distribution**: Firebase App Distribution (internal-developers group)
- **AdMob**: Test AdMob IDs
- **Signing**: Debug keystore
- **Workflow**: `.github/workflows/dev-build.yml`

### 2. Staging
- **Purpose**: Pre-release testing with beta testers
- **Package**: `com.cihatakyol.sleeptimer.staging`
- **Trigger**: Push to `develop` branch
- **Build Type**: Release (signed)
- **Distribution**: Firebase App Distribution (internal-developers and beta-testers groups)
- **AdMob**: Test AdMob IDs
- **Signing**: Staging keystore (`keystores/staging.jks`)
- **Workflow**: `.github/workflows/staging-build.yml`

### 3. Production
- **Purpose**: Release to Google Play Store
- **Package**: `com.cihatakyol.sleeptimer`
- **Trigger**: Push to `main` or `master` branch
- **Build Type**: Release (signed)
- **Distribution**: Google Play Console (Internal Testing track)
- **AdMob**: Production AdMob IDs
- **Signing**: Production keystore (`keystores/release.jks`)
- **Workflow**: `.github/workflows/production-build.yml`

## Workflow Details

### Dev Build Workflow

**File**: `.github/workflows/dev-build.yml`

**Trigger**: Pull request events (opened, synchronize, reopened)

**Steps**:
1. Checkout code
2. Set up JDK 17
3. Cache Gradle dependencies
4. Decode and place `google-services.json`
5. Run unit tests (continue-on-error)
6. Build Dev Debug APK
7. Set up Ruby and install Fastlane
8. Upload to Firebase App Distribution
9. Upload test results and APK as artifacts
10. Comment on PR with build status

**Artifacts**:
- APK: `app-dev-debug` (30 day retention)
- Test results: `test-results` (30 day retention)

### Staging Build Workflow

**File**: `.github/workflows/staging-build.yml`

**Trigger**: Push to `develop` branch

**Steps**:
1. Checkout code
2. Set up JDK 17
3. Cache Gradle dependencies
4. Decode staging keystore
5. Decode and place `google-services.json`
6. Run unit tests (continue-on-error)
7. Build Staging Release APK (signed)
8. Set up Ruby and install Fastlane
9. Upload to Firebase App Distribution
10. Upload test results, APK, and mapping files
11. Cleanup keystore

**Artifacts**:
- APK: `app-staging-release` (90 day retention)
- Mapping files: `staging-mapping-files` (90 day retention)
- Test results: `staging-test-results` (30 day retention)

### Production Build Workflow

**File**: `.github/workflows/production-build.yml`

**Trigger**: Push to `main` or `master` branch

**Steps**:
1. Checkout code
2. Set up JDK 17
3. Cache Gradle dependencies
4. Decode production keystore
5. Decode and place `google-services.json`
6. Run unit tests (must pass)
7. Build Production Release AAB (signed)
8. Build Production Release APK (signed)
9. Set up Ruby and install Fastlane
10. Upload AAB to Google Play Console (Internal Testing)
11. Extract version name
12. Create GitHub Release with tag
13. Upload AAB, APK, and mapping files as artifacts
14. Cleanup keystore

**Artifacts**:
- AAB: `app-production-release-aab` (90 day retention)
- APK: `app-production-release-apk` (90 day retention)
- Mapping files: `production-mapping-files` (365 day retention)

**GitHub Release**:
- Tag format: `v{version}-{run_number}` (e.g., `v1.0.0-42`)
- Includes AAB and APK as release assets
- Auto-generated release notes with commit information

## GitHub Secrets Configuration

The following secrets must be configured in GitHub repository settings (`Settings > Secrets and variables > Actions`):

### Keystore Secrets
- `STAGING_KEYSTORE_FILE`: Base64 encoded staging keystore file
- `STAGING_KEYSTORE_PASSWORD`: Password for staging keystore
- `STAGING_KEY_ALIAS`: Alias for staging key
- `STAGING_KEY_PASSWORD`: Password for staging key
- `RELEASE_KEYSTORE_FILE`: Base64 encoded production keystore file
- `RELEASE_KEYSTORE_PASSWORD`: Password for production keystore
- `RELEASE_KEY_ALIAS`: Alias for production key
- `RELEASE_KEY_PASSWORD`: Password for production key

### Firebase Secrets
- `GOOGLE_SERVICES_JSON`: Base64 encoded google-services.json file
- `FIREBASE_APP_ID`: Firebase App ID (format: `1:123456789:android:abcdef`)
- `FIREBASE_TOKEN`: Firebase CI token (generate with `firebase login:ci`)

### Google Play Secrets
- `PLAY_STORE_SERVICE_ACCOUNT_JSON`: Base64 encoded service account JSON

### AdMob Secrets
- `ADMOB_APP_ID_PROD`: Production AdMob App ID
- `ADMOB_BANNER_ID_PROD`: Production AdMob Banner ID
- `ADMOB_INTERSTITIAL_ID_PROD`: Production AdMob Interstitial ID

See `docs/GITHUB_SECRETS_CHECKLIST.md` for detailed setup instructions.

## Version Management

The application uses a dynamic versioning system:

### Version Code
```kotlin
versionCode = (baseVersionCode * 1000) + ciRunNumber
```

- **Base Version Code**: Set manually in `app/build.gradle.kts` (currently 1)
- **CI Run Number**: Automatically incremented by GitHub Actions (`github.run_number`)
- **Example**: Base 1, Run 42 → Version Code 1042

### Version Name
```kotlin
versionName = "1.0.0"
versionNameSuffix = "-dev" // or "-staging" for staging builds
```

- Set manually in `app/build.gradle.kts`
- Follows semantic versioning (MAJOR.MINOR.PATCH)
- Dev builds: `1.0.0-dev`
- Staging builds: `1.0.0-staging`
- Production builds: `1.0.0`

## Build Configuration

### Product Flavors

Defined in `app/build.gradle.kts`:

```kotlin
flavorDimensions += "environment"

productFlavors {
    create("dev") {
        dimension = "environment"
        applicationIdSuffix = ".dev"
        versionNameSuffix = "-dev"
        buildConfigField("String", "ADMOB_APP_ID", "\"ca-app-pub-3940256099942544~3347511713\"")
        buildConfigField("String", "ADMOB_BANNER_ID", "\"ca-app-pub-3940256099942544/6300978111\"")
        buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\"ca-app-pub-3940256099942544/1033173712\"")
        buildConfigField("String", "BUILD_ENVIRONMENT", "\"dev\"")
        manifestPlaceholders["admobAppId"] = "ca-app-pub-3940256099942544~3347511713"
    }

    create("staging") {
        dimension = "environment"
        applicationIdSuffix = ".staging"
        versionNameSuffix = "-staging"
        // Similar configuration with test AdMob IDs
    }

    create("production") {
        dimension = "environment"
        // Uses production AdMob IDs from environment variables
        // Firebase enabled only for this flavor
    }
}
```

### Signing Configuration

Defined in `app/build.gradle.kts`:

```kotlin
signingConfigs {
    create("staging") {
        storeFile = file("../keystores/staging.jks")
        storePassword = System.getenv("STAGING_KEYSTORE_PASSWORD")
        keyAlias = System.getenv("STAGING_KEY_ALIAS")
        keyPassword = System.getenv("STAGING_KEY_PASSWORD")
    }

    create("production") {
        storeFile = file("../keystores/release.jks")
        storePassword = System.getenv("RELEASE_KEYSTORE_PASSWORD")
        keyAlias = System.getenv("RELEASE_KEY_ALIAS")
        keyPassword = System.getenv("RELEASE_KEY_PASSWORD")
    }
}
```

Applied using androidComponents API:

```kotlin
androidComponents {
    onVariants { variant ->
        when (variant.flavorName) {
            "staging" -> {
                if (variant.buildType == "release") {
                    variant.signingConfig.setConfig(signingConfigs.getByName("staging"))
                }
            }
            "production" -> {
                if (variant.buildType == "release") {
                    variant.signingConfig.setConfig(signingConfigs.getByName("production"))
                }
            }
        }
    }
}
```

### ProGuard/R8 Configuration

Enabled for release builds:

```kotlin
buildTypes {
    release {
        isMinifyEnabled = true
        isShrinkResources = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
}
```

Custom rules defined in `app/proguard-rules.pro` include:
- Kotlin and Coroutines preservation
- Jetpack Compose runtime preservation
- Hilt/Dagger dependency injection
- Firebase SDK rules
- AdMob SDK rules
- App-specific classes preservation
- Aggressive optimization settings

## Fastlane Configuration

### Lanes

Defined in `fastlane/Fastfile`:

#### Build Lanes
- `build_dev`: Build dev debug APK
- `build_staging`: Build staging release APK
- `build_production`: Build production release AAB
- `build_production_apk`: Build production release APK

#### Distribution Lanes
- `distribute_dev`: Upload dev APK to Firebase (internal-developers)
- `distribute_staging`: Upload staging APK to Firebase (internal-developers, beta-testers)
- `upload_to_play_store_internal`: Upload production AAB to Google Play Console

#### Combined Lanes
- `dev_release`: Build and distribute dev
- `staging_release`: Build and distribute staging
- `production_release`: Build AAB, upload to Play Store, build APK

### Firebase App Distribution

Groups configured:
- **internal-developers**: Receives dev and staging builds
- **beta-testers**: Receives staging builds only

Release notes automatically generated from commit messages.

### Google Play Console Integration

- Uploads to **Internal Testing** track
- Requires manual promotion to higher tracks (closed testing, open testing, production)
- Service account has "Release to Internal Testing" permission

## Testing

### Unit Tests

Run automatically in all workflows:
- Dev workflow: `./gradlew testDebugUnitTest --continue` (continue-on-error)
- Staging workflow: `./gradlew testReleaseUnitTest --continue` (continue-on-error)
- Production workflow: `./gradlew testReleaseUnitTest` (must pass)

Test results uploaded as artifacts for all builds (30 day retention).

## Artifacts and Retention

### Dev Builds
- APK: 30 days
- Test results: 30 days

### Staging Builds
- APK: 90 days
- Mapping files: 90 days
- Test results: 30 days

### Production Builds
- AAB: 90 days
- APK: 90 days
- Mapping files: 365 days (critical for crash analysis)
- GitHub Release: Permanent

## Security Considerations

### Keystore Management
- Keystores never committed to repository (excluded in `.gitignore`)
- Keystores base64 encoded and stored as GitHub Secrets
- Decoded keystores cleaned up after build (`rm -f keystores/*.jks`)
- Cleanup runs even if build fails (`if: always()`)

### Credentials
- All credentials stored as GitHub Secrets
- Environment variables used to pass secrets to build
- No credentials in logs or artifacts

### Firebase Configuration
- `google-services.json` excluded from repository (`.gitignore`)
- File decoded from GitHub Secret during build
- Contains only app configuration, not sensitive credentials

## Workflow Status

Monitor workflow execution:
- GitHub Actions tab in repository
- Workflow status badges in README.md
- Email notifications on failure
- PR comments for dev builds

## Manual Testing Checklist

Before pushing to production:

1. **Dev Build Testing**
   - [ ] Create a test PR
   - [ ] Verify dev-build workflow completes
   - [ ] Check Firebase App Distribution
   - [ ] Download and install dev APK
   - [ ] Verify test AdMob IDs are used
   - [ ] Test core functionality

2. **Staging Build Testing**
   - [ ] Push to develop branch
   - [ ] Verify staging-build workflow completes
   - [ ] Check Firebase App Distribution
   - [ ] Download and install staging APK
   - [ ] Verify test AdMob IDs are used
   - [ ] Verify ProGuard/R8 didn't break functionality
   - [ ] Check Firebase Crashlytics integration

3. **Production Build Testing**
   - [ ] Push to main/master branch
   - [ ] Verify production-build workflow completes
   - [ ] Check Google Play Console internal testing track
   - [ ] Verify GitHub Release created
   - [ ] Download AAB and APK from artifacts
   - [ ] Verify production AdMob IDs are used
   - [ ] Test on internal testing track
   - [ ] Manually promote to production when ready

## Troubleshooting

See `docs/TROUBLESHOOTING_CI_CD.md` for common issues and solutions.

## Related Documentation

- `docs/KEYSTORE_GENERATION.md` - Keystore generation guide
- `docs/FIREBASE_SETUP_NOTES.md` - Firebase project setup
- `docs/FIREBASE_CLI_SETUP.md` - Firebase CLI setup
- `docs/GOOGLE_PLAY_SERVICE_ACCOUNT_SETUP.md` - Google Play service account
- `docs/GITHUB_SECRETS_CHECKLIST.md` - GitHub Secrets setup
- `tasks/prd-cicd-pipeline.md` - Product Requirements Document
- `tasks/tasks-cicd-pipeline.md` - Implementation task list

## Maintenance

### Updating Dependencies
- GitHub Actions: Review and update action versions quarterly
- Gradle: Update Android Gradle Plugin and dependencies regularly
- Ruby/Fastlane: Update Gemfile.lock with `bundle update`

### Version Bumping
1. Update `versionName` in `app/build.gradle.kts`
2. Optionally update `baseVersionCode` for major releases
3. Commit and push to appropriate branch

### Keystore Rotation
1. Generate new keystore (see `docs/KEYSTORE_GENERATION.md`)
2. Base64 encode new keystore
3. Update GitHub Secrets
4. For production: Use Play App Signing to avoid compatibility issues

## Support

For issues or questions:
1. Check `docs/TROUBLESHOOTING_CI_CD.md`
2. Review workflow logs in GitHub Actions
3. Check Firebase and Google Play Console dashboards
4. Contact DevOps team
