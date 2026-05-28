package com.ToolCompany.screentimer.receiver

import android.app.admin.DevicePolicyManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.ToolCompany.screentimer.utils.PreferencesManager

class LockScreenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = ComponentName(context, SleepTimerDeviceAdmin::class.java)
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.lockNow()
        }

        val prefs = context.getSharedPreferences(PreferencesManager.PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .remove(PreferencesManager.KEY_TIMER_END_TIME)
            .remove(PreferencesManager.KEY_TIMER_ACTIVE)
            .apply()
    }
}