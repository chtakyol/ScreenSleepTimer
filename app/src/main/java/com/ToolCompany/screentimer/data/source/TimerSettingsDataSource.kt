package com.ToolCompany.screentimer.data.source

import com.ToolCompany.screentimer.data.model.TimerSettings

interface TimerSettingsDataSource {
    suspend fun getTimerSettings(): TimerSettings
    suspend fun saveTimerSettings(settings: TimerSettings)
} 