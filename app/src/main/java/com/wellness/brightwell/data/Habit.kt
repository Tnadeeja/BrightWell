package com.wellness.brightwell.data

import java.util.UUID

/**
 * Data class representing a wellness habit
 * @param id Unique identifier for the habit
 * @param name Name of the habit (e.g., "Drink Water", "Meditate")
 * @param description Optional description of the habit
 * @param createdAt Timestamp when the habit was created
 * @param completedDates Set of dates (in "yyyy-MM-dd" format) when the habit was completed
 */
data class Habit(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val completedDates: MutableSet<String> = mutableSetOf()
) {
    /**
     * Check if habit is completed for a specific date
     * @param date Date string in "yyyy-MM-dd" format
     * @return true if completed on that date
     */
    fun isCompletedOn(date: String): Boolean {
        return completedDates.contains(date)
    }

    /**
     * Toggle completion status for a specific date
     * @param date Date string in "yyyy-MM-dd" format
     */
    fun toggleCompletion(date: String) {
        if (completedDates.contains(date)) {
            completedDates.remove(date)
        } else {
            completedDates.add(date)
        }
    }
}
