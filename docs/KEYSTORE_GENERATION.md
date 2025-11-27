# Keystore Generation Guide

This document explains how to generate and manage keystores for the SleepTimer Android application.

## Overview

The SleepTimer app uses different keystores for different build variants:
- **Staging Keystore** (`staging.jks`) - For staging builds distributed via Firebase App Distribution
- **Production Keystore** (`release.jks`) - For production builds published to Google Play Store

## Prerequisites

- JDK installed (version 8 or higher)
- `keytool` command available in your PATH (comes with JDK)
- Access to store keystore passwords securely (password manager recommended)

## Keystore Locations

All keystores are stored in the `keystores/` directory at the project root:
```
keystores/
├── staging.jks       # Staging keystore
└── release.jks       # Production keystore (copy of screenTimerKeyStore.jks)
```

**IMPORTANT:** The `keystores/` directory is excluded from version control via `.gitignore`. Never commit keystores to Git.

## Generating Keystores

### Staging Keystore

The staging keystore was generated using the following command:

```bash
keytool -genkey -v \
  -keystore keystores/staging.jks \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -alias staging \
  -storepass staging123 \
  -keypass staging123 \
  -dname "CN=Sleep Timer Staging, OU=Development, O=Sleep Timer, L=Unknown, ST=Unknown, C=US"
```

**Parameters:**
- **Algorithm:** RSA
- **Key Size:** 2048 bits
- **Validity:** 10,000 days (~27 years)
- **Alias:** staging
- **Store Password:** staging123
- **Key Password:** staging123

### Production Keystore

The production keystore (`release.jks`) is a copy of the existing `screenTimerKeyStore.jks` that was originally used to sign the app for Google Play Store.

**Important:** The production keystore passwords and alias are already configured and must be kept secure. Do NOT regenerate the production keystore as it would prevent updates to the existing Play Store listing.

## Verifying Keystores

To verify a keystore and view its details:

```bash
# List keystore contents
keytool -list -v -keystore keystores/staging.jks -storepass staging123

# List keystore contents (production - password required)
keytool -list -v -keystore keystores/release.jks
```

## Base64 Encoding for CI/CD

For use in GitHub Secrets, keystores must be base64 encoded:

### macOS
```bash
# Staging keystore
base64 -i keystores/staging.jks | pbcopy

# Production keystore
base64 -i keystores/release.jks | pbcopy
```

### Linux
```bash
# Staging keystore
base64 keystores/staging.jks > staging_base64.txt

# Production keystore
base64 keystores/release.jks > release_base64.txt
```

## GitHub Secrets Configuration

The following secrets must be added to the GitHub repository:

### Staging Keystore Secrets
- `STAGING_KEYSTORE_FILE` - Base64 encoded staging.jks file
- `STAGING_KEYSTORE_PASSWORD` - `staging123`
- `STAGING_KEY_ALIAS` - `staging`
- `STAGING_KEY_PASSWORD` - `staging123`

### Production Keystore Secrets
- `RELEASE_KEYSTORE_FILE` - Base64 encoded release.jks file
- `RELEASE_KEYSTORE_PASSWORD` - [Original production password]
- `RELEASE_KEY_ALIAS` - [Original production alias]
- `RELEASE_KEY_PASSWORD` - [Original production key password]

## Security Best Practices

1. **Never Commit Keystores**
   - Keystores are excluded via `.gitignore`
   - Double-check before committing changes

2. **Backup Keystores Securely**
   - Store production keystore in a secure location (encrypted backup, password manager)
   - Losing the production keystore means you cannot update the app on Google Play Store

3. **Rotate Staging Keystore**
   - The staging keystore can be regenerated if compromised
   - Update GitHub Secrets if staging keystore changes

4. **Production Keystore is Critical**
   - NEVER lose or share the production keystore
   - NEVER regenerate the production keystore
   - Store passwords in a secure password manager
   - Limit access to production keystore to release managers only

## Troubleshooting

### "keytool: command not found"
- Ensure JDK is installed
- Add JDK bin directory to PATH: `export PATH=$PATH:$JAVA_HOME/bin`

### "Keystore was tampered with, or password was incorrect"
- Verify you're using the correct password
- Check that the keystore file is not corrupted

### "Certificate already exists in keystore"
- The alias already exists in the keystore
- Use a different alias or delete the existing entry

## Keystore Renewal

### When to Renew
- Before the validity period expires (10,000 days for staging)
- Never renew production keystore (use the same one for app lifetime)

### How to Check Validity
```bash
keytool -list -v -keystore keystores/staging.jks -storepass staging123 | grep Valid
```

## References

- [Android App Signing Documentation](https://developer.android.com/studio/publish/app-signing)
- [keytool Documentation](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html)
- [Google Play App Signing](https://support.google.com/googleplay/android-developer/answer/9842756)

---

**Last Updated:** 2025-11-27
**Owner:** Engineering Team
