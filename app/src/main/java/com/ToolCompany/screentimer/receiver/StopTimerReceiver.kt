package com.ToolCompany.screentimer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ToolCompany.screentimer.utils.TimerNotificationHelper
import com.ToolCompany.screentimer.utils.TimerScheduler

class StopTimerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_STOP) {
            TimerScheduler.instance.cancelTimer()
            TimerNotificationHelper.instance.cancelNotification()
        }
    }

    companion object {
        const val ACTION_STOP = "com.ToolCompany.screentimer.STOP_TIMER"
    }
}