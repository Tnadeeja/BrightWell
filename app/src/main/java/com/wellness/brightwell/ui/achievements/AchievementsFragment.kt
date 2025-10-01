package com.wellness.brightwell.ui.achievements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.wellness.brightwell.data.Achievement
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.databinding.FragmentAchievementsBinding
import com.wellness.brightwell.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment for displaying achievements and badges
 */
class AchievementsFragment : Fragment() {

    private var _binding: FragmentAchievementsBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefsManager: PreferencesManager
    private lateinit var adapter: AchievementAdapter
    private val achievements = mutableListOf<Achievement>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAchievementsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefsManager = PreferencesManager(requireContext())

        setupRecyclerView()
        loadAchievements()
        updateStats()
    }

    /**
     * Set up RecyclerView with grid layout
     */
    private fun setupRecyclerView() {
        adapter = AchievementAdapter(achievements)
        binding.recyclerViewAchievements.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewAchievements.adapter = adapter
    }

    /**
     * Load and check achievements
     */
    private fun loadAchievements() {
        achievements.clear()
        
        val allAchievements = Achievement.getAllAchievements()
        
        allAchievements.forEach { achievement ->
            val updatedAchievement = checkAchievement(achievement)
            achievements.add(updatedAchievement)
        }
        
        adapter.notifyDataSetChanged()
    }

    /**
     * Check if achievement is unlocked
     */
    private fun checkAchievement(achievement: Achievement): Achievement {
        return when (achievement.id) {
            Achievement.FIRST_STEP -> checkFirstStep(achievement)
            Achievement.HABIT_MASTER -> checkHabitMaster(achievement)
            Achievement.PERFECT_WEEK -> checkPerfectWeek(achievement)
            Achievement.HYDRATION_HERO -> checkHydrationHero(achievement)
            Achievement.MOOD_MASTER -> checkMoodMaster(achievement)
            Achievement.STREAK_KING -> checkStreakKing(achievement)
            Achievement.EARLY_BIRD -> checkEarlyBird(achievement)
            Achievement.CONSISTENCY_CHAMPION -> checkConsistencyChampion(achievement)
            else -> achievement
        }
    }

    /**
     * Check First Step achievement
     */
    private fun checkFirstStep(achievement: Achievement): Achievement {
        val habits = prefsManager.loadHabits()
        val today = DateUtils.getTodayString()
        val completedCount = habits.count { it.isCompletedOn(today) }
        
        return if (completedCount > 0) {
            achievement.copy(isUnlocked = true, progress = 1)
        } else {
            achievement.copy(progress = 0)
        }
    }

    /**
     * Check Habit Master achievement
     */
    private fun checkHabitMaster(achievement: Achievement): Achievement {
        val habits = prefsManager.loadHabits()
        val progress = habits.size
        
        return if (progress >= achievement.target) {
            achievement.copy(isUnlocked = true, progress = progress)
        } else {
            achievement.copy(progress = progress)
        }
    }

    /**
     * Check Perfect Week achievement
     */
    private fun checkPerfectWeek(achievement: Achievement): Achievement {
        val habits = prefsManager.loadHabits()
        if (habits.isEmpty()) return achievement.copy(progress = 0)
        
        val calendar = Calendar.getInstance()
        var perfectDays = 0
        
        for (i in 0 until 7) {
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            val completed = habits.count { it.isCompletedOn(dateString) }
            if (completed == habits.size) perfectDays++
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        
        return if (perfectDays >= achievement.target) {
            achievement.copy(isUnlocked = true, progress = perfectDays)
        } else {
            achievement.copy(progress = perfectDays)
        }
    }

    /**
     * Check Hydration Hero achievement
     */
    private fun checkHydrationHero(achievement: Achievement): Achievement {
        val entries = prefsManager.loadHydrationEntries()
        val goal = prefsManager.getHydrationGoal()
        val calendar = Calendar.getInstance()
        var daysAchieved = 0
        
        for (i in 0 until 7) {
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            val dayTotal = entries.filter { it.date == dateString }.sumOf { it.amount }
            if (dayTotal >= goal) daysAchieved++
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        
        return if (daysAchieved >= achievement.target) {
            achievement.copy(isUnlocked = true, progress = daysAchieved)
        } else {
            achievement.copy(progress = daysAchieved)
        }
    }

    /**
     * Check Mood Master achievement
     */
    private fun checkMoodMaster(achievement: Achievement): Achievement {
        val moods = prefsManager.loadMoods()
        val progress = moods.size
        
        return if (progress >= achievement.target) {
            achievement.copy(isUnlocked = true, progress = progress)
        } else {
            achievement.copy(progress = progress)
        }
    }

    /**
     * Check Streak King achievement
     */
    private fun checkStreakKing(achievement: Achievement): Achievement {
        val habits = prefsManager.loadHabits()
        val longestStreak = habits.maxOfOrNull { calculateLongestStreak(it) } ?: 0
        
        return if (longestStreak >= achievement.target) {
            achievement.copy(isUnlocked = true, progress = longestStreak)
        } else {
            achievement.copy(progress = longestStreak)
        }
    }

    /**
     * Check Early Bird achievement
     */
    private fun checkEarlyBird(achievement: Achievement): Achievement {
        // Simplified - just check if habits are completed
        val habits = prefsManager.loadHabits()
        val calendar = Calendar.getInstance()
        var earlyDays = 0
        
        for (i in 0 until 7) {
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            val completed = habits.count { it.isCompletedOn(dateString) }
            if (completed > 0) earlyDays++
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        
        return if (earlyDays >= achievement.target) {
            achievement.copy(isUnlocked = true, progress = earlyDays)
        } else {
            achievement.copy(progress = earlyDays)
        }
    }

    /**
     * Check Consistency Champion achievement
     */
    private fun checkConsistencyChampion(achievement: Achievement): Achievement {
        val habits = prefsManager.loadHabits()
        val calendar = Calendar.getInstance()
        var consistentDays = 0
        
        for (i in 0 until 30) {
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            val completed = habits.count { it.isCompletedOn(dateString) }
            if (completed > 0) consistentDays++
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        
        return if (consistentDays >= achievement.target) {
            achievement.copy(isUnlocked = true, progress = consistentDays)
        } else {
            achievement.copy(progress = consistentDays)
        }
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
     * Update achievement statistics
     */
    private fun updateStats() {
        val unlockedCount = achievements.count { it.isUnlocked }
        val totalCount = achievements.size
        val percentage = if (totalCount > 0) (unlockedCount * 100) / totalCount else 0
        
        binding.textUnlockedCount.text = "$unlockedCount / $totalCount"
        binding.textCompletionPercentage.text = "$percentage%"
        binding.progressBarAchievements.progress = percentage
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
