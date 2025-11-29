package com.ToolCompany.screentimer.data.source

import android.content.Context
import android.content.SharedPreferences
import com.ToolCompany.screentimer.data.model.TimerSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalTimerSettingsDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : TimerSettingsDataSource {
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override suspend fun getTimerSettings(): TimerSettings = withContext(Dispatchers.IO) {
        TimerSettings(
            lastSetDurationByUser = sharedPreferences.getLong(KEY_LAST_DURATION, DEFAULT_DURATION)
        )
    }

    override suspend fun saveTimerSettings(settings: TimerSettings) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().apply {
                putLong(KEY_LAST_DURATION, settings.lastSetDurationByUser)
                apply()
            }
        }
    }

    companion object {
        private const val PREFS_NAME = "SleepTimerPrefs"
        private const val KEY_LAST_DURATION = "last_duration"
        private const val DEFAULT_DURATION = 0L
    }
} 