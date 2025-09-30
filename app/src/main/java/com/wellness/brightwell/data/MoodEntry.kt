package com.wellness.brightwell.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * Data class representing a mood journal entry
 * @param id Unique identifier for the entry
 * @param emoji Emoji representing the mood
 * @param note Optional text note about the mood
 * @param timestamp Time when the mood was logged
 */
data class MoodEntry(
    val id: String = UUID.randomUUID().toString(),
    val emoji: String,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Get formatted date string
     * @return Date in "MMM dd, yyyy" format
     */
    fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    /**
     * Get formatted time string
     * @return Time in "hh:mm a" format
     */
    fun getFormattedTime(): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    /**
     * Get date key for grouping (yyyy-MM-dd format)
     */
    fun getDateKey(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
