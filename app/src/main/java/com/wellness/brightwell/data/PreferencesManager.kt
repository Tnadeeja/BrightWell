package com.wellness.brightwell.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Manager class for handling data persistence using SharedPreferences
 * Stores habits, mood entries, and user settings
 */
class PreferencesManager(context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "brightwell_prefs"
        private const val KEY_HABITS = "habits"
        private const val KEY_MOODS = "moods"
        private const val KEY_HYDRATION_ENTRIES = "hydration_entries"
        private const val KEY_HYDRATION_GOAL = "hydration_goal"
        private const val KEY_HYDRATION_INTERVAL = "hydration_interval"
        private const val KEY_HYDRATION_ENABLED = "hydration_enabled"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        
        // Default hydration reminder interval in minutes
        const val DEFAULT_HYDRATION_INTERVAL = 60
        // Default daily hydration goal in milliliters (2 liters)
        const val DEFAULT_HYDRATION_GOAL = 2000
    }

    // ==================== Habit Management ====================
    
    /**
     * Save the list of habits to SharedPreferences
     * @param habits List of habits to save
     */
    fun saveHabits(habits: List<Habit>) {
        val json = gson.toJson(habits)
        sharedPreferences.edit().putString(KEY_HABITS, json).apply()
    }

    /**
     * Load the list of habits from SharedPreferences
     * @return List of saved habits, or empty list if none exist
     */
    fun loadHabits(): MutableList<Habit> {
        val json = sharedPreferences.getString(KEY_HABITS, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Habit>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    /**
     * Add a new habit
     * @param habit Habit to add
     */
    fun addHabit(habit: Habit) {
        val habits = loadHabits()
        habits.add(habit)
        saveHabits(habits)
    }

    /**
     * Update an existing habit
     * @param habit Updated habit
     */
    fun updateHabit(habit: Habit) {
        val habits = loadHabits()
        val index = habits.indexOfFirst { it.id == habit.id }
        if (index != -1) {
            habits[index] = habit
            saveHabits(habits)
        }
    }

    /**
     * Delete a habit by ID
     * @param habitId ID of the habit to delete
     */
    fun deleteHabit(habitId: String) {
        val habits = loadHabits()
        habits.removeAll { it.id == habitId }
        saveHabits(habits)
    }

    // ==================== Mood Entry Management ====================

    /**
     * Save the list of mood entries to SharedPreferences
     * @param moods List of mood entries to save
     */
    fun saveMoods(moods: List<MoodEntry>) {
        val json = gson.toJson(moods)
        sharedPreferences.edit().putString(KEY_MOODS, json).apply()
    }

    /**
     * Load the list of mood entries from SharedPreferences
     * @return List of saved mood entries, sorted by timestamp (newest first)
     */
    fun loadMoods(): MutableList<MoodEntry> {
        val json = sharedPreferences.getString(KEY_MOODS, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<MoodEntry>>() {}.type
        val moods: MutableList<MoodEntry> = gson.fromJson(json, type) ?: mutableListOf()
        return moods.sortedByDescending { it.timestamp }.toMutableList()
    }

    /**
     * Add a new mood entry
     * @param mood Mood entry to add
     */
    fun addMood(mood: MoodEntry) {
        val moods = loadMoods()
        moods.add(0, mood) // Add to beginning
        saveMoods(moods)
    }

    /**
     * Delete a mood entry by ID
     * @param moodId ID of the mood entry to delete
     */
    fun deleteMood(moodId: String) {
        val moods = loadMoods()
        moods.removeAll { it.id == moodId }
        saveMoods(moods)
    }

    // ==================== Settings Management ====================

    /**
     * Get the hydration reminder interval in minutes
     * @return Interval in minutes
     */
    fun getHydrationInterval(): Int {
        return sharedPreferences.getInt(KEY_HYDRATION_INTERVAL, DEFAULT_HYDRATION_INTERVAL)
    }

    /**
     * Set the hydration reminder interval
     * @param minutes Interval in minutes
     */
    fun setHydrationInterval(minutes: Int) {
        sharedPreferences.edit().putInt(KEY_HYDRATION_INTERVAL, minutes).apply()
    }

    /**
     * Check if hydration reminders are enabled
     * @return true if enabled
     */
    fun isHydrationEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_HYDRATION_ENABLED, true)
    }

    /**
     * Enable or disable hydration reminders
     * @param enabled true to enable, false to disable
     */
    fun setHydrationEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_HYDRATION_ENABLED, enabled).apply()
    }

    /**
     * Check if this is the first app launch
     * @return true if first launch
     */
    fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true)
    }

    /**
     * Mark that the app has been launched
     */
    fun setFirstLaunchComplete() {
        sharedPreferences.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
    }

    // ==================== Hydration Entry Management ====================

    /**
     * Save the list of hydration entries to SharedPreferences
     * @param entries List of hydration entries to save
     */
    fun saveHydrationEntries(entries: List<HydrationEntry>) {
        val json = gson.toJson(entries)
        sharedPreferences.edit().putString(KEY_HYDRATION_ENTRIES, json).apply()
    }

    /**
     * Load the list of hydration entries from SharedPreferences
     * @return List of saved hydration entries, sorted by timestamp (newest first)
     */
    fun loadHydrationEntries(): MutableList<HydrationEntry> {
        val json = sharedPreferences.getString(KEY_HYDRATION_ENTRIES, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<HydrationEntry>>() {}.type
        val entries: MutableList<HydrationEntry> = gson.fromJson(json, type) ?: mutableListOf()
        return entries.sortedByDescending { it.timestamp }.toMutableList()
    }

    /**
     * Add a new hydration entry
     * @param entry Hydration entry to add
     */
    fun addHydrationEntry(entry: HydrationEntry) {
        val entries = loadHydrationEntries()
        entries.add(0, entry) // Add to beginning
        saveHydrationEntries(entries)
    }

    /**
     * Delete a hydration entry by ID
     * @param entryId ID of the hydration entry to delete
     */
    fun deleteHydrationEntry(entryId: String) {
        val entries = loadHydrationEntries()
        entries.removeAll { it.id == entryId }
        saveHydrationEntries(entries)
    }

    /**
     * Get the daily hydration goal in milliliters
     * @return Goal in milliliters
     */
    fun getHydrationGoal(): Int {
        return sharedPreferences.getInt(KEY_HYDRATION_GOAL, DEFAULT_HYDRATION_GOAL)
    }

    /**
     * Set the daily hydration goal
     * @param goal Goal in milliliters
     */
    fun setHydrationGoal(goal: Int) {
        sharedPreferences.edit().putInt(KEY_HYDRATION_GOAL, goal).apply()
    }
}
