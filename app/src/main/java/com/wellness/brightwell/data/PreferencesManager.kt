package com.wellness.brightwell.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Manager class for handling all SharedPreferences operations
 * Provides methods to save and retrieve habits, mood entries, and app settings
 */
class PreferencesManager(context: Context) {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "BrightWellPrefs"
        private const val KEY_HABITS = "habits"
        private const val KEY_MOOD_ENTRIES = "mood_entries"
        private const val KEY_HYDRATION_INTERVAL = "hydration_interval"
        private const val KEY_HYDRATION_ENABLED = "hydration_enabled"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        
        // Default hydration reminder interval in minutes
        const val DEFAULT_HYDRATION_INTERVAL = 120 // 2 hours
    }

    // ==================== Habits Management ====================

    /**
     * Save list of habits to SharedPreferences
     * @param habits List of Habit objects to save
     */
    fun saveHabits(habits: List<Habit>) {
        val json = gson.toJson(habits)
        prefs.edit().putString(KEY_HABITS, json).apply()
    }

    /**
     * Retrieve list of habits from SharedPreferences
     * @return List of Habit objects, empty list if none saved
     */
    fun getHabits(): MutableList<Habit> {
        val json = prefs.getString(KEY_HABITS, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Habit>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    /**
     * Add a new habit
     * @param habit Habit to add
     */
    fun addHabit(habit: Habit) {
        val habits = getHabits()
        habits.add(habit)
        saveHabits(habits)
    }

    /**
     * Update an existing habit
     * @param habit Updated habit object
     */
    fun updateHabit(habit: Habit) {
        val habits = getHabits()
        val index = habits.indexOfFirst { it.id == habit.id }
        if (index != -1) {
            habits[index] = habit
            saveHabits(habits)
        }
    }

    /**
     * Delete a habit by ID
     * @param habitId ID of habit to delete
     */
    fun deleteHabit(habitId: String) {
        val habits = getHabits()
        habits.removeAll { it.id == habitId }
        saveHabits(habits)
    }

    // ==================== Mood Entries Management ====================

    /**
     * Save list of mood entries to SharedPreferences
     * @param entries List of MoodEntry objects to save
     */
    fun saveMoodEntries(entries: List<MoodEntry>) {
        val json = gson.toJson(entries)
        prefs.edit().putString(KEY_MOOD_ENTRIES, json).apply()
    }

    /**
     * Retrieve list of mood entries from SharedPreferences
     * @return List of MoodEntry objects, sorted by timestamp (newest first)
     */
    fun getMoodEntries(): MutableList<MoodEntry> {
        val json = prefs.getString(KEY_MOOD_ENTRIES, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<MoodEntry>>() {}.type
        val entries: MutableList<MoodEntry> = gson.fromJson(json, type) ?: mutableListOf()
        return entries.sortedByDescending { it.timestamp }.toMutableList()
    }

    /**
     * Add a new mood entry
     * @param entry MoodEntry to add
     */
    fun addMoodEntry(entry: MoodEntry) {
        val entries = getMoodEntries()
        entries.add(0, entry) // Add to beginning
        saveMoodEntries(entries)
    }

    /**
     * Delete a mood entry by ID
     * @param entryId ID of entry to delete
     */
    fun deleteMoodEntry(entryId: String) {
        val entries = getMoodEntries()
        entries.removeAll { it.id == entryId }
        saveMoodEntries(entries)
    }

    // ==================== Settings Management ====================

    /**
     * Set hydration reminder interval
     * @param minutes Interval in minutes
     */
    fun setHydrationInterval(minutes: Int) {
        prefs.edit().putInt(KEY_HYDRATION_INTERVAL, minutes).apply()
    }

    /**
     * Get hydration reminder interval
     * @return Interval in minutes
     */
    fun getHydrationInterval(): Int {
        return prefs.getInt(KEY_HYDRATION_INTERVAL, DEFAULT_HYDRATION_INTERVAL)
    }

    /**
     * Set whether hydration reminders are enabled
     * @param enabled true to enable, false to disable
     */
    fun setHydrationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_HYDRATION_ENABLED, enabled).apply()
    }

    /**
     * Check if hydration reminders are enabled
     * @return true if enabled
     */
    fun isHydrationEnabled(): Boolean {
        return prefs.getBoolean(KEY_HYDRATION_ENABLED, true)
    }

    /**
     * Check if this is the first launch of the app
     * @return true if first launch
     */
    fun isFirstLaunch(): Boolean {
        val isFirst = prefs.getBoolean(KEY_FIRST_LAUNCH, true)
        if (isFirst) {
            prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
        }
        return isFirst
    }

    /**
     * Clear all data (useful for testing or reset functionality)
     */
    fun clearAllData() {
        prefs.edit().clear().apply()
    }
}
