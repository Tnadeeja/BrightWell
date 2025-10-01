package com.wellness.brightwell.utils

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.wellness.brightwell.worker.HydrationReminderWorker
import java.util.concurrent.TimeUnit

/**
 * Utility object for scheduling and managing notification reminders
 */
object NotificationScheduler {
    
    private const val HYDRATION_WORK_NAME = "hydration_reminder_work"

    /**
     * Schedule periodic hydration reminders using WorkManager
     * @param context Application context
     * @param intervalMinutes Interval between reminders in minutes
     */
    fun scheduleHydrationReminder(context: Context, intervalMinutes: Int) {
        // Cancel any existing work
        cancelHydrationReminder(context)

        // Create a periodic work request
        // Note: Minimum interval for PeriodicWorkRequest is 15 minutes
        val actualInterval = if (intervalMinutes < 15) 15 else intervalMinutes
        
        val workRequest = PeriodicWorkRequestBuilder<HydrationReminderWorker>(
            actualInterval.toLong(),
            TimeUnit.MINUTES
        ).build()

        // Enqueue the work request
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            HYDRATION_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    /**
     * Cancel all hydration reminder notifications
     * @param context Application context
     */
    fun cancelHydrationReminder(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(HYDRATION_WORK_NAME)
    }
}
