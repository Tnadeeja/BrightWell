package com.wellness.brightwell

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.databinding.ActivityMainBinding
import com.wellness.brightwell.notifications.NotificationScheduler
import com.wellness.brightwell.ui.habits.HabitsFragment
import com.wellness.brightwell.ui.mood.MoodFragment
import com.wellness.brightwell.ui.settings.SettingsFragment

/**
 * Main Activity with bottom navigation
 * Hosts three main fragments: Habits, Mood, and Settings
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefsManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        prefsManager = PreferencesManager(this)
        
        // Set up bottom navigation
        setupBottomNavigation()
        
        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(HabitsFragment())
        }
        
        // Initialize hydration reminders on first launch
        initializeReminders()
    }

    /**
     * Set up bottom navigation view
     */
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_habits -> {
                    loadFragment(HabitsFragment())
                    true
                }
                R.id.navigation_mood -> {
                    loadFragment(MoodFragment())
                    true
                }
                R.id.navigation_settings -> {
                    loadFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Load a fragment into the container
     */
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    /**
     * Initialize hydration reminders on first launch
     */
    private fun initializeReminders() {
        if (prefsManager.isFirstLaunch()) {
            // Schedule default hydration reminders
            NotificationScheduler.scheduleHydrationReminders(this)
        }
    }

    override fun onResume() {
        super.onResume()
        // Update widgets when app comes to foreground
        updateWidgets()
    }

    /**
     * Update all home screen widgets
     */
    private fun updateWidgets() {
        try {
            com.wellness.brightwell.widget.HabitWidgetProvider.updateAllWidgets(this)
        } catch (e: Exception) {
            // Widget not added to home screen yet, ignore
        }
    }
}
