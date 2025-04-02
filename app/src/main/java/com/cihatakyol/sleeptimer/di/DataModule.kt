package com.cihatakyol.sleeptimer.di

import com.cihatakyol.sleeptimer.data.source.LocalTimerSettingsDataSource
import com.cihatakyol.sleeptimer.data.source.TimerSettingsDataSource
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