# SleepTimer

A simple and elegant Android application that allows users to set a timer to automatically lock their device screen. Perfect for listening to music, podcasts, or audiobooks before sleep.

## Build Status

[![Dev Build](https://github.com/cihatakyol/SleepTimer/actions/workflows/dev-build.yml/badge.svg)](https://github.com/cihatakyol/SleepTimer/actions/workflows/dev-build.yml)
[![Staging Build](https://github.com/cihatakyol/SleepTimer/actions/workflows/staging-build.yml/badge.svg)](https://github.com/cihatakyol/SleepTimer/actions/workflows/staging-build.yml)
[![Production Build](https://github.com/cihatakyol/SleepTimer/actions/workflows/production-build.yml/badge.svg)](https://github.com/cihatakyol/SleepTimer/actions/workflows/production-build.yml)

## Features

- Set custom sleep timer duration
- Automatically locks device screen when timer expires
- Clean and modern UI built with Jetpack Compose
- Material Design 3 theming
- Firebase integration for analytics and crash reporting
- AdMob integration for monetization

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Hilt dependency injection
- **Minimum SDK**: Android 5.0 (API 21)
- **Target SDK**: Android 14 (API 34)

### Libraries & Dependencies

- Jetpack Compose (UI)
- Hilt (Dependency Injection)
- Kotlin Coroutines (Asynchronous operations)
- AndroidX Lifecycle (ViewModel)
- Firebase Crashlytics (Crash reporting)
- Firebase Analytics (Usage analytics)
- Firebase Performance Monitoring
- Google AdMob (Monetization)
- Accompanist (Compose utilities)

## Build Variants

The application supports three build variants:

### Dev
- **Purpose**: Development and testing
- **Package**: `com.cihatakyol.sleeptimer.dev`
- **Distribution**: Firebase App Distribution
- **AdMob**: Test IDs

### Staging
- **Purpose**: Pre-release testing
- **Package**: `com.cihatakyol.sleeptimer.staging`
- **Distribution**: Firebase App Distribution
- **AdMob**: Test IDs

### Production
- **Purpose**: Production release
- **Package**: `com.cihatakyol.sleeptimer`
- **Distribution**: Google Play Store
- **AdMob**: Production IDs

## CI/CD Pipeline

The project uses GitHub Actions for continuous integration and deployment:

- **Pull Requests**: Automatically builds dev variant and uploads to Firebase App Distribution
- **Develop Branch**: Automatically builds staging variant and uploads to Firebase App Distribution
- **Main Branch**: Automatically builds production variant, uploads to Google Play Console, and creates GitHub Release

For detailed CI/CD setup information, see [docs/CI_CD_SETUP.md](docs/CI_CD_SETUP.md).

## Building Locally

### Prerequisites

- JDK 17
- Android Studio Hedgehog or later
- Android SDK with API 34

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/cihatakyol/SleepTimer.git
   cd SleepTimer
   ```

2. Add `google-services.json` to the `app/` directory (required for Firebase)

3. Build the project:
   ```bash
   ./gradlew assembleDevDebug
   ```

### Build Commands

```bash
# Dev debug build
./gradlew assembleDevDebug

# Staging release build (requires keystore)
./gradlew assembleStagingRelease

# Production release build (requires keystore)
./gradlew assembleProductionRelease

# Run unit tests
./gradlew testDebugUnitTest

# Run all checks
./gradlew check
```

## Permissions

The app requires the following permissions:

- **DEVICE_ADMIN**: Required to lock the device screen
- **FOREGROUND_SERVICE**: Required to run timer service
- **POST_NOTIFICATIONS**: Required for timer notifications (Android 13+)
- **INTERNET**: Required for Firebase and AdMob
- **ACCESS_NETWORK_STATE**: Required for AdMob

## License

[Add your license information here]

## Contributing

[Add contribution guidelines here]

## Support

For issues, questions, or feature requests, please open an issue on GitHub.

## Documentation

- [CI/CD Setup Guide](docs/CI_CD_SETUP.md)
- [Keystore Generation](docs/KEYSTORE_GENERATION.md)
- [Firebase Setup](docs/FIREBASE_SETUP_NOTES.md)
- [Google Play Service Account Setup](docs/GOOGLE_PLAY_SERVICE_ACCOUNT_SETUP.md)
- [GitHub Secrets Checklist](docs/GITHUB_SECRETS_CHECKLIST.md)
- [Troubleshooting](docs/TROUBLESHOOTING_CI_CD.md)
