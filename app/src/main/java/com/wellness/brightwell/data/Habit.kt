package com.wellness.brightwell.data

import java.util.UUID

/**
 * Data class representing a daily wellness habit
 * @property id Unique identifier for the habit
 * @property name Name of the habit (e.g., "Drink Water", "Meditate")
 * @property description Optional description of the habit
 * @property frequency Frequency type: "Daily", "Weekly", "Custom"
 * @property customDays For custom frequency: selected days (Mon, Tue, etc.)
 * @property reminderEnabled Whether reminders are enabled
 * @property category Category of habit (Health, Study, Work, etc.)
 * @property difficulty Difficulty level: "Easy", "Medium", "Hard"
 * @property color Color hex code for the habit
 * @property icon Emoji icon for the habit
 * @property createdDate Timestamp when the habit was created
 * @property completionDates Set of dates when the habit was completed
 */
data class Habit(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val frequency: String = "Daily",
    val customDays: List<String> = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
    val reminderEnabled: Boolean = false,
    val category: String = "Health",
    val difficulty: String = "Medium",
    val color: String = "#6366F1",
    val icon: String = "✓",
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
