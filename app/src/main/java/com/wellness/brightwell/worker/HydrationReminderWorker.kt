package com.wellness.brightwell.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.wellness.brightwell.MainActivity
import com.wellness.brightwell.R

/**
 * Worker class that handles periodic hydration reminders
 * Uses WorkManager to schedule notifications at user-defined intervals
 */
class HydrationReminderWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    companion object {
        const val CHANNEL_ID = "hydration_reminder_channel"
        const val NOTIFICATION_ID = 1001
        private const val CHANNEL_NAME = "Hydration Reminders"
        private const val CHANNEL_DESCRIPTION = "Notifications to remind you to drink water"
    }

    override fun doWork(): Result {
        // Create notification channel (required for Android 8.0+)
        createNotificationChannel()

        // Show the hydration reminder notification
        showNotification()

        return Result.success()
    }

    /**
     * Create a notification channel for hydration reminders
     * Required for Android 8.0 (API 26) and above
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }

            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Display the hydration reminder notification
     */
    private fun showNotification() {
        // Intent to open the app when notification is tapped
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Build the notification
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_water_drop)
            .setContentTitle("Time to Hydrate! 💧")
            .setContentText("Don't forget to drink water and stay healthy!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Show the notification
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
