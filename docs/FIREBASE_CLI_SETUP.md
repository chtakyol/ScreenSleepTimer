# Firebase CLI Setup and Token Generation

## Task 1.13 - Generate Firebase CLI Token

This guide will help you install Firebase CLI and generate a CI token for GitHub Actions.

## Current Status
- **Firebase CLI:** Not installed
- **Required for:** Firebase App Distribution uploads in CI/CD pipeline

## Step 1: Install Firebase CLI

### Option A: Using npm (Recommended)
```bash
npm install -g firebase-tools
```

### Option B: Using standalone binary
```bash
curl -sL https://firebase.tools | bash
```

### Option C: Using Homebrew (macOS)
```bash
brew install firebase-cli
```

## Step 2: Verify Installation

```bash
firebase --version
```

Expected output: `13.x.x` or similar version number

## Step 3: Login to Firebase (CI Token)

Run the following command to generate a CI token:

```bash
firebase login:ci
```

### What happens:
1. A browser window will open
2. Sign in with your Google account (the one used for Firebase Console)
3. Authorize Firebase CLI
4. A token will be displayed in the terminal

### Example output:
```
✔  Success! Use this token to login on a CI server:

1//0daBcDeFgHiJkLmNoPqRsTuVwXyZ1234567890-abcdefghijklmnopqrstuvwxyz

Example: firebase deploy --token "$FIREBASE_TOKEN"
```

## Step 4: Save the Token Securely

**IMPORTANT:** This token is sensitive! It provides access to your Firebase project.

1. **Copy the token** (the long string after "Use this token to login on a CI server:")
2. **Save it temporarily** to add to GitHub Secrets (task 1.17)
3. **Do NOT commit the token** to version control

### Token will be used as:
- GitHub Secret name: `FIREBASE_TOKEN`
- Used by: Firebase App Distribution uploads in CI/CD workflows

## Step 5: Test the Token (Optional)

You can verify the token works:

```bash
firebase projects:list --token "YOUR_TOKEN_HERE"
```

This should list your Firebase projects, including "sleeptimer-462513".

## Troubleshooting

### "firebase: command not found"
- Ensure npm bin directory is in your PATH
- Try: `export PATH=$PATH:$(npm bin -g)`
- Or restart your terminal after installation

### "Error: Failed to authenticate"
- Make sure you're logged into the correct Google account
- Try: `firebase logout` then `firebase login:ci` again

### Token Expired
- CI tokens don't expire unless revoked
- If needed, regenerate: `firebase login:ci --reauth`

## Security Notes

1. **Token Protection**
   - Never commit tokens to Git
   - Never share tokens publicly
   - Store only in GitHub Secrets

2. **Token Revocation**
   - To revoke: Go to https://myaccount.google.com/permissions
   - Find "Firebase CLI"
   - Click "Remove access"

3. **Token Rotation**
   - Regenerate periodically for security
   - Update GitHub Secret when rotated

## Next Steps

After generating the token:
1. Save the token value
2. Continue to task 1.17 to add it as `FIREBASE_TOKEN` in GitHub Secrets
3. The token will be used in workflows for:
   - Firebase App Distribution uploads
   - Crashlytics mapping file uploads

---

**Status:** Awaiting Firebase CLI installation and token generation
**Date:** 2025-11-27
