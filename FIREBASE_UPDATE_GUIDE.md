# Firebase Update Guide for Package Name Change

## Overview
This guide provides step-by-step instructions to update your Firebase project after changing the app package name from `com.cihatakyol.sleeptimer` to `com.ToolCompany.screentimer`.

## Prerequisites
- Access to Firebase Console (https://console.firebase.google.com)
- Project admin permissions
- The new `google-services.json` file (will be generated in Firebase Console)

## Steps to Update Firebase

### 1. Add New Package to Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com)
2. Select your project (SleepTimer)
3. Click on the **gear icon** (⚙️) next to "Project Overview" → **Project settings**
4. Scroll down to "Your apps" section
5. Find your Android app
6. Click **Add fingerprint** or **Add app** if you need to add a new configuration

### 2. Add New Package Name

**Option A: Add as a new Android app (Recommended)**
1. In Project settings → Your apps section
2. Click **"Add app"** → Select **Android**
3. Enter the new package name: `com.ToolCompany.screentimer`
4. Enter app nickname (optional): "Screen Timer"
5. Click **"Register app"**
6. Download the new `google-services.json` file
7. Add your SHA-1 and SHA-256 fingerprints (for features like Google Sign-In)

**Option B: Update existing app package name**
⚠️ **Warning**: This will break the old app installations
1. In Project settings → Your apps section
2. Find your existing Android app
3. This option may not be available - Firebase typically requires adding a new app

### 3. Update SHA Fingerprints

Get your SHA-1 and SHA-256 fingerprints:

```bash
# For debug builds
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

# For release builds
keytool -list -v -keystore /path/to/your/release.jks -alias your-key-alias
```

Add fingerprints in Firebase Console:
1. Project settings → Your apps → Select your app
2. Scroll to **SHA certificate fingerprints**
3. Click **"Add fingerprint"**
4. Paste your SHA-1 fingerprint
5. Repeat for SHA-256 fingerprint

### 4. Download and Replace google-services.json

1. In Firebase Console → Project settings → Your apps
2. Find the app with package name `com.ToolCompany.screentimer`
3. Click **"google-services.json"** download button
4. Replace the existing file in your project:
   ```bash
   # Navigate to your project
   cd /Users/cihat.akyol/AndroidStudioProjects/SleepTimer

   # Replace the file
   cp ~/Downloads/google-services.json app/google-services.json
   ```
5. **Important**: Update the base64-encoded version in GitHub Secrets:
   ```bash
   # Encode the file
   base64 -i app/google-services.json | pbcopy

   # Then go to GitHub → Settings → Secrets and variables → Actions
   # Update GOOGLE_SERVICES_JSON with the new base64 content
   ```

### 5. Update Firebase Services Configuration

#### Analytics
- No additional changes needed
- Data will start appearing under the new package name

#### Crashlytics
1. Go to Firebase Console → Crashlytics
2. You should see the new package name automatically
3. Upload mapping files for proper crash deobfuscation:
   ```bash
   # This is handled automatically by the build workflow
   # Mapping files are uploaded as artifacts
   ```

#### Performance Monitoring
- No additional changes needed
- Performance data will appear under the new package name

#### Cloud Messaging (FCM)
If you use FCM:
1. Go to Firebase Console → Cloud Messaging
2. Verify server key is still valid
3. No changes needed to existing tokens (they'll update automatically)

### 6. Update AdMob (if linked to Firebase)

1. Go to [AdMob Console](https://apps.admob.com)
2. Find your app
3. **Update App Settings**:
   - Click on your app
   - Go to **App settings**
   - Update package name if needed
4. **Link to Firebase** (if not already linked):
   - In Firebase Console → Project settings
   - Scroll to **Service accounts**
   - Link AdMob account

### 7. Update Play Store Listing (Optional)

If you need to connect Firebase to Play Store:
1. Firebase Console → Project settings → Integrations
2. Find **Google Play**
3. Click **Link** and follow the instructions

### 8. Verify Configuration

After updating everything:

1. **Build the app**:
   ```bash
   ./gradlew assembleProductionRelease
   ```

2. **Install and test**:
   - Install on a test device
   - Check Logcat for Firebase initialization:
     ```
     adb logcat -s FirebaseApp
     ```
   - You should see: "Firebase initialization completed successfully"

3. **Verify in Firebase Console**:
   - Firebase Console → Analytics → Events
   - Install the app and use it
   - Check if events appear (may take a few hours)

4. **Test Crashlytics**:
   - Add a test crash in your code
   - Run the app and trigger the crash
   - Check Firebase Console → Crashlytics
   - Crash should appear within a few minutes

## GitHub Secrets to Update

Update these secrets in GitHub repository settings:

1. **GOOGLE_SERVICES_JSON**
   - Base64-encoded `google-services.json` file
   - Command to encode: `base64 -i app/google-services.json | pbcopy`

2. **FIREBASE_APP_ID_DEV** (if using different Firebase projects for dev)
   - Get from Firebase Console → Project settings → Your apps
   - Format: `1:123456789:android:abcdef123456`

3. **FIREBASE_APP_ID_STAGING** (if using)
   - Same as above, for staging flavor

## Migration Notes

### Data Continuity
- **Analytics**: Historical data remains in the old package name
- **Crashlytics**: New crashes will appear under new package name
- **Performance**: New traces will appear under new package name

### Existing Users
- Users must **uninstall the old app** and install the new one
- Data stored locally will be lost (unless you implement migration)
- Consider implementing data export/import for user data

### Dual Package Support (Transitional Period)
If you want both packages to work simultaneously:
1. Keep both apps in Firebase Console
2. Each will have separate analytics/crashlytics
3. You can merge data later if needed

## Troubleshooting

### google-services.json errors
```
Error: File google-services.json is not well formed
```
- Re-download from Firebase Console
- Ensure it's the correct file for `com.ToolCompany.screentimer`
- Validate JSON syntax: `python -m json.tool app/google-services.json`

### Firebase not initializing
```
FirebaseApp initialization unsuccessful
```
- Check if `google-services.json` is in the correct location (`app/` directory)
- Verify package name matches in `google-services.json` and `build.gradle.kts`
- Clean and rebuild: `./gradlew clean assembleDebug`

### Crashlytics not working
- Ensure Crashlytics plugin is applied in `build.gradle.kts`
- Check if mapping files are being generated
- Verify app is using release build (Crashlytics only works in release builds)

### Analytics not showing data
- Data can take up to 24 hours to appear
- Ensure app is in foreground for at least 1 minute
- Check DebugView in Firebase Console for real-time events
- Enable debug mode: `adb shell setprop debug.firebase.analytics.app com.ToolCompany.screentimer`

## Rollback Plan

If something goes wrong:
1. Revert package name changes in code
2. Restore old `google-services.json`
3. Build and deploy
4. Old Firebase configuration will work again

## Additional Resources

- [Firebase Android Setup Guide](https://firebase.google.com/docs/android/setup)
- [Change Package Name in Firebase](https://firebase.google.com/docs/projects/manage-installations)
- [Firebase Crashlytics Documentation](https://firebase.google.com/docs/crashlytics)
- [Firebase Analytics Documentation](https://firebase.google.com/docs/analytics)

---

**Last Updated**: 2025-11-29
**Version**: 3.0
**Package**: com.ToolCompany.screentimer
