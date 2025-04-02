package com.cihatakyol.sleeptimer.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.cihatakyol.sleeptimer.service.SleepTimerForegroundService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _isServiceRunning = MutableStateFlow(false)
    val isServiceRunning: StateFlow<Boolean> = _isServiceRunning

    @RequiresApi(Build.VERSION_CODES.O)
    fun startTimer(duration: Long) {
        val intent = Intent(context, SleepTimerForegroundService::class.java).apply {
            putExtra(SleepTimerForegroundService.EXTRA_DURATION, duration)
        }
        context.startForegroundService(intent)
        _isServiceRunning.value = true
    }

    fun stopTimer() {
        context.stopService(Intent(context, SleepTimerForegroundService::class.java))
        _isServiceRunning.value = false
    }
} 