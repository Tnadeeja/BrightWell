package com.wellness.brightwell.data

/**
 * Data class representing an achievement/badge
 */
data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String, // Emoji
    val isUnlocked: Boolean = false,
    val unlockedDate: Long = 0L,
    val progress: Int = 0,
    val target: Int = 1
) {
    companion object {
        // Achievement IDs
        const val FIRST_STEP = "first_step"
        const val HABIT_MASTER = "habit_master"
        const val PERFECT_WEEK = "perfect_week"
        const val HYDRATION_HERO = "hydration_hero"
        const val MOOD_MASTER = "mood_master"
        const val STREAK_KING = "streak_king"
        const val EARLY_BIRD = "early_bird"
        const val CONSISTENCY_CHAMPION = "consistency_champion"
        
        /**
         * Get all available achievements
         */
        fun getAllAchievements(): List<Achievement> {
            return listOf(
                Achievement(
                    id = FIRST_STEP,
                    title = "First Step",
                    description = "Complete your first habit",
                    icon = "🎯",
                    target = 1
                ),
                Achievement(
                    id = HABIT_MASTER,
                    title = "Habit Master",
                    description = "Create 10 habits",
                    icon = "📋",
                    target = 10
                ),
                Achievement(
                    id = PERFECT_WEEK,
                    title = "Perfect Week",
                    description = "Complete all habits for 7 days",
                    icon = "⭐",
                    target = 7
                ),
                Achievement(
                    id = HYDRATION_HERO,
                    title = "Hydration Hero",
                    description = "Reach water goal for 7 days",
                    icon = "💧",
                    target = 7
                ),
                Achievement(
                    id = MOOD_MASTER,
                    title = "Mood Master",
                    description = "Log 30 mood entries",
                    icon = "😊",
                    target = 30
                ),
                Achievement(
                    id = STREAK_KING,
                    title = "Streak King",
                    description = "Maintain a 30-day habit streak",
                    icon = "🔥",
                    target = 30
                ),
                Achievement(
                    id = EARLY_BIRD,
                    title = "Early Bird",
                    description = "Complete habits before 9 AM for 5 days",
                    icon = "🌅",
                    target = 5
                ),
                Achievement(
                    id = CONSISTENCY_CHAMPION,
                    title = "Consistency Champion",
                    description = "Use app for 30 consecutive days",
                    icon = "🏆",
                    target = 30
                )
            )
        }
    }
}
