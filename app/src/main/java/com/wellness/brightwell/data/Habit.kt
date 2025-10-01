package com.wellness.brightwell.data

import java.util.UUID

/**
 * Data class representing a daily wellness habit
 * @property id Unique identifier for the habit
 * @property name Name of the habit (e.g., "Drink Water", "Meditate")
 * @property description Optional description of the habit
 * @property icon Emoji icon for the habit
 * @property color Color hex code for the habit
 * @property timesPerDay Number of times to complete per day
 * @property reminderTimes List of reminder times in HH:mm format
 * @property frequency Frequency type: "daily", "weekly", "monthly"
 * @property repeatDays For daily: list of day numbers (1-7), weekly: count, monthly: day numbers
 * @property reminderEnabled Whether reminders are enabled
 * @property createdDate Timestamp when the habit was created
 * @property completionDates Set of dates (in milliseconds) when the habit was completed
 */
data class Habit(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val icon: String = "✓",
    val color: String = "#6366F1",
    val timesPerDay: Int = 1,
    val reminderTimes: List<String> = emptyList(),
    val frequency: String = "daily",
    val repeatDays: List<Int> = listOf(1,2,3,4,5,6,7),
    val reminderEnabled: Boolean = false,
    val createdDate: Long = System.currentTimeMillis(),
    val completionDates: MutableSet<String> = mutableSetOf() // Stores dates in "yyyy-MM-dd" format
) {
    /**
     * Check if the habit is completed for a specific date
     * @param date Date string in "yyyy-MM-dd" format
     * @return true if completed on that date
     */
    fun isCompletedOn(date: String): Boolean {
        return completionDates.contains(date)
    }

    /**
     * Toggle completion status for a specific date
     * @param date Date string in "yyyy-MM-dd" format
     */
    fun toggleCompletion(date: String) {
        if (completionDates.contains(date)) {
            completionDates.remove(date)
        } else {
            completionDates.add(date)
        }
    }
}
