package com.wellness.brightwell.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Utility object for date formatting and manipulation
 */
object DateUtils {
    
    private const val DATE_FORMAT = "yyyy-MM-dd"
    private const val DISPLAY_DATE_FORMAT = "MMM dd, yyyy"
    private const val TIME_FORMAT = "hh:mm a"
    private const val DATETIME_FORMAT = "MMM dd, yyyy hh:mm a"
    
    /**
     * Get today's date as a string in "yyyy-MM-dd" format
     * @return Today's date string
     */
    fun getTodayString(): String {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * Format a timestamp to a readable date string
     * @param timestamp Time in milliseconds
     * @return Formatted date string (e.g., "Jan 15, 2025")
     */
    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    /**
     * Format a timestamp to a readable time string
     * @param timestamp Time in milliseconds
     * @return Formatted time string (e.g., "03:30 PM")
     */
    fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    /**
     * Format a timestamp to a readable date and time string
     * @param timestamp Time in milliseconds
     * @return Formatted datetime string (e.g., "Jan 15, 2025 03:30 PM")
     */
    fun formatDateTime(timestamp: Long): String {
        val sdf = SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    /**
     * Get a relative time description (e.g., "Today", "Yesterday", or formatted date)
     * @param timestamp Time in milliseconds
     * @return Relative time string
     */
    fun getRelativeTimeString(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_YEAR)
        val todayYear = calendar.get(Calendar.YEAR)
        
        calendar.timeInMillis = timestamp
        val day = calendar.get(Calendar.DAY_OF_YEAR)
        val year = calendar.get(Calendar.YEAR)
        
        return when {
            day == today && year == todayYear -> "Today at ${formatTime(timestamp)}"
            day == today - 1 && year == todayYear -> "Yesterday at ${formatTime(timestamp)}"
            else -> formatDateTime(timestamp)
        }
    }

    /**
     * Convert a date string to timestamp
     * @param dateString Date in "yyyy-MM-dd" format
     * @return Timestamp in milliseconds
     */
    fun dateStringToTimestamp(dateString: String): Long {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return sdf.parse(dateString)?.time ?: 0L
    }
}
