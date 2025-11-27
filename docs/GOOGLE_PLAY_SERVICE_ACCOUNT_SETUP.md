# Google Play Service Account Setup Guide

## Tasks 1.14-1.16: Google Play Console Integration

This guide covers setting up a Google Play service account for automated app uploads via CI/CD.

## Prerequisites

- Google Play Developer account (already have, since app is published)
- Access to Google Cloud Console
- App already published on Google Play Store

## Task 1.14: Set up Google Play Service Account in Google Cloud Console

### Step 1: Access Google Cloud Console

1. Go to https://console.cloud.google.com
2. Sign in with your Google Play developer account

### Step 2: Create or Select a Project

**Option A: If you already have a Google Cloud project linked to Play Console**
1. Select the existing project from the dropdown
2. Note: Projects linked to Play Console usually have names like "pc-api-..." or your app name

**Option B: Create a new project**
1. Click project dropdown → "New Project"
2. Project name: `SleepTimer Play Console API` (or similar)
3. Click "Create"

### Step 3: Enable Google Play Developer API

1. In Google Cloud Console, go to **APIs & Services** → **Library**
2. Search for "Google Play Android Developer API"
3. Click on it
4. Click **Enable** button
5. Wait for the API to be enabled

### Step 4: Create Service Account

1. Go to **APIs & Services** → **Credentials**
2. Click **Create Credentials** → **Service Account**
3. Fill in details:
   - **Service account name:** `sleeptimer-ci-cd`
   - **Service account ID:** `sleeptimer-ci-cd` (auto-generated)
   - **Description:** `Service account for SleepTimer CI/CD pipeline`
4. Click **Create and Continue**
5. **Grant this service account access to project** (optional, skip for now)
   - Click **Continue**
6. **Grant users access to this service account** (optional)
   - Click **Done**

### Step 5: Create Service Account Key (JSON)

1. In the **Service Accounts** list, find the account you just created
2. Click on the service account email (e.g., `sleeptimer-ci-cd@...`)
3. Go to the **Keys** tab
4. Click **Add Key** → **Create new key**
5. Select **JSON** format
6. Click **Create**
7. A JSON file will be downloaded automatically
8. **Save this file securely** - you'll need it for task 1.16

### Important Information from JSON:

The downloaded JSON file contains:
```json
{
  "type": "service_account",
  "project_id": "your-project-id",
  "private_key_id": "...",
  "private_key": "...",
  "client_email": "sleeptimer-ci-cd@your-project.iam.gserviceaccount.com",
  "client_id": "...",
  ...
}
```

**Note the `client_email`** - you'll need this for task 1.15

## Task 1.15: Grant Service Account Access to Google Play Console

Now you need to give this service account permission to upload builds to Play Console.

### Step 1: Access Google Play Console

1. Go to https://play.google.com/console
2. Sign in with your Google Play developer account
3. Select your organization/developer account

### Step 2: Add Service Account User

1. In Play Console, go to **Setup** → **API access**
   - Or direct link: https://play.google.com/console/api-access
2. If this is your first time:
   - Click **Link a Google Cloud project**
   - Select the Google Cloud project you used/created in task 1.14
   - Click **Link project**
3. Scroll down to **Service accounts**
4. Find the service account you created (email: `sleeptimer-ci-cd@...`)
5. Click **Grant access**

### Step 3: Configure Permissions

In the **Invite user** dialog:

1. **App permissions:**
   - Click **Add app**
   - Select **SleepTimer** from the list
   - Click **Apply**

2. **Account permissions:**
   - **View app information and download bulk reports:** ✓ (auto-selected)
   - **Create and edit draft apps:** Leave unchecked
   - **Release to production, exclude devices, and use app-internal only:** Leave unchecked
   - **Release apps to testing tracks:** ✓ **CHECK THIS**
   - **Manage testing tracks and edit tester lists:** ✓ **CHECK THIS**

3. Click **Invite user**
4. Click **Send invite** (even though it's a service account)

### Important Permissions Explained:

- ✅ **Release apps to testing tracks:** Allows CI/CD to upload to internal/alpha/beta tracks
- ✅ **Manage testing tracks:** Allows managing internal testing
- ❌ **Release to production:** We DON'T want CI/CD to auto-release to production (manual only)

## Task 1.16: Download Service Account JSON Key File

You already completed this in task 1.14, step 5!

The JSON file downloaded from Google Cloud Console is your service account key.

### What to do with the JSON file:

1. **Keep it secure** - it's like a password
2. **Do NOT commit to Git** - it's already in .gitignore
3. **Copy the entire JSON content** for GitHub Secret `PLAY_STORE_SERVICE_ACCOUNT_JSON`

### To copy the JSON content:

```bash
# View the content
cat path/to/downloaded-service-account-key.json

# Copy to clipboard (macOS)
cat path/to/downloaded-service-account-key.json | pbcopy

# Copy to clipboard (Linux)
cat path/to/downloaded-service-account-key.json | xclip -selection clipboard
```

## Verification

### Test the Service Account (Optional)

You can test if the service account has proper access:

1. Install Google Play Developer API client (if not already):
   ```bash
   pip install google-api-python-client google-auth
   ```

2. Test listing app details:
   ```python
   from google.oauth2 import service_account
   from googleapiclient.discovery import build

   credentials = service_account.Credentials.from_service_account_file(
       'path/to/service-account.json',
       scopes=['https://www.googleapis.com/auth/androidpublisher']
   )

   service = build('androidpublisher', 'v3', credentials=credentials)
   result = service.edits().insert(
       packageName='com.cihatakyol.sleeptimer'
   ).execute()

   print("Success! Edit ID:", result['id'])
   ```

If this runs without errors, your service account is properly configured!

## Troubleshooting

### "The current user has insufficient permissions"
- Go back to Play Console → API access
- Make sure the service account has "Release apps to testing tracks" permission
- For the correct app (SleepTimer)

### "Google Play Android Developer API has not been enabled"
- Go to Google Cloud Console
- APIs & Services → Library
- Enable "Google Play Android Developer API"

### "Permission denied on resource project"
- Make sure the Google Cloud project is linked to Play Console
- Play Console → Setup → API access → Link project

### Can't find the service account in Play Console
- Make sure you're in the correct Google Cloud project
- The service account email should match what you created
- Wait a few minutes and refresh Play Console

## Security Best Practices

1. ✅ Use a dedicated service account for CI/CD
2. ✅ Grant minimum required permissions (testing tracks only, not production)
3. ✅ Store JSON key securely in GitHub Secrets only
4. ✅ Rotate service account keys periodically
5. ❌ Never commit service account JSON to Git
6. ❌ Never share service account keys publicly

## Next Steps

After completing tasks 1.14-1.16:
- You'll have the service account JSON file
- Add it to GitHub Secrets as `PLAY_STORE_SERVICE_ACCOUNT_JSON`
- The CI/CD pipeline will use it to upload builds to Play Console

---

**Status:** Guide for Google Play service account setup
**Date:** 2025-11-27
