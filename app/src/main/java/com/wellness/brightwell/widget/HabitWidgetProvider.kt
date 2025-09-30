package com.wellness.brightwell.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.wellness.brightwell.MainActivity
import com.wellness.brightwell.R
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.utils.DateUtils

/**
 * Home screen widget showing today's habit completion percentage
 * This is the advanced feature for the lab exam
 */
class HabitWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Update all widget instances
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Called when the first widget is created
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context) {
        // Called when the last widget is removed
        super.onDisabled(context)
    }

    companion object {
        /**
         * Update a single widget instance
         */
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val prefsManager = PreferencesManager(context)
            val habits = prefsManager.getHabits()
            
            // Calculate today's completion percentage
            val today = DateUtils.getTodayString()
            val totalHabits = habits.size
            val completedHabits = habits.count { it.isCompletedOn(today) }
            val percentage = if (totalHabits > 0) {
                (completedHabits * 100) / totalHabits
            } else {
                0
            }
            
            // Create RemoteViews
            val views = RemoteViews(context.packageName, R.layout.widget_habit)
            
            // Update widget content
            views.setTextViewText(R.id.widgetPercentage, "$percentage%")
            views.setTextViewText(
                R.id.widgetDetails,
                "$completedHabits of $totalHabits habits"
            )
            views.setProgressBar(R.id.widgetProgressBar, 100, percentage, false)
            
            // Set emoji based on completion percentage
            val emoji = when {
                percentage == 0 -> "😴"
                percentage < 30 -> "😊"
                percentage < 60 -> "🌟"
                percentage < 90 -> "🔥"
                else -> "🎉"
            }
            views.setTextViewText(R.id.widgetEmoji, emoji)
            
            // Create intent to open app when widget is clicked
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.widgetContainer, pendingIntent)
            
            // Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        /**
         * Update all widget instances
         */
        fun updateAllWidgets(context: Context) {
            val intent = Intent(context, HabitWidgetProvider::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(
                android.content.ComponentName(context, HabitWidgetProvider::class.java)
            )
            
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            context.sendBroadcast(intent)
        }
    }
}
