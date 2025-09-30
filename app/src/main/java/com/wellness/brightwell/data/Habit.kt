package com.wellness.brightwell.data

import java.util.UUID

/**
 * Data class representing a daily wellness habit
 * @property id Unique identifier for the habit
 * @property name Name of the habit (e.g., "Drink Water", "Meditate")
 * @property description Optional description of the habit
 * @property createdDate Timestamp when the habit was created
 * @property completionDates Set of dates (in milliseconds) when the habit was completed
 */
data class Habit(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
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
