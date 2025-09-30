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
 * Widget provider that displays today's habit completion percentage
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
    }

    override fun onDisabled(context: Context) {
        // Called when the last widget is removed
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
            val habits = prefsManager.loadHabits()
            val today = DateUtils.getTodayString()

            // Calculate completion percentage
            val percentage = if (habits.isEmpty()) {
                0
            } else {
                val completedCount = habits.count { it.isCompletedOn(today) }
                (completedCount * 100) / habits.size
            }

            // Create RemoteViews
            val views = RemoteViews(context.packageName, R.layout.widget_habit)

            // Set the progress and text
            views.setProgressBar(R.id.widgetProgressBar, 100, percentage, false)
            views.setTextViewText(R.id.widgetTextPercentage, "$percentage%")
            views.setTextViewText(
                R.id.widgetTextCompleted,
                if (habits.isEmpty()) {
                    "No habits tracked"
                } else {
                    val completedCount = habits.count { it.isCompletedOn(today) }
                    "$completedCount of ${habits.size} completed"
                }
            )

            // Set up click intent to open the app
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
         * Call this method whenever habit data changes
         */
        fun updateWidget(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                android.content.ComponentName(context, HabitWidgetProvider::class.java)
            )

            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }
        }
    }
}
