# Firebase Apps Setup Guide

## Overview

To support all three build variants (Dev, Staging, Production), you need to create **three separate Android apps** within your Firebase project. Each app will have its own App ID for Firebase App Distribution.

## Why Three Separate Apps?

Each build variant has a different package name:
- **Dev**: `com.cihatakyol.sleeptimer.dev`
- **Staging**: `com.cihatakyol.sleeptimer.staging`
- **Production**: `com.cihatakyol.sleeptimer`

Firebase App Distribution requires the APK package name to match the registered Firebase app package name.

## Setup Steps

### 1. Go to Firebase Console

1. Navigate to https://console.firebase.google.com
2. Select your **SleepTimer** project (or create one if it doesn't exist)

### 2. Add Dev App

1. Click the **gear icon** (⚙️) next to "Project Overview" → "Project settings"
2. Scroll down to "Your apps" section
3. Click **"Add app"** → Select **Android** icon
4. Fill in the details:
   - **Android package name**: `com.cihatakyol.sleeptimer.dev`
   - **App nickname**: `SleepTimer Dev` (optional but recommended)
   - **Debug SHA-1**: (optional, can add later if needed)
5. Click **"Register app"**
6. **Download `google-services.json`** → Save as `google-services-dev.json` (for reference, but we'll use the production one in the project)
7. Click **"Continue"** and **"Skip"** the remaining setup steps
8. **Copy the App ID**:
   - In Project Settings → Your apps → SleepTimer Dev
   - Copy the **App ID** (format: `1:123456789:android:abc123def456`)
   - Save this for GitHub Secrets as `FIREBASE_APP_ID_DEV`

### 3. Add Staging App

1. Still in Project Settings, click **"Add app"** again → Select **Android**
2. Fill in the details:
   - **Android package name**: `com.cihatakyol.sleeptimer.staging`
   - **App nickname**: `SleepTimer Staging`
   - **Debug SHA-1**: (optional)
3. Click **"Register app"**
4. **Download `google-services.json`** → Save as `google-services-staging.json` (for reference)
5. Click **"Continue"** and **"Skip"** remaining steps
6. **Copy the App ID**:
   - In Project Settings → Your apps → SleepTimer Staging
   - Copy the **App ID**
   - Save this for GitHub Secrets as `FIREBASE_APP_ID_STAGING`

### 4. Verify/Add Production App

1. Check if you already have an app with package name `com.cihatakyol.sleeptimer`
2. If not, click **"Add app"** → Select **Android**
3. Fill in the details:
   - **Android package name**: `com.cihatakyol.sleeptimer`
   - **App nickname**: `SleepTimer Production`
   - **Debug SHA-1**: (optional, but recommended for production)
4. Click **"Register app"**
5. **Download `google-services.json`** → This is the main file used in the project
6. **Copy the App ID**:
   - In Project Settings → Your apps → SleepTimer Production
   - Copy the **App ID**
   - Save this for GitHub Secrets as `FIREBASE_APP_ID_PROD` (if needed in the future)

### 5. Enable Firebase App Distribution

For **each app** (Dev, Staging, Production):

1. In Firebase Console left sidebar, click **"Release & Monitor"** → **"App Distribution"**
2. Select the app from the dropdown at the top
3. Click **"Get started"** (if prompted)
4. The App Distribution is now enabled for this app

### 6. Set Up Tester Groups

Tester groups are shared across all apps in the project:

1. In Firebase Console → **App Distribution** → **"Testers & Groups"** tab
2. Click **"Add Group"**
3. Create group: **`internal-developers`**
   - Add email addresses of internal team members
   - Click **"Save"**
4. Click **"Add Group"** again
5. Create group: **`beta-testers`**
   - Add email addresses of beta testers
   - Click **"Save"**

## GitHub Secrets Configuration

Add these secrets to your GitHub repository (Settings → Secrets and variables → Actions → New repository secret):

### Firebase App IDs

| Secret Name | Value | How to Get |
|------------|-------|------------|
| `FIREBASE_APP_ID_DEV` | `1:xxx:android:devAppId` | Firebase Console → Project Settings → SleepTimer Dev → App ID |
| `FIREBASE_APP_ID_STAGING` | `1:xxx:android:stagingAppId` | Firebase Console → Project Settings → SleepTimer Staging → App ID |
| `FIREBASE_TOKEN` | `1//xxx...` | Run `firebase login:ci` in terminal (already done) |

### Note on google-services.json

The project currently uses a single `google-services.json` file (for production). This works because:
- Dev and Staging builds don't use Firebase features (Crashlytics, Analytics) - they only use App Distribution
- Production build uses the production `google-services.json` with Firebase features enabled

If you want to enable Firebase features for all variants, you would need to:
1. Download all three `google-services.json` files
2. Place them in variant-specific directories
3. Update the build configuration to use the correct file per variant

## Verification Checklist

- [ ] Three apps created in Firebase Console:
  - [ ] `com.cihatakyol.sleeptimer.dev` (SleepTimer Dev)
  - [ ] `com.cihatakyol.sleeptimer.staging` (SleepTimer Staging)
  - [ ] `com.cihatakyol.sleeptimer` (SleepTimer Production)
- [ ] App Distribution enabled for all three apps
- [ ] Tester groups created:
  - [ ] `internal-developers` group
  - [ ] `beta-testers` group
- [ ] Testers added to groups
- [ ] GitHub Secrets added:
  - [ ] `FIREBASE_APP_ID_DEV`
  - [ ] `FIREBASE_APP_ID_STAGING`
  - [ ] `FIREBASE_TOKEN`

## Testing

After setup, test each variant:

### Test Dev Build
1. Create a pull request
2. Dev build workflow should trigger
3. Check Firebase Console → App Distribution → SleepTimer Dev
4. Verify the build appears for `internal-developers` group

### Test Staging Build
1. Merge PR to `develop` branch
2. Staging build workflow should trigger
3. Check Firebase Console → App Distribution → SleepTimer Staging
4. Verify the build appears for `internal-developers` and `beta-testers` groups

### Test Production Build
1. Merge to `main`/`master` branch
2. Production build goes to Google Play Console (not Firebase App Distribution)

## Troubleshooting

### Error: "APK package name does not match Firebase app package name"

**Problem**: The Firebase app is registered with a different package name.

**Solution**:
1. Verify the package name in `app/build.gradle.kts` matches the Firebase app
2. Check that you're using the correct `FIREBASE_APP_ID_*` secret for each variant
3. Ensure the Firebase app exists in Firebase Console

### Error: "App not found"

**Problem**: Invalid Firebase App ID or app doesn't exist.

**Solution**:
1. Double-check the App ID in Firebase Console → Project Settings
2. Verify the GitHub Secret value matches exactly (no extra spaces)
3. Ensure App Distribution is enabled for the app

### Error: "Permission denied"

**Problem**: Firebase token expired or insufficient permissions.

**Solution**:
1. Regenerate Firebase token: `firebase login:ci`
2. Update `FIREBASE_TOKEN` secret in GitHub
3. Ensure the Firebase account has App Distribution permissions

## Quick Reference

### Firebase Console Links
- Project Settings: https://console.firebase.google.com/project/YOUR_PROJECT_ID/settings/general
- App Distribution: https://console.firebase.google.com/project/YOUR_PROJECT_ID/appdistribution

### Command to Get App IDs
From Firebase Console → Project Settings → scroll down to "Your apps" section, click on each app to see its App ID.

### Workflow File Locations
- Dev: `.github/workflows/dev-build.yml`
- Staging: `.github/workflows/staging-build.yml`
- Production: `.github/workflows/production-build.yml`

## Next Steps

Once all apps are set up:
1. Add the three GitHub Secrets
2. Create a test PR to trigger dev-build workflow
3. Verify the APK appears in Firebase App Distribution
4. Check that testers receive email notifications
5. Download and install the APK to test

---

**Last Updated**: 2025-11-28
