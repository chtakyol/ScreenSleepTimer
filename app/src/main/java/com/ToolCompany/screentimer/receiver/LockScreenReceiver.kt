package com.ToolCompany.screentimer.receiver

import android.app.admin.DevicePolicyManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.ToolCompany.screentimer.utils.TimerNotificationHelper
import com.ToolCompany.screentimer.utils.TimerScheduler

class LockScreenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = ComponentName(context, SleepTimerDeviceAdmin::class.java)
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.lockNow()
        }

        TimerScheduler.instance.cancelTimer()
        TimerNotificationHelper.instance.cancelNotification()
    }
}