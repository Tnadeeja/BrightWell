package com.wellness.brightwell.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Utility object for date-related operations
 */
object DateUtils {
    
    /**
     * Get today's date in "yyyy-MM-dd" format
     */
    fun getTodayString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * Get current date and time formatted
     */
    fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * Get dates for the last N days
     * @param days Number of days to go back
     * @return List of date strings in "yyyy-MM-dd" format
     */
    fun getLastNDays(days: Int): List<String> {
        val dateList = mutableListOf<String>()
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        
        for (i in 0 until days) {
            dateList.add(sdf.format(calendar.time))
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        
        return dateList
    }

    /**
     * Format timestamp to readable date
     */
    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    /**
     * Format timestamp to readable time
     */
    fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
