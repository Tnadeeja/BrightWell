package com.wellness.brightwell

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.wellness.brightwell.data.Habit
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.databinding.ActivityMainBinding
import com.wellness.brightwell.ui.habits.HabitsFragment
import com.wellness.brightwell.ui.hydration.HydrationFragment
import com.wellness.brightwell.ui.mood.MoodFragment
import com.wellness.brightwell.ui.settings.SettingsFragment
import com.wellness.brightwell.utils.NotificationScheduler

/**
 * Main Activity that hosts the bottom navigation and fragments
 * Manages navigation between Habits, Hydration, Mood Journal, and Settings screens
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefsManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize PreferencesManager
        prefsManager = PreferencesManager(this)

        // Set up first launch
        setupFirstLaunch()

        // Set up bottom navigation
        setupBottomNavigation()

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(HabitsFragment())
        }
    }

    /**
     * Set up first launch - add sample data and schedule reminders
     */
    private fun setupFirstLaunch() {
        if (prefsManager.isFirstLaunch()) {
            // Add sample habits
            val sampleHabits = listOf(
                Habit(name = "Drink Water", description = "Drink 8 glasses of water"),
                Habit(name = "Meditate", description = "10 minutes of meditation"),
                Habit(name = "Exercise", description = "30 minutes of physical activity"),
                Habit(name = "Read", description = "Read for 20 minutes")
            )
            
            sampleHabits.forEach { habit ->
                prefsManager.addHabit(habit)
            }

            // Schedule hydration reminders
            if (prefsManager.isHydrationEnabled()) {
                val interval = prefsManager.getHydrationInterval()
                NotificationScheduler.scheduleHydrationReminder(this, interval)
            }

            // Mark first launch as complete
            prefsManager.setFirstLaunchComplete()
        }
    }

    /**
     * Set up bottom navigation bar
     */
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_habits -> {
                    loadFragment(HabitsFragment())
                    true
                }
                R.id.nav_hydration -> {
                    loadFragment(HydrationFragment())
                    true
                }
                R.id.nav_mood -> {
                    loadFragment(MoodFragment())
                    true
                }
                R.id.nav_settings -> {
                    loadFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Load a fragment into the container
     * @param fragment Fragment to load
     */
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        // Update widget when app resumes
        com.wellness.brightwell.widget.HabitWidgetProvider.updateWidget(this)
    }
}
