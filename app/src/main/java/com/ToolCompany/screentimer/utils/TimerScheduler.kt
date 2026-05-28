package com.ToolCompany.screentimer.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ToolCompany.screentimer.receiver.LockScreenReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val _isTimerRunning = MutableStateFlow(preferencesManager.isTimerActive())
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    companion object {
        private const val REQUEST_CODE_LOCK_SCREEN = 1001
        lateinit var instance: TimerScheduler
            private set
    }

    init {
        instance = this
    }

    fun scheduleTimer(durationMillis: Long) {
        val endTime = System.currentTimeMillis() + durationMillis
        preferencesManager.saveTimerState(endTime)

        val intent = Intent(context, LockScreenReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_LOCK_SCREEN,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endTime, pendingIntent)
        _isTimerRunning.value = true
    }

    fun cancelTimer() {
        val intent = Intent(context, LockScreenReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_LOCK_SCREEN,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        preferencesManager.clearTimerState()
        _isTimerRunning.value = false
    }

    fun getRemainingTime(): Long {
        if (!preferencesManager.isTimerActive()) return 0
        val remaining = preferencesManager.getTimerEndTime() - System.currentTimeMillis()
        return if (remaining > 0) remaining else 0
    }
}