package com.wellness.brightwell.data

/**
 * Data class for habit templates
 */
data class HabitTemplate(
    val id: String,
    val name: String,
    val icon: String,
    val description: String,
    val habits: List<Pair<String, String>> // name to description
) {
    companion object {
        /**
         * Get all predefined habit templates
         */
        fun getAllTemplates(): List<HabitTemplate> {
            return listOf(
                HabitTemplate(
                    id = "morning_routine",
                    name = "Morning Routine",
                    icon = "🌅",
                    description = "Start your day right with healthy morning habits",
                    habits = listOf(
                        "Wake up early" to "Rise before 7 AM",
                        "Drink water" to "Hydrate first thing",
                        "Meditate" to "10 minutes of mindfulness",
                        "Exercise" to "Morning workout or yoga",
                        "Healthy breakfast" to "Nutritious meal"
                    )
                ),
                HabitTemplate(
                    id = "fitness_goals",
                    name = "Fitness Goals",
                    icon = "💪",
                    description = "Build a strong and healthy body",
                    habits = listOf(
                        "Workout" to "30 minutes of exercise",
                        "Drink 8 glasses of water" to "Stay hydrated",
                        "Track calories" to "Monitor food intake",
                        "Stretch" to "Flexibility exercises",
                        "Get 8 hours sleep" to "Rest and recovery"
                    )
                ),
                HabitTemplate(
                    id = "mental_wellness",
                    name = "Mental Wellness",
                    icon = "🧘",
                    description = "Take care of your mental health",
                    habits = listOf(
                        "Meditate" to "Daily mindfulness practice",
                        "Journal" to "Write thoughts and feelings",
                        "Gratitude practice" to "List 3 things you're grateful for",
                        "Deep breathing" to "5 minutes of breathing exercises",
                        "Digital detox" to "1 hour without screens"
                    )
                ),
                HabitTemplate(
                    id = "productivity",
                    name = "Productivity Boost",
                    icon = "🚀",
                    description = "Maximize your daily productivity",
                    habits = listOf(
                        "Plan your day" to "Morning planning session",
                        "Focus time" to "2 hours of deep work",
                        "Take breaks" to "Pomodoro technique",
                        "Review progress" to "Evening reflection",
                        "Prepare for tomorrow" to "Set up next day"
                    )
                ),
                HabitTemplate(
                    id = "healthy_lifestyle",
                    name = "Healthy Lifestyle",
                    icon = "🌱",
                    description = "Comprehensive wellness habits",
                    habits = listOf(
                        "Eat vegetables" to "5 servings per day",
                        "Walk 10,000 steps" to "Daily movement",
                        "Avoid junk food" to "Healthy eating",
                        "Take vitamins" to "Daily supplements",
                        "Sleep by 10 PM" to "Consistent sleep schedule"
                    )
                ),
                HabitTemplate(
                    id = "learning",
                    name = "Continuous Learning",
                    icon = "📚",
                    description = "Develop new skills and knowledge",
                    habits = listOf(
                        "Read 30 minutes" to "Books or articles",
                        "Learn new skill" to "Practice daily",
                        "Watch educational content" to "TED talks or courses",
                        "Practice language" to "Duolingo or similar",
                        "Take notes" to "Document learnings"
                    )
                ),
                HabitTemplate(
                    id = "social_connection",
                    name = "Social Connection",
                    icon = "👥",
                    description = "Build and maintain relationships",
                    habits = listOf(
                        "Call a friend" to "Stay connected",
                        "Family time" to "Quality time with loved ones",
                        "Express gratitude" to "Thank someone",
                        "Help others" to "Random act of kindness",
                        "Social activity" to "Meet people"
                    )
                ),
                HabitTemplate(
                    id = "evening_routine",
                    name = "Evening Routine",
                    icon = "🌙",
                    description = "Wind down and prepare for restful sleep",
                    habits = listOf(
                        "No screens 1 hour before bed" to "Digital sunset",
                        "Skincare routine" to "Self-care",
                        "Read before bed" to "Relaxing activity",
                        "Plan tomorrow" to "Prepare for next day",
                        "Lights out by 10 PM" to "Consistent sleep time"
                    )
                )
            )
        }
    }
}
