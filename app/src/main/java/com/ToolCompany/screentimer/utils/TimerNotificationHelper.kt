package com.ToolCompany.screentimer.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ToolCompany.screentimer.MainActivity
import com.ToolCompany.screentimer.R
import com.ToolCompany.screentimer.receiver.StopTimerReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerNotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val timeFormatter: TimeFormatter
) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

    companion object {
        const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "sleep_timer_channel"
        private const val CHANNEL_NAME = "Sleep Timer"
        private const val CHANNEL_DESCRIPTION = "Shows the countdown for sleep timer"

        lateinit var instance: TimerNotificationHelper
            private set
    }

    init {
        instance = this
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            android.app.NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = CHANNEL_DESCRIPTION
            enableLights(false)
            enableVibration(false)
            setShowBadge(false)
            setSound(null, null)
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun showNotification(remainingTime: Long) {
        if (!hasNotificationPermission()) return
        notificationManager.notify(NOTIFICATION_ID, buildNotification(remainingTime))
    }

    fun updateNotification(remainingTime: Long) {
        if (!hasNotificationPermission()) return
        notificationManager.notify(NOTIFICATION_ID, buildNotification(remainingTime))
    }

    fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun buildNotification(remainingTime: Long): Notification {
        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            context,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(context, StopTimerReceiver::class.java).apply {
            action = StopTimerReceiver.ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(timeFormatter.formatTime(remainingTime))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setOngoing(true)
            .setAutoCancel(false)
            .setOnlyAlertOnce(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(openAppPendingIntent)
            .addAction(
                R.drawable.ic_notification,
                context.getString(R.string.stop_timer),
                stopPendingIntent
            )
            .build()
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }
}