package com.ToolCompany.screentimer

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.ToolCompany.screentimer.ads.AppOpenAdManager
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SleepTimerApplication : Application(), Application.ActivityLifecycleCallbacks {

    private var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()

        // Register activity lifecycle callbacks for foreground detection
        registerActivityLifecycleCallbacks(this)

        // Initialize AdMob SDK
        initializeAdMob()

        // Preload first app open ad after SDK initialization
        AppOpenAdManager.loadAd(this)
    }

    private fun initializeAdMob() {
        try {
            MobileAds.initialize(this, OnInitializationCompleteListener { initializationStatus: InitializationStatus ->
                Log.d(TAG, "AdMob SDK initialized successfully")
                Log.d(TAG, "Initialization status: ${initializationStatus.adapterStatusMap}")
            })
        } catch (e: Exception) {
            // Silent failure - app should continue even if AdMob fails to initialize
            Log.e(TAG, "Failed to initialize AdMob SDK", e)
        }
    }

    // ActivityLifecycleCallbacks implementation
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = activity
    }

    override fun onActivityStarted(activity: Activity) {
        // Show app open ad when app comes to foreground
        // Frequency cap and ad availability handled by AppOpenAdManager
        if (currentActivity != null) {
            AppOpenAdManager.showAdIfAvailable(activity)
        }
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        // No action needed
    }

    override fun onActivityStopped(activity: Activity) {
        // No action needed
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // No action needed
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (currentActivity == activity) {
            currentActivity = null
        }
    }

    companion object {
        private const val TAG = "SleepTimerApp"
    }
}