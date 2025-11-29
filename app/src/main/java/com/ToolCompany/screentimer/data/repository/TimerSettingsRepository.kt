package com.ToolCompany.screentimer.data.repository

import com.ToolCompany.screentimer.data.model.TimerSettings
import com.ToolCompany.screentimer.data.source.TimerSettingsDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerSettingsRepository @Inject constructor(
    private val dataSource: TimerSettingsDataSource
) {
    suspend fun getTimerSettings(): TimerSettings {
        return dataSource.getTimerSettings()
    }

    private suspend fun saveTimerSettings(settings: TimerSettings) {
        dataSource.saveTimerSettings(settings)
    }

    suspend fun saveLastDuration(duration: Long) {
        val currentSettings = getTimerSettings()
        saveTimerSettings(currentSettings.copy(lastSetDurationByUser = duration))
    }
} 