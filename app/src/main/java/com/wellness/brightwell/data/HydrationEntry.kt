package com.wellness.brightwell.data

import java.util.UUID

/**
 * Data class representing a water intake entry
 * @property id Unique identifier for the entry
 * @property amount Amount of water in ml
 * @property timestamp Time when water was logged
 * @property date Date string in "yyyy-MM-dd" format for grouping
 */
data class HydrationEntry(
    val id: String = UUID.randomUUID().toString(),
    val amount: Int, // in milliliters
    val timestamp: Long = System.currentTimeMillis(),
    val date: String // yyyy-MM-dd format
)
