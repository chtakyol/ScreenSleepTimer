package com.cihatakyol.sleeptimer.data.repository

import com.cihatakyol.sleeptimer.data.model.TimerSettings
import com.cihatakyol.sleeptimer.data.source.TimerSettingsDataSource
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

    suspend fun saveLastDuration(duration: Int) {
        val currentSettings = getTimerSettings()
        saveTimerSettings(currentSettings.copy(lastDuration = duration))
    }
} 