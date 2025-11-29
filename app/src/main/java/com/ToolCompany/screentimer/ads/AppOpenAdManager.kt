package com.ToolCompany.screentimer.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.ToolCompany.screentimer.BuildConfig
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd

/**
 * Singleton manager for app open ads.
 *
 * Handles loading, displaying, and frequency capping of app open ads.
 * Implements a 1-hour frequency cap to avoid showing ads too frequently.
 *
 * Usage:
 * ```kotlin
 * // In Application onCreate()
 * AppOpenAdManager.loadAd(this)
 *
 * // In Activity onCreate() or onStart()
 * AppOpenAdManager.showAdIfAvailable(this)
 * ```
 */
object AppOpenAdManager {

    private const val TAG = "AppOpenAdManager"
    private const val PREFS_NAME = "app_open_ad_prefs"
    private const val KEY_LAST_AD_SHOW_TIME = "last_app_open_ad_timestamp"
    private const val FREQUENCY_CAP_MILLIS = 3600000L // 1 hour in milliseconds

    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd: Boolean = false
    private var isShowingAd: Boolean = false

    /**
     * Loads an app open ad in the background.
     *
     * @param context Application or Activity context
     */
    fun loadAd(context: Context) {
        if (isLoadingAd || isAdAvailable()) {
            Log.d(TAG, "Ad already loading or available, skipping load")
            return
        }

        isLoadingAd = true
        val adUnitId = BuildConfig.ADMOB_APP_OPEN_ID

        val request = AdRequest.Builder().build()

        AppOpenAd.load(
            context.applicationContext,
            adUnitId,
            request,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    Log.d(TAG, "App open ad loaded successfully")
                    appOpenAd = ad
                    isLoadingAd = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e(TAG, "App open ad failed to load: ${error.message} (Code: ${error.code})")
                    appOpenAd = null
                    isLoadingAd = false
                }
            }
        )
    }

    /**
     * Shows the app open ad if it's available and frequency cap allows.
     *
     * @param activity Activity to show the ad in
     */
    fun showAdIfAvailable(activity: Activity) {
        if (isShowingAd) {
            Log.d(TAG, "Ad is already showing, skipping")
            return
        }

        if (!isAdAvailable()) {
            Log.d(TAG, "Ad not available, loading new ad")
            loadAd(activity)
            return
        }

        if (!canShowAd(activity)) {
            Log.d(TAG, "Frequency cap not met, skipping ad display")
            return
        }

        Log.d(TAG, "Showing app open ad")

        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "App open ad dismissed")
                appOpenAd = null
                isShowingAd = false

                // Update last show time and load next ad
                setLastAdShowTime(activity)
                loadAd(activity)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.e(TAG, "App open ad failed to show: ${error.message} (Code: ${error.code})")
                appOpenAd = null
                isShowingAd = false

                // Load next ad for future attempts
                loadAd(activity)
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "App open ad showed full screen content")
                isShowingAd = true
            }
        }

        appOpenAd?.show(activity)
    }

    /**
     * Checks if an ad is currently loaded and available.
     */
    private fun isAdAvailable(): Boolean {
        return appOpenAd != null
    }

    /**
     * Checks if enough time has passed since the last ad was shown.
     *
     * @param context Context to access SharedPreferences
     * @return true if ad can be shown (frequency cap met), false otherwise
     */
    private fun canShowAd(context: Context): Boolean {
        val lastAdTime = getLastAdShowTime(context)
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - lastAdTime

        return elapsedTime >= FREQUENCY_CAP_MILLIS
    }

    /**
     * Gets the timestamp of when the last ad was shown.
     *
     * @param context Context to access SharedPreferences
     * @return Timestamp in milliseconds, or 0 if no ad has been shown yet
     */
    private fun getLastAdShowTime(context: Context): Long {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getLong(KEY_LAST_AD_SHOW_TIME, 0L)
    }

    /**
     * Updates the timestamp of when the last ad was shown to the current time.
     *
     * @param context Context to access SharedPreferences
     */
    private fun setLastAdShowTime(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putLong(KEY_LAST_AD_SHOW_TIME, System.currentTimeMillis()).apply()
        Log.d(TAG, "Updated last ad show time to ${System.currentTimeMillis()}")
    }
}
