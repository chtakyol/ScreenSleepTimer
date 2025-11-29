package com.ToolCompany.screentimer.di

import com.ToolCompany.screentimer.data.source.LocalTimerSettingsDataSource
import com.ToolCompany.screentimer.data.source.TimerSettingsDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    
    @Binds
    @Singleton
    abstract fun bindTimerSettingsDataSource(
        localTimerSettingsDataSource: LocalTimerSettingsDataSource
    ): TimerSettingsDataSource
} 