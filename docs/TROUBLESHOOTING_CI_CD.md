# CI/CD Pipeline Troubleshooting Guide

This document provides solutions to common issues encountered with the CI/CD pipeline.

## Table of Contents

- [Workflow Failures](#workflow-failures)
- [Build Errors](#build-errors)
- [Signing Issues](#signing-issues)
- [Firebase Distribution Issues](#firebase-distribution-issues)
- [Google Play Console Issues](#google-play-console-issues)
- [Dependency Issues](#dependency-issues)
- [ProGuard/R8 Issues](#proguardr8-issues)
- [Secret Management Issues](#secret-management-issues)

---

## Workflow Failures

### Workflow not triggering

**Problem**: GitHub Actions workflow doesn't start when expected.

**Solutions**:

1. **Check branch names**: Ensure you're pushing to the correct branch
   - Dev builds: Any PR
   - Staging builds: `develop` branch
   - Production builds: `main` or `master` branch

2. **Verify workflow syntax**: Check for YAML syntax errors
   ```bash
   # Use a YAML validator
   yamllint .github/workflows/*.yml
   ```

3. **Check GitHub Actions permissions**:
   - Go to repository Settings > Actions > General
   - Ensure "Allow all actions and reusable workflows" is enabled
   - Verify workflow permissions are set to "Read and write permissions"

### Workflow stuck in queue

**Problem**: Workflow shows "Queued" status for extended period.

**Solutions**:

1. **Check concurrent job limits**: Free GitHub accounts have limits on concurrent jobs
   - Wait for other workflows to complete
   - Consider GitHub Actions paid plan for more concurrent jobs

2. **Check runner availability**: Ensure `ubuntu-latest` runners are available
   - GitHub status: https://www.githubstatus.com/

---

## Build Errors

### Gradle build fails with "Task not found"

**Problem**: Error like `Task 'assembleProductionRelease' not found`

**Solutions**:

1. **Verify flavor names**: Check `app/build.gradle.kts` for correct flavor names
   ```kotlin
   productFlavors {
       create("dev") { ... }
       create("staging") { ... }
       create("production") { ... }
   }
   ```

2. **Clean and rebuild**:
   ```bash
   ./gradlew clean
   ./gradlew assembleStagingRelease
   ```

### Missing google-services.json error

**Problem**: Build fails with "File google-services.json is missing"

**Solutions**:

1. **Verify secret exists**: Check that `GOOGLE_SERVICES_JSON` secret is set in GitHub
   - Go to Settings > Secrets and variables > Actions
   - Verify `GOOGLE_SERVICES_JSON` exists and contains base64 encoded content

2. **Check decode step**: Ensure workflow has decode step before build
   ```yaml
   - name: Decode google-services.json
     env:
       GOOGLE_SERVICES_JSON: ${{ secrets.GOOGLE_SERVICES_JSON }}
     run: |
       echo "$GOOGLE_SERVICES_JSON" | base64 --decode > app/google-services.json
   ```

3. **Verify base64 encoding**: Re-encode the file
   ```bash
   base64 -i app/google-services.json | pbcopy
   # Paste into GitHub Secret (macOS)

   # Linux
   base64 -w 0 app/google-services.json
   ```

### Package name mismatch error

**Problem**: Error about package name not matching in google-services.json

**Solution**: This is expected for dev and staging builds. The production flavor conditionally applies the plugin:

```kotlin
// In app/build.gradle.kts
if (gradle.startParameter.taskRequests.toString().contains("Production")) {
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
}
```

This is working as designed - Firebase is only enabled for production builds.

### Manifest merger failed

**Problem**: Error about conflicting attributes in AndroidManifest.xml

**Solutions**:

1. **Check for duplicate properties**: Common with AdMob and Firebase
   ```xml
   <!-- Add tools:replace to resolve conflicts -->
   <property
       android:name="android.adservices.AD_SERVICES_CONFIG"
       android:resource="@xml/gma_ad_services_config"
       tools:replace="android:resource" />
   ```

2. **Add tools namespace**: Ensure manifest has tools namespace
   ```xml
   <manifest xmlns:android="http://schemas.android.com/apk/res/android"
       xmlns:tools="http://schemas.android.com/tools">
   ```

---

## Signing Issues

### Keystore file not found

**Problem**: Error "Keystore file 'keystores/release.jks' not found"

**Solutions**:

1. **Check decode step**: Ensure keystore is decoded before build
   ```yaml
   - name: Decode production keystore
     env:
       RELEASE_KEYSTORE_FILE: ${{ secrets.RELEASE_KEYSTORE_FILE }}
     run: |
       mkdir -p keystores
       echo "$RELEASE_KEYSTORE_FILE" | base64 --decode > keystores/release.jks
   ```

2. **Verify secret**: Check that keystore secret is correctly base64 encoded
   ```bash
   # Re-encode keystore
   base64 -i keystores/release.jks > keystores/release_base64.txt
   # Copy contents of release_base64.txt to GitHub Secret
   ```

### Invalid keystore password

**Problem**: "Keystore was tampered with, or password was incorrect"

**Solutions**:

1. **Verify password secret**: Check that password matches keystore
   ```bash
   # Test keystore locally
   keytool -list -v -keystore keystores/release.jks
   # Enter password to verify it works
   ```

2. **Check for whitespace**: Ensure no extra spaces in GitHub Secret
   - Secrets are trimmed automatically, but verify no newlines

3. **Verify alias**: Ensure key alias secret matches keystore
   ```bash
   # List aliases in keystore
   keytool -list -keystore keystores/release.jks
   ```

### Wrong key used to sign APK

**Problem**: Upload to Play Store fails with "Upload failed: You uploaded an APK that is signed with a different certificate"

**Solutions**:

1. **Verify correct keystore**: Ensure using production keystore for production builds
   - Check workflow uses `RELEASE_KEYSTORE_FILE` not `STAGING_KEYSTORE_FILE`

2. **Check signing configuration**: Verify `app/build.gradle.kts` applies correct signing
   ```kotlin
   androidComponents {
       onVariants { variant ->
           when (variant.flavorName) {
               "production" -> {
                   if (variant.buildType == "release") {
                       variant.signingConfig.setConfig(signingConfigs.getByName("production"))
                   }
               }
           }
       }
   }
   ```

3. **Use Play App Signing**: Consider enrolling in Play App Signing to avoid signing issues
   - Google Play Console > Release > Setup > App signing
   - Upload certificate from keystore
   - Use upload key for builds

---

## Firebase Distribution Issues

### Firebase distribution fails with authentication error

**Problem**: "Authentication failed" when uploading to Firebase

**Solutions**:

1. **Regenerate Firebase token**:
   ```bash
   firebase login:ci
   # Copy new token to FIREBASE_TOKEN secret
   ```

2. **Verify token hasn't expired**: Firebase tokens can expire
   - Regenerate token every 6-12 months

3. **Check Firebase CLI version**: Ensure Fastlane uses recent firebase-tools
   ```ruby
   # In Fastfile
   firebase_app_distribution(
       app: ENV["FIREBASE_APP_ID"],
       firebase_cli_token: ENV["FIREBASE_TOKEN"],
       # ... other settings
   )
   ```

### Wrong Firebase App ID

**Problem**: "App not found" error from Firebase

**Solutions**:

1. **Verify App ID format**: Should be like `1:123456789:android:abcdef123456`
   ```bash
   # Find in Firebase Console
   # Project Settings > General > Your apps > App ID
   ```

2. **Check secret value**: Ensure `FIREBASE_APP_ID` secret is correct
   - No extra spaces or newlines
   - Matches the app in Firebase Console

### Testers not receiving distribution

**Problem**: Firebase distribution succeeds but testers don't get notified

**Solutions**:

1. **Verify tester groups**: Check groups exist in Firebase Console
   - Firebase Console > App Distribution > Testers & Groups
   - Ensure `internal-developers` and `beta-testers` groups exist

2. **Check group members**: Verify testers are added to groups
   - Add testers via Firebase Console
   - Testers must accept invitation

3. **Check release notes**: Ensure release notes are provided
   ```ruby
   firebase_app_distribution(
       release_notes: ENV["RELEASE_NOTES"] || "New build available",
       # ...
   )
   ```

4. **Email notifications**: Enable notifications in Firebase Console
   - App Distribution > Settings
   - Enable "Notify testers via email"

---

## Google Play Console Issues

### Upload to Play Store fails with 403 error

**Problem**: "The caller does not have permission"

**Solutions**:

1. **Verify service account permissions**:
   - Google Play Console > Users and permissions
   - Service account should have "Release to Internal Testing" permission
   - Ensure service account is for correct app

2. **Re-download service account JSON**:
   ```bash
   # Base64 encode new JSON
   base64 -i service-account.json | pbcopy
   # Update PLAY_STORE_SERVICE_ACCOUNT_JSON secret
   ```

3. **Check API access**: Ensure Play Console API access is enabled
   - Google Play Console > API access
   - Service account should be linked

### Version code conflict

**Problem**: "Version code X has already been used"

**Solutions**:

1. **Check version code calculation**: Ensure `github.run_number` is incrementing
   ```yaml
   # In workflow, check run number
   - name: Debug version code
     run: |
       echo "Run number: ${{ github.run_number }}"
       echo "Version code: $((1 * 1000 + ${{ github.run_number }}))"
   ```

2. **Increment base version code**: If needed, update in `app/build.gradle.kts`
   ```kotlin
   val baseVersionCode = 2 // Increment from 1
   ```

3. **Check Play Console**: Verify no manual uploads with conflicting version codes
   - Play Console > Release > Production/Testing
   - Check version codes of existing releases

### APK/AAB file not found

**Problem**: Upload fails with "File not found" error

**Solutions**:

1. **Verify build path**: Check that build output path is correct
   ```bash
   # Expected paths
   app/build/outputs/bundle/productionRelease/app-production-release.aab
   app/build/outputs/apk/production/release/app-production-release.apk
   ```

2. **Check build task**: Ensure build task completed successfully
   - Look for "BUILD SUCCESSFUL" in logs
   - Verify no build errors before upload step

3. **List build outputs**: Debug by listing files
   ```yaml
   - name: List build outputs
     run: |
       ls -R app/build/outputs/bundle/
       ls -R app/build/outputs/apk/
   ```

---

## Dependency Issues

### Bundler version mismatch

**Problem**: "Your bundle is locked to X but running Y"

**Solutions**:

1. **Update Gemfile.lock**:
   ```bash
   bundle update --bundler
   git add Gemfile.lock
   git commit -m "Update bundler version"
   ```

2. **Use bundler-cache**: Workflow already uses Ruby cache
   ```yaml
   - uses: ruby/setup-ruby@v1
     with:
       ruby-version: '3.0'
       bundler-cache: true  # This handles version
   ```

### Gradle dependency resolution failure

**Problem**: "Could not resolve dependency" errors

**Solutions**:

1. **Clear Gradle cache**: Workflow already has cache, but can clear
   ```yaml
   - name: Clear Gradle cache
     run: |
       rm -rf ~/.gradle/caches
       rm -rf ~/.gradle/wrapper
   ```

2. **Check repository access**: Ensure repositories are accessible
   ```kotlin
   // In build.gradle.kts
   repositories {
       google()
       mavenCentral()
   }
   ```

3. **Update dependency versions**: Check for deprecated or unavailable versions

### Firebase plugin version conflict

**Problem**: "Duplicate class found" or version conflicts with Firebase

**Solutions**:

1. **Use Firebase BoM**: Already implemented in `app/build.gradle.kts`
   ```kotlin
   implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
   implementation("com.google.firebase:firebase-crashlytics")
   implementation("com.google.firebase:firebase-analytics")
   ```

2. **Exclude conflicting dependencies**:
   ```kotlin
   implementation("com.some.library") {
       exclude(group = "com.google.firebase", module = "firebase-common")
   }
   ```

---

## ProGuard/R8 Issues

### App crashes after ProGuard/R8 optimization

**Problem**: Release build crashes but debug build works

**Solutions**:

1. **Check ProGuard rules**: Verify all necessary classes are kept
   ```proguard
   # In proguard-rules.pro
   -keep class com.cihatakyol.sleeptimer.** { *; }
   -keep class * extends androidx.lifecycle.ViewModel { *; }
   ```

2. **Analyze crash reports**: Check Firebase Crashlytics for obfuscated stack traces
   - Upload mapping file to deobfuscate:
   ```bash
   # Mapping file location
   app/build/outputs/mapping/productionRelease/mapping.txt
   ```

3. **Test release build locally**:
   ```bash
   ./gradlew assembleProductionRelease
   adb install app/build/outputs/apk/production/release/app-production-release.apk
   # Test app functionality
   ```

4. **Add missing keep rules**: Common additions
   ```proguard
   # Keep data classes
   -keep class com.cihatakyol.sleeptimer.data.** { *; }

   # Keep ViewModels
   -keep class * extends androidx.lifecycle.ViewModel {
       <init>(...);
   }

   # Keep Hilt generated classes
   -keep class **_HiltModules { *; }
   -keep class **_Factory { *; }
   ```

### Missing mapping files

**Problem**: Cannot deobfuscate crash reports

**Solutions**:

1. **Verify artifact upload**: Check workflow uploaded mapping files
   ```yaml
   - name: Upload mapping files as artifact
     uses: actions/upload-artifact@v4
     with:
       name: production-mapping-files
       path: app/build/outputs/mapping/productionRelease/
       retention-days: 365
   ```

2. **Download mapping files**: Get from GitHub Actions artifacts
   - Actions tab > Select workflow run > Artifacts section
   - Download `production-mapping-files`

3. **Upload to Firebase**: Automatically upload mapping files
   ```bash
   # Using Firebase Crashlytics Gradle plugin (already configured)
   # Mapping files automatically uploaded during build
   ```

---

## Secret Management Issues

### Secret not available in workflow

**Problem**: Environment variable is empty in workflow

**Solutions**:

1. **Check secret name**: Ensure exact match (case-sensitive)
   ```yaml
   env:
     RELEASE_KEYSTORE_PASSWORD: ${{ secrets.RELEASE_KEYSTORE_PASSWORD }}
   ```

2. **Verify secret is set**: GitHub Settings > Secrets and variables > Actions
   - Secret must be repository secret, not organization secret (unless configured)

3. **Check secret scope**: Organization secrets need repository access
   - If using organization secret, verify repository has access

### Base64 decoding fails

**Problem**: "Invalid base64 input" error

**Solutions**:

1. **Re-encode file correctly**:
   ```bash
   # macOS
   base64 -i file.jks -o file_base64.txt

   # Linux (no line wrapping)
   base64 -w 0 file.jks > file_base64.txt
   ```

2. **Copy full content**: Ensure entire base64 string is copied
   - No truncation
   - No extra newlines or spaces

3. **Test decode locally**:
   ```bash
   echo "$BASE64_CONTENT" | base64 --decode > test.jks
   # Verify file is valid
   file test.jks
   ```

### Secrets visible in logs

**Problem**: Concerned about secret exposure

**Solution**: GitHub automatically masks secrets in logs
- Any secret value is automatically replaced with `***`
- If secret appears, it's likely not registered as a secret
- Never `echo` or `cat` secret files in workflow

---

## Performance Issues

### Workflow takes too long

**Problem**: Workflow runs for extended time

**Solutions**:

1. **Use Gradle cache**: Already implemented
   ```yaml
   - uses: actions/cache@v4
     with:
       path: |
         ~/.gradle/caches
         ~/.gradle/wrapper
       key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*') }}
   ```

2. **Use bundler cache**: Already implemented
   ```yaml
   - uses: ruby/setup-ruby@v1
     with:
       bundler-cache: true
   ```

3. **Parallel builds**: Enable in gradle.properties
   ```properties
   org.gradle.parallel=true
   org.gradle.caching=true
   ```

4. **Reduce test scope**: For dev builds, tests continue-on-error
   ```yaml
   - name: Run unit tests
     run: ./gradlew testDebugUnitTest --continue
     continue-on-error: true
   ```

---

## Getting Help

If issues persist:

1. **Check workflow logs**: GitHub Actions > Select workflow run > View logs
2. **Review documentation**: See `docs/CI_CD_SETUP.md`
3. **Check Firebase Console**: For distribution issues
4. **Check Play Console**: For upload issues
5. **Verify secrets**: Double-check all GitHub Secrets are correctly set
6. **Test locally**: Try running Gradle/Fastlane commands locally

### Useful Commands for Debugging

```bash
# Test Gradle build locally
./gradlew assembleStagingRelease --stacktrace

# Test Fastlane locally
bundle exec fastlane build_staging

# Verify keystore
keytool -list -v -keystore keystores/release.jks

# Test Firebase upload
firebase appdistribution:distribute \
  app/build/outputs/apk/staging/release/app-staging-release.apk \
  --app FIREBASE_APP_ID \
  --groups "internal-developers"

# Decode and test google-services.json
echo "$GOOGLE_SERVICES_JSON" | base64 --decode > app/google-services.json
cat app/google-services.json | jq .
```

### Log Analysis Tips

1. **Search for "FAILED"**: Find which step failed
2. **Check "exit code"**: Non-zero indicates failure
3. **Look for stack traces**: Full error context
4. **Check environment variables**: Verify values (secrets will be masked)
5. **Review artifact uploads**: Ensure files exist before upload

---

## Additional Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Fastlane Documentation](https://docs.fastlane.tools/)
- [Firebase App Distribution](https://firebase.google.com/docs/app-distribution)
- [Google Play Console API](https://developers.google.com/android-publisher)
- [ProGuard Manual](https://www.guardsquare.com/manual/home)
- [R8 Documentation](https://developer.android.com/build/shrink-code)
