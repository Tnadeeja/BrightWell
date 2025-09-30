package com.wellness.brightwell.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.wellness.brightwell.data.PreferencesManager
import java.util.concurrent.TimeUnit

/**
 * Utility class for scheduling and canceling hydration reminder notifications
 */
object NotificationScheduler {
    
    private const val WORK_NAME = "hydration_reminder_work"

    /**
     * Schedule periodic hydration reminders
     * @param context Application context
     */
    fun scheduleHydrationReminders(context: Context) {
        val prefsManager = PreferencesManager(context)
        
        // Check if reminders are enabled
        if (!prefsManager.isHydrationEnabled()) {
            cancelHydrationReminders(context)
            return
        }
        
        // Get reminder interval in minutes
        val intervalMinutes = prefsManager.getHydrationInterval().toLong()
        
        // Create periodic work request
        // Note: Minimum interval for PeriodicWorkRequest is 15 minutes
        val workRequest = PeriodicWorkRequestBuilder<HydrationWorker>(
            intervalMinutes,
            TimeUnit.MINUTES
        ).build()
        
        // Schedule the work
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    /**
     * Cancel all hydration reminders
     * @param context Application context
     */
    fun cancelHydrationReminders(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }

    /**
     * Check if hydration reminders are currently scheduled
     * @param context Application context
     * @return true if reminders are scheduled
     */
    fun areRemindersScheduled(context: Context): Boolean {
        val workInfos = WorkManager.getInstance(context)
            .getWorkInfosForUniqueWork(WORK_NAME)
            .get()
        
        return workInfos.any { !it.state.isFinished }
    }
}
