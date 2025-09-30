package com.wellness.brightwell.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.wellness.brightwell.data.PreferencesManager

/**
 * BroadcastReceiver to reschedule notifications after device reboot
 */
class BootReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule hydration reminders if they were enabled
            val prefsManager = PreferencesManager(context)
            if (prefsManager.isHydrationEnabled()) {
                NotificationScheduler.scheduleHydrationReminders(context)
            }
        }
    }
}
