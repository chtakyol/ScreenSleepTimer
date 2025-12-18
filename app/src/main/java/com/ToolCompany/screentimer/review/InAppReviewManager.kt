package com.ToolCompany.screentimer.review

import android.app.Activity
import android.content.Context
import android.util.Log
import com.ToolCompany.screentimer.BuildConfig
import com.ToolCompany.screentimer.data.UserPreferences
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InAppReviewManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferences: UserPreferences
) {
    private val reviewManager: ReviewManager = ReviewManagerFactory.create(context)

    companion object {
        private const val TAG = "InAppReviewManager"
        private const val LAUNCH_COUNT_THRESHOLD = 2
    }

    suspend fun incrementLaunchCount() {
        userPreferences.incrementLaunchCount()
        if (BuildConfig.DEBUG) {
            val count = userPreferences.appLaunchCount.first()
            Log.d(TAG, "Launch count incremented to: $count")
        }
    }

    suspend fun requestReviewIfNeeded(activity: Activity) {
        if (shouldShowReview()) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Requesting review flow...")
            }

            try {
                // Request review flow
                val reviewInfo: ReviewInfo = reviewManager.requestReviewFlow().await()

                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Review flow requested successfully")
                }

                // Launch review flow
                reviewManager.launchReviewFlow(activity, reviewInfo).await()

                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Review flow launched successfully")
                }

                // Mark review as requested
                markReviewRequested()

            } catch (e: Exception) {
                // Handle errors gracefully - don't crash the app
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "Review flow failed", e)
                }

                // Still mark as requested to avoid repeated attempts
                markReviewRequested()
            }
        } else {
            if (BuildConfig.DEBUG) {
                val count = userPreferences.appLaunchCount.first()
                val requested = userPreferences.reviewRequested.first()
                Log.d(TAG, "Review not needed. Launch count: $count, Already requested: $requested")
            }
        }
    }

    private suspend fun shouldShowReview(): Boolean {
        val launchCount = userPreferences.appLaunchCount.first()
        val reviewRequested = userPreferences.reviewRequested.first()

        return launchCount == LAUNCH_COUNT_THRESHOLD && !reviewRequested
    }

    private suspend fun markReviewRequested() {
        userPreferences.setReviewRequested(true)
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Review marked as requested")
        }
    }
}
