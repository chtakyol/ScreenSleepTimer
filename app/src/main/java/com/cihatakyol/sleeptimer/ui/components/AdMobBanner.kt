package com.cihatakyol.sleeptimer.ui.components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.cihatakyol.sleeptimer.BuildConfig
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

/**
 * Reusable AdMob banner ad composable.
 *
 * Displays a standard banner ad (320x50 dp) at the specified position.
 * Automatically uses test ad unit IDs for dev/staging builds and production
 * ad unit IDs for production builds via BuildConfig.
 *
 * Implements silent failure - if ad fails to load, no error is shown to user.
 *
 * @param modifier Modifier for positioning and styling the banner ad container
 *
 * Usage:
 * ```kotlin
 * Scaffold(
 *     bottomBar = { AdMobBanner() }
 * ) {
 *     // Screen content
 * }
 * ```
 */
@Composable
fun AdMobBanner(
    modifier: Modifier = Modifier
) {
    val adUnitId = BuildConfig.ADMOB_BANNER_ID

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                this.adUnitId = adUnitId

                // Add ad listener for logging
                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        Log.d(TAG, "Banner ad loaded successfully")
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        // Silent failure - just log the error
                        Log.e(TAG, "Banner ad failed to load: ${error.message} (Code: ${error.code})")
                    }

                    override fun onAdOpened() {
                        Log.d(TAG, "Banner ad opened")
                    }

                    override fun onAdClosed() {
                        Log.d(TAG, "Banner ad closed")
                    }
                }

                // Load the ad
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

private const val TAG = "AdMobBanner"