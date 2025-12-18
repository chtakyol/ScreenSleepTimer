package com.ToolCompany.screentimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.ToolCompany.screentimer.ads.AppOpenAdManager
import com.ToolCompany.screentimer.review.InAppReviewManager
import com.ToolCompany.screentimer.ui.navigation.SleepTimerNavGraph
import com.ToolCompany.screentimer.ui.theme.SleepTimerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var inAppReviewManager: InAppReviewManager
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Handle in-app review logic
        lifecycleScope.launch {
            inAppReviewManager.incrementLaunchCount()
            inAppReviewManager.requestReviewIfNeeded(this@MainActivity)
        }

        // Show app open ad on cold start (frequency cap handled by AppOpenAdManager)
        AppOpenAdManager.showAdIfAvailable(this)

        var keepSplashScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }

        setContent {
            SleepTimerTheme {
                SleepTimerNavGraph(
                    onSplashScreenFinished = {
                        keepSplashScreen = false
                    }
                )
            }
        }
    }
}