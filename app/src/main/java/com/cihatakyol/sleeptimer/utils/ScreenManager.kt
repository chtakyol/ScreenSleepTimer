package com.cihatakyol.sleeptimer.utils

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import com.cihatakyol.sleeptimer.receiver.SleepTimerDeviceAdmin
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScreenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val devicePolicyManager: DevicePolicyManager =
        context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    private val componentName: ComponentName = ComponentName(context, SleepTimerDeviceAdmin::class.java)

    fun isDeviceAdminActive(): Boolean {
        return devicePolicyManager.isAdminActive(componentName)
    }

    fun getComponentName(): ComponentName {
        return componentName
    }

    fun lockScreen() {
        if (isDeviceAdminActive()) {
            devicePolicyManager.lockNow()
        }
    }
} 