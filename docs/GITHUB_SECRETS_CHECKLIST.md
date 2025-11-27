# GitHub Secrets Configuration Checklist

## Task 1.17 - Add All Required GitHub Secrets

This document provides a step-by-step checklist for adding all required secrets to your GitHub repository.

## How to Add Secrets

1. Go to your GitHub repository
2. Click **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Enter the name and value for each secret below
5. Click **Add secret**

## Secrets Checklist

### Keystore Secrets (4 secrets)

#### Staging Keystore
- [ ] **STAGING_KEYSTORE_FILE**
  - Source: Copy content from `keystores/staging_base64.txt`
  - Value: Base64 encoded staging.jks file

- [ ] **STAGING_KEYSTORE_PASSWORD**
  - Value: `staging123`

- [ ] **STAGING_KEY_ALIAS**
  - Value: `staging`

- [ ] **STAGING_KEY_PASSWORD**
  - Value: `staging123`

#### Production Keystore
- [ ] **RELEASE_KEYSTORE_FILE**
  - Source: Copy content from `keystores/release_base64.txt`
  - Value: Base64 encoded release.jks file

- [ ] **RELEASE_KEYSTORE_PASSWORD**
  - Value: [Your production keystore password]
  - ⚠️ Use the actual password for screenTimerKeyStore.jks

- [ ] **RELEASE_KEY_ALIAS**
  - Value: [Your production key alias]
  - ⚠️ Use the actual alias from screenTimerKeyStore.jks

- [ ] **RELEASE_KEY_PASSWORD**
  - Value: [Your production key password]
  - ⚠️ Use the actual key password for screenTimerKeyStore.jks

### Firebase Secrets (3 secrets)

- [ ] **GOOGLE_SERVICES_JSON**
  - Source: Copy content from `keystores/google-services_base64.txt`
  - Value: Base64 encoded google-services.json file

- [ ] **FIREBASE_TOKEN**
  - Value: [Token from `firebase login:ci` command]
  - Format: `1//0...` (long string)

- [ ] **FIREBASE_APP_ID**
  - Value: `1:1013770654798:android:6a641fa491f0cba749df1f`
  - Source: From google-services.json (`mobilesdk_app_id`)

### Google Play Secrets (1 secret)

- [ ] **PLAY_STORE_SERVICE_ACCOUNT_JSON**
  - Value: [Content of Google Play service account JSON file]
  - ⚠️ Use the actual JSON content, NOT base64 encoded
  - Should start with: `{"type": "service_account", ...}`

### AdMob Production Secrets (3 secrets)

- [ ] **ADMOB_APP_ID_PROD**
  - Value: [Your production AdMob app ID]
  - Format: `ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX`
  - ⚠️ NOT the test ID currently in AndroidManifest.xml

- [ ] **ADMOB_BANNER_ID_PROD**
  - Value: [Your production banner ad unit ID]
  - Format: `ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX`

- [ ] **ADMOB_INTERSTITIAL_ID_PROD**
  - Value: [Your production interstitial ad unit ID]
  - Format: `ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX`

## Total Secrets: 14

### Summary by Category:
- **Keystores:** 8 secrets (4 staging + 4 release)
- **Firebase:** 3 secrets
- **Google Play:** 1 secret
- **AdMob:** 3 secrets

## How to Get Values

### For Base64 Encoded Files:
```bash
# View the entire content of a base64 file
cat keystores/staging_base64.txt

# Copy to clipboard (macOS)
cat keystores/staging_base64.txt | pbcopy

# Copy to clipboard (Linux with xclip)
cat keystores/staging_base64.txt | xclip -selection clipboard
```

### For Production Keystore Info:
If you don't remember the alias/passwords for your production keystore:
```bash
# List keystore details (will prompt for password)
keytool -list -v -keystore keystores/release.jks

# The alias will be shown in the output
# If you don't have the password, you'll need to use the original screenTimerKeyStore.jks
```

### For Firebase App ID:
Already available from google-services.json:
- App ID: `1:1013770654798:android:6a641fa491f0cba749df1f`

### For AdMob IDs:
Check your AdMob account:
1. Go to https://apps.admob.com
2. Select your app
3. Click "App settings" to get App ID
4. Click "Ad units" to get Banner and Interstitial IDs

## Verification

After adding all secrets, verify in GitHub:
1. Go to **Settings** → **Secrets and variables** → **Actions**
2. You should see **14 repository secrets**
3. Names should match exactly (case-sensitive)

## Security Reminder

- ✅ Secrets are encrypted by GitHub
- ✅ Secrets are never exposed in logs
- ✅ Only GitHub Actions workflows can access them
- ⚠️ Never commit secrets to Git
- ⚠️ Never share secrets publicly

## Next Steps

After adding all secrets:
- Mark task 1.17 as complete
- Task 1.0 will be 100% complete (18/18 tasks)
- Proceed to Task 2.0: Configure Gradle build variants

---

**Status:** Ready to add secrets to GitHub
**Date:** 2025-11-27
