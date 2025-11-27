# Firebase Setup Notes

## Current Status
- **Firebase Integration:** Not yet configured
- **google-services.json:** Not present in project
- **Firebase Gradle Plugin:** Not added

## Task 1.7 - Firebase Project Setup

### Steps to Complete:

1. **Go to Firebase Console**
   - URL: https://console.firebase.google.com
   - Sign in with Google account

2. **Create New Project** (if not exists)
   - Click "Add project" or "Create a project"
   - Project name: `sleeptimer`
   - Enable/disable Google Analytics (recommended: enable for production)
   - Click "Create Project"

3. **Add Android App to Project**
   - Click "Add app" → Select Android icon
   - **Android package name:** `com.cihatakyol.sleeptimer`
   - **App nickname:** SleepTimer (optional)
   - **Debug signing certificate SHA-1:** (optional for now)
   - Click "Register app"

4. **Download google-services.json**
   - Download the `google-services.json` file
   - **Important:** DO NOT add to project yet (will be done in later tasks)
   - Keep file secure for task 1.11

5. **Firebase Features to Enable:**
   - Firebase App Distribution (task 1.8)
   - Firebase Crashlytics (task 1.10)
   - Firebase Analytics (already included in google-services.json)
   - Firebase Performance Monitoring (optional)

## App Variants Configuration

For the CI/CD pipeline, we'll need to register the following package names:

### Test Variant
- **Package Name:** `com.cihatakyol.sleeptimer.test`
- **Purpose:** Internal testing via Firebase App Distribution
- **Action:** Register as separate app in Firebase OR use same google-services.json

### Staging Variant
- **Package Name:** `com.cihatakyol.sleeptimer.staging`
- **Purpose:** Pre-production testing via Firebase App Distribution
- **Action:** Register as separate app in Firebase OR use same google-services.json

### Release Variant
- **Package Name:** `com.cihatakyol.sleeptimer`
- **Purpose:** Production release on Google Play Store
- **Action:** Primary app registration (already done in step 3)

## Recommendation

For simplicity, you can use the **same google-services.json** for all variants. Firebase will recognize the different package names as the same app in development.

Alternatively, for stricter separation, create 3 separate Firebase projects:
- SleepTimer-Test
- SleepTimer-Staging
- SleepTimer-Production

**Recommended approach:** Use single Firebase project for all variants initially, then separate if needed.

## Next Steps After Task 1.7

Once Firebase project is created:
- Task 1.8: Enable Firebase App Distribution
- Task 1.9: Add tester groups
- Task 1.10: Enable Crashlytics
- Task 1.11: Download google-services.json
- Task 1.12: Base64 encode google-services.json for GitHub Secrets
- Task 1.13: Generate Firebase CLI token

---

**Status:** Waiting for manual Firebase project creation
**Date:** 2025-11-27
