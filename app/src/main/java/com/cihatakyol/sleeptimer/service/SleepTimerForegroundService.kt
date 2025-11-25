package com.cihatakyol.sleeptimer.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.ServiceCompat
import com.cihatakyol.sleeptimer.utils.NotificationManager
import com.cihatakyol.sleeptimer.utils.ScreenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SleepTimerForegroundService : Service() {
    companion object {
        const val EXTRA_DURATION = "duration"
        const val EXTRA_REMAINING_TIME = "remaining_time"
        const val EXTRA_IS_ACTIVE = "is_active"
        const val ACTION_COUNTDOWN_UPDATE = "com.cihatakyol.sleeptimer.COUNTDOWN_UPDATE"
        const val ACTION_EXTEND_TIME = "com.cihatakyol.sleeptimer.EXTEND_TIME"
        const val EXTRA_EXTEND_TIME = "extend_time"
        const val PERMISSION_RECEIVE_COUNTDOWN = "com.cihatakyol.sleeptimer.permission.RECEIVE_COUNTDOWN"
        const val ACTION_STOP = "com.cihatakyol.sleeptimer.permission.STOP"
    }

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var screenManager: ScreenManager

    private var countDownTimer: CountDownTimer? = null
    private var remainingTime: Long = 0

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate() {
        super.onCreate()
        startForegroundWithNotification()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startForegroundWithNotification() {
        val notification = createInitialNotification()
        ServiceCompat.startForeground(
            this,
            NotificationManager.NOTIFICATION_ID,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
        )
    }

    private fun createInitialNotification(): Notification {
        return notificationManager.createCountdownNotification(0)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_EXTEND_TIME -> {
                val extendTime = intent.getLongExtra(EXTRA_EXTEND_TIME, 0)
                if (extendTime > 0) {
                    extendCountdown(extendTime)
                }
            }
            ACTION_STOP -> {
                stopCountdown()
            }
            else -> {
                intent?.getLongExtra(EXTRA_DURATION, 0)?.let { duration ->
                    startCountdown(duration)
                }
            }
        }
        return START_STICKY
    }

    private fun extendCountdown(extendTime: Long) {
        countDownTimer?.cancel()
        val newRemainingTime = remainingTime + extendTime
        startCountdown(newRemainingTime)
    }

    private fun startCountdown(duration: Long) {
        countDownTimer?.cancel()
        remainingTime = duration

        countDownTimer = object : CountDownTimer(duration, 10) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                notificationManager.updateNotification(millisUntilFinished)
                broadcastUpdate(millisUntilFinished, true)
            }

            override fun onFinish() {
                screenManager.lockScreen()
                broadcastUpdate(0, false)
                stopSelf()
            }
        }.start()
    }

    private fun broadcastUpdate(remainingTime: Long, isActive: Boolean) {
        val intent = Intent(ACTION_COUNTDOWN_UPDATE).apply {
            putExtra(EXTRA_REMAINING_TIME, remainingTime)
            putExtra(EXTRA_IS_ACTIVE, isActive)
            setPackage(packageName)
        }
        sendBroadcast(intent, PERMISSION_RECEIVE_COUNTDOWN)
    }

    fun stopCountdown() {
        countDownTimer?.cancel()
        stopSelf()
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        broadcastUpdate(0, false)
        notificationManager.cancelNotification()
        super.onDestroy()
    }
} 