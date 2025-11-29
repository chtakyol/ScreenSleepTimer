package com.ToolCompany.screentimer.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ToolCompany.screentimer.MainActivity
import com.ToolCompany.screentimer.R
import com.ToolCompany.screentimer.service.SleepTimerForegroundService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val timeFormatter: TimeFormatter
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "sleep_timer_channel"
        private const val CHANNEL_NAME = "Sleep Timer"
        private const val CHANNEL_DESCRIPTION = "Shows the countdown for sleep timer"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = CHANNEL_DESCRIPTION
            enableLights(true)
            enableVibration(false)
            setShowBadge(true)
            setSound(null, null)
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun createCountdownNotification(remainingTime: Long): Notification {
        // PendingIntent to open MainActivity when the notification is clicked
        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            context,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // PendingIntent for "Extend Time" action (still points to service)
        val extendTimeIntent = Intent(context, SleepTimerForegroundService::class.java).apply {
            action = SleepTimerForegroundService.ACTION_EXTEND_TIME
            putExtra(SleepTimerForegroundService.EXTRA_EXTEND_TIME, 5000L) // 5 seconds
        }
        val extendTimePendingIntent = PendingIntent.getService(
            context,
            1,
            extendTimeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // PendingIntent for "Stop" action (still points to service)
        val stopIntent = Intent(context, SleepTimerForegroundService::class.java).apply {
            action = SleepTimerForegroundService.ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            context,
            2,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(timeFormatter.formatTime(remainingTime))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setOngoing(true)
            .setAutoCancel(false)
            .setOnlyAlertOnce(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setColor(context.getColor(R.color.purple_500))
            .setContentIntent(openAppPendingIntent)
            .addAction(
                R.drawable.ic_notification,
                context.getString(R.string.extend_time),
                extendTimePendingIntent
            )
            .addAction(
                R.drawable.ic_notification,
                "Stop",
                stopPendingIntent
            )
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()
    }

    fun updateNotification(remainingTime: Long) {
        notificationManager.notify(NOTIFICATION_ID, createCountdownNotification(remainingTime))
    }

    fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }
} 