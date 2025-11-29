package com.ToolCompany.screentimer.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveLastDuration(seconds: Int) {
        sharedPreferences.edit().putInt(KEY_LAST_DURATION, seconds).apply()
    }

    fun getLastDuration(): Int {
        return sharedPreferences.getInt(KEY_LAST_DURATION, DEFAULT_DURATION)
    }
    
    fun hasCompletedOnboarding(): Boolean {
        return sharedPreferences.getBoolean(KEY_COMPLETED_ONBOARDING, false)
    }
    
    fun setCompletedOnboarding(completed: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_COMPLETED_ONBOARDING, completed).apply()
    }

    companion object {
        private const val PREFS_NAME = "SleepTimerPrefs"
        private const val KEY_LAST_DURATION = "last_duration"
        private const val KEY_COMPLETED_ONBOARDING = "completed_onboarding"
        private const val DEFAULT_DURATION = 0
    }
} 