package com.wellness.brightwell.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.databinding.FragmentStatsBinding
import com.wellness.brightwell.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment for displaying statistics and insights
 * Shows habit completion trends, mood patterns, and hydration stats
 */
class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefsManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefsManager = PreferencesManager(requireContext())

        loadStatistics()
    }

    /**
     * Load and display all statistics
     */
    private fun loadStatistics() {
        loadHabitStats()
        loadMoodStats()
        loadHydrationStats()
        loadStreakStats()
    }

    /**
     * Calculate and display habit statistics
     */
    private fun loadHabitStats() {
        val habits = prefsManager.loadHabits()
        val today = DateUtils.getTodayString()
        
        // Total habits
        binding.textTotalHabits.text = habits.size.toString()
        
        // Completed today
        val completedToday = habits.count { it.isCompletedOn(today) }
        binding.textCompletedToday.text = completedToday.toString()
        
        // Completion rate (last 7 days)
        val completionRate = calculateWeeklyCompletionRate(habits)
        binding.textCompletionRate.text = "$completionRate%"
        
        // Best day
        val bestDay = findBestDay(habits)
        binding.textBestDay.text = bestDay
    }

    /**
     * Calculate weekly completion rate
     */
    private fun calculateWeeklyCompletionRate(habits: List<com.wellness.brightwell.data.Habit>): Int {
        if (habits.isEmpty()) return 0
        
        val calendar = Calendar.getInstance()
        var totalPossible = 0
        var totalCompleted = 0
        
        // Check last 7 days
        for (i in 0 until 7) {
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            totalPossible += habits.size
            totalCompleted += habits.count { it.isCompletedOn(dateString) }
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        
        return if (totalPossible > 0) (totalCompleted * 100) / totalPossible else 0
    }

    /**
     * Find the day with best completion
     */
    private fun findBestDay(habits: List<com.wellness.brightwell.data.Habit>): String {
        if (habits.isEmpty()) return "N/A"
        
        val calendar = Calendar.getInstance()
        val dayCompletions = mutableMapOf<String, Int>()
        
        // Check last 30 days
        for (i in 0 until 30) {
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            val completed = habits.count { it.isCompletedOn(dateString) }
            if (completed == habits.size && habits.isNotEmpty()) {
                return DateUtils.formatDate(calendar.timeInMillis)
            }
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        
        return "Keep going!"
    }

    /**
     * Calculate and display mood statistics
     */
    private fun loadMoodStats() {
        val moods = prefsManager.loadMoods()
        
        // Total mood entries
        binding.textTotalMoods.text = moods.size.toString()
        
        // Most common mood
        val mostCommon = moods.groupingBy { it.emoji }.eachCount().maxByOrNull { it.value }
        binding.textMostCommonMood.text = mostCommon?.key ?: "😊"
        
        // This week's moods
        val weekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
        val thisWeekMoods = moods.count { it.timestamp >= weekAgo }
        binding.textMoodsThisWeek.text = thisWeekMoods.toString()
    }

    /**
     * Calculate and display hydration statistics
     */
    private fun loadHydrationStats() {
        val entries = prefsManager.loadHydrationEntries()
        val today = DateUtils.getTodayString()
        val goal = prefsManager.getHydrationGoal()
        
        // Today's intake
        val todayIntake = entries.filter { it.date == today }.sumOf { it.amount }
        binding.textTodayHydration.text = "$todayIntake ml"
        
        // Average daily intake (last 7 days)
        val avgIntake = calculateAverageHydration(entries)
        binding.textAvgHydration.text = "$avgIntake ml"
        
        // Days goal achieved (last 7 days)
        val daysAchieved = countDaysGoalAchieved(entries, goal)
        binding.textDaysGoalAchieved.text = "$daysAchieved / 7 days"
    }

    /**
     * Calculate average daily hydration
     */
    private fun calculateAverageHydration(entries: List<com.wellness.brightwell.data.HydrationEntry>): Int {
        val calendar = Calendar.getInstance()
        val dailyTotals = mutableListOf<Int>()
        
        for (i in 0 until 7) {
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            val dayTotal = entries.filter { it.date == dateString }.sumOf { it.amount }
            dailyTotals.add(dayTotal)
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        
        return if (dailyTotals.isNotEmpty()) dailyTotals.average().toInt() else 0
    }

    /**
     * Count days where hydration goal was achieved
     */
    private fun countDaysGoalAchieved(entries: List<com.wellness.brightwell.data.HydrationEntry>, goal: Int): Int {
        val calendar = Calendar.getInstance()
        var count = 0
        
        for (i in 0 until 7) {
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            val dayTotal = entries.filter { it.date == dateString }.sumOf { it.amount }
            if (dayTotal >= goal) count++
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        
        return count
    }

    /**
     * Calculate and display streak statistics
     */
    private fun loadStreakStats() {
        val habits = prefsManager.loadHabits()
        
        // Longest streak
        val longestStreak = habits.maxOfOrNull { calculateLongestStreak(it) } ?: 0
        binding.textLongestStreak.text = "$longestStreak days"
        
        // Current streak
        val currentStreak = habits.maxOfOrNull { calculateCurrentStreak(it) } ?: 0
        binding.textCurrentStreak.text = "$currentStreak days"
        
        // Perfect days (all habits completed)
        val perfectDays = countPerfectDays(habits)
        binding.textPerfectDays.text = "$perfectDays days"
    }

    /**
     * Calculate longest streak for a habit
     */
    private fun calculateLongestStreak(habit: com.wellness.brightwell.data.Habit): Int {
        var longestStreak = 0
        var currentStreak = 0
        val calendar = Calendar.getInstance()
        
        for (i in 0 until 365) {
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            if (habit.isCompletedOn(dateString)) {
                currentStreak++
                longestStreak = maxOf(longestStreak, currentStreak)
            } else {
                currentStreak = 0
            }
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        
        return longestStreak
    }

    /**
     * Calculate current streak for a habit
     */
    private fun calculateCurrentStreak(habit: com.wellness.brightwell.data.Habit): Int {
        var streak = 0
        val calendar = Calendar.getInstance()
        
        while (true) {
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            if (habit.isCompletedOn(dateString)) {
                streak++
                calendar.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                break
            }
        }
        
        return streak
    }

    /**
     * Count perfect days (all habits completed)
     */
    private fun countPerfectDays(habits: List<com.wellness.brightwell.data.Habit>): Int {
        if (habits.isEmpty()) return 0
        
        val calendar = Calendar.getInstance()
        var perfectDays = 0
        
        for (i in 0 until 30) {
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            val completed = habits.count { it.isCompletedOn(dateString) }
            if (completed == habits.size) perfectDays++
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        
        return perfectDays
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
