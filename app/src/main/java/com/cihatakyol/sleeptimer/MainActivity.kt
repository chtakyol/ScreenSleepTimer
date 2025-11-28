package com.cihatakyol.sleeptimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.cihatakyol.sleeptimer.ads.AppOpenAdManager
import com.cihatakyol.sleeptimer.ui.navigation.SleepTimerNavGraph
import com.cihatakyol.sleeptimer.ui.theme.SleepTimerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

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