package com.cihatakyol.sleeptimer

import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.cihatakyol.sleeptimer.receiver.CountdownReceiver
import com.cihatakyol.sleeptimer.service.SleepTimerForegroundService
import com.cihatakyol.sleeptimer.ui.screens.mainscreen.MainScreen
import com.cihatakyol.sleeptimer.ui.theme.SleepTimerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val countdownReceiver = CountdownReceiver()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Register the countdown receiver with proper flags
        val filter = IntentFilter(SleepTimerForegroundService.ACTION_COUNTDOWN_UPDATE)
        val receiverFlags = ContextCompat.RECEIVER_NOT_EXPORTED
        ContextCompat.registerReceiver(
            this,
            countdownReceiver,
            filter,
            receiverFlags
        )

        setContent {
            SleepTimerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(countdownReceiver)
    }
}