package com.cihatakyol.sleeptimer.ui.screens.onboarding

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cihatakyol.sleeptimer.receiver.SleepTimerDeviceAdmin
import com.cihatakyol.sleeptimer.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    init {
        checkOnboardingStatus()
    }

    private fun checkOnboardingStatus() {
        val hasCompletedOnboarding = preferencesManager.hasCompletedOnboarding()
        _state.value = _state.value.copy(hasCompletedOnboarding = hasCompletedOnboarding)
    }

    fun isDeviceAdminActive(): Boolean {
//        val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
//        val componentName = ComponentName(context, SleepTimerDeviceAdmin::class.java)
//        return devicePolicyManager.isAdminActive(componentName)
        return false
    }

    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED
            false
        } else {
            true
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            preferencesManager.setCompletedOnboarding(true)
            _state.value = _state.value.copy(hasCompletedOnboarding = true)
        }
    }
}

data class OnboardingState(
    val hasCompletedOnboarding: Boolean = false
) 