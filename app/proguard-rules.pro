# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# ================================
# General Android Rules
# ================================

# Preserve line number information for debugging stack traces
-keepattributes SourceFile,LineNumberTable

# Keep source file names for better crash reports
-renamesourcefileattribute SourceFile

# Keep annotations
-keepattributes *Annotation*

# Keep generic signature for reflection
-keepattributes Signature

# Keep exceptions for better crash reports
-keepattributes Exceptions

# ================================
# Kotlin
# ================================

# Keep Kotlin metadata for reflection
-keep class kotlin.Metadata { *; }

# Keep Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.** {
    volatile <fields>;
}

# Keep Kotlin data classes
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# ================================
# Jetpack Compose
# ================================

# Keep Compose runtime classes
-keep class androidx.compose.** { *; }
-keep interface androidx.compose.** { *; }

# Keep Composable functions
-keep @androidx.compose.runtime.Composable public class * {
    public <methods>;
}

# Keep stability markers
-keepclassmembers class androidx.compose.** {
    <fields>;
}

# ================================
# Hilt / Dagger
# ================================

# Keep Hilt generated classes
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
-keep class **_HiltModules { *; }
-keep class **_HiltComponents { *; }
-keep class **_HiltComponentsInternal { *; }
-keep class **_Factory { *; }
-keep class **_MembersInjector { *; }

# Keep Hilt annotations
-keepclassmembers class * {
    @dagger.hilt.* <fields>;
    @dagger.hilt.* <methods>;
    @javax.inject.* <fields>;
    @javax.inject.* <methods>;
}

# Keep entry points
-keep @dagger.hilt.InstallIn class *
-keep @dagger.hilt.codegen.OriginatingElement class *
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }

# ================================
# Firebase
# ================================

# Firebase Crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.crashlytics.**

# Firebase Analytics
-keep class com.google.android.gms.measurement.** { *; }
-keep class com.google.firebase.analytics.** { *; }
-dontwarn com.google.android.gms.measurement.**

# Firebase Performance
-keep class com.google.firebase.perf.** { *; }
-dontwarn com.google.firebase.perf.**

# Firebase general
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# ================================
# Google Play Services / AdMob
# ================================

# AdMob
-keep public class com.google.android.gms.ads.** {
    public *;
}
-keep public class com.google.ads.** {
    public *;
}
-keep class com.google.android.gms.ads.identifier.** { *; }
-dontwarn com.google.android.gms.ads.**

# Google Play Services
-keep class com.google.android.gms.common.** { *; }
-dontwarn com.google.android.gms.**

# ================================
# AndroidX & Jetpack
# ================================

# AndroidX
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-dontwarn androidx.**

# Lifecycle
-keep class androidx.lifecycle.** { *; }
-keepclassmembers class androidx.lifecycle.** { *; }

# Navigation
-keep class androidx.navigation.** { *; }
-keepnames class androidx.navigation.fragment.NavHostFragment

# DataStore
-keep class androidx.datastore.** { *; }
-keepclassmembers class * extends androidx.datastore.core.Serializer {
    <methods>;
}

# ================================
# Accompanist
# ================================

-keep class com.google.accompanist.** { *; }
-dontwarn com.google.accompanist.**

# ================================
# App Specific Rules
# ================================

# Keep all application classes
-keep class com.cihatakyol.sleeptimer.** { *; }

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# Keep custom Application class
-keep class com.cihatakyol.sleeptimer.SleepTimerApplication { *; }

# Keep data classes (for Kotlin serialization/reflection)
-keep @kotlinx.serialization.Serializable class com.cihatakyol.sleeptimer.** { *; }

# Keep Parcelable implementations
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep Serializable classes
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep Device Admin Receiver
-keep class com.cihatakyol.sleeptimer.receiver.SleepTimerDeviceAdmin { *; }

# Keep Services and Receivers
-keep class com.cihatakyol.sleeptimer.service.** { *; }
-keep class com.cihatakyol.sleeptimer.receiver.** { *; }

# ================================
# Reflection & Native
# ================================

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep classes with @Keep annotation
-keep @androidx.annotation.Keep class * { *; }
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

# ================================
# Optimization
# ================================

# Enable aggressive optimization for production
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose

# Optimization filters
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*

# ================================
# Warnings to Ignore
# ================================

-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn javax.annotation.**
-dontwarn javax.lang.model.**
-dontwarn org.jetbrains.annotations.**

# ================================
# Debug Information (for Crashlytics)
# ================================

# Preserve source file names and line numbers
-keepattributes SourceFile,LineNumberTable

# Keep custom exceptions
-keep public class * extends java.lang.Exception

# Rename source file attribute to hide internal details
-renamesourcefileattribute SourceFile
