package com.wellness.brightwell.data

import java.util.UUID

/**
 * Data class representing a mood journal entry
 * @property id Unique identifier for the entry
 * @property emoji Emoji representing the mood (e.g., "😊", "😢", "😡")
 * @property note Optional text note about the mood
 * @property timestamp Time when the mood was logged
 */
data class MoodEntry(
    val id: String = UUID.randomUUID().toString(),
    val emoji: String,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
