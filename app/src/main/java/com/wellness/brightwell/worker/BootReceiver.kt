package com.wellness.brightwell.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.utils.NotificationScheduler

/**
 * BroadcastReceiver that reschedules hydration reminders after device reboot
 */
class BootReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule hydration reminders if they were enabled
            val prefsManager = PreferencesManager(context)
            if (prefsManager.isHydrationEnabled()) {
                val interval = prefsManager.getHydrationInterval()
                NotificationScheduler.scheduleHydrationReminder(context, interval)
            }
        }
    }
}
