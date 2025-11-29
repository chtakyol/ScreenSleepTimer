package com.ToolCompany.screentimer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ToolCompany.screentimer.service.SleepTimerForegroundService

class CountdownReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            SleepTimerForegroundService.ACTION_COUNTDOWN_UPDATE -> {
                val remainingTime = intent.getLongExtra(SleepTimerForegroundService.EXTRA_REMAINING_TIME, 0)
                val isActive = intent.getBooleanExtra(SleepTimerForegroundService.EXTRA_IS_ACTIVE, false)
                onCountdownUpdate?.invoke(remainingTime, isActive)
            }
        }
    }

    companion object {
        var onCountdownUpdate: ((Long, Boolean) -> Unit)? = null
    }
} 