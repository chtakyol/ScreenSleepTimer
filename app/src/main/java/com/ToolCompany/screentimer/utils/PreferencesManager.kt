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

    fun saveTimerState(endTime: Long) {
        sharedPreferences.edit()
            .putLong(KEY_TIMER_END_TIME, endTime)
            .putBoolean(KEY_TIMER_ACTIVE, true)
            .apply()
    }

    fun isTimerActive(): Boolean {
        return sharedPreferences.getBoolean(KEY_TIMER_ACTIVE, false)
    }

    fun getTimerEndTime(): Long {
        return sharedPreferences.getLong(KEY_TIMER_END_TIME, 0L)
    }

    fun clearTimerState() {
        sharedPreferences.edit()
            .remove(KEY_TIMER_END_TIME)
            .remove(KEY_TIMER_ACTIVE)
            .apply()
    }

    companion object {
        const val PREFS_NAME = "SleepTimerPrefs"
        const val KEY_TIMER_END_TIME = "timer_end_time"
        const val KEY_TIMER_ACTIVE = "timer_active"
        private const val KEY_LAST_DURATION = "last_duration"
        private const val KEY_COMPLETED_ONBOARDING = "completed_onboarding"
        private const val DEFAULT_DURATION = 0
    }
} 