package com.cihatakyol.sleeptimer.data.source

import com.cihatakyol.sleeptimer.data.model.TimerSettings

interface TimerSettingsDataSource {
    suspend fun getTimerSettings(): TimerSettings
    suspend fun saveTimerSettings(settings: TimerSettings)
} 