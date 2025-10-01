package com.wellness.brightwell

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.wellness.brightwell.data.Habit
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.databinding.ActivityMainBinding
import com.wellness.brightwell.ui.achievements.AchievementsFragment
import com.wellness.brightwell.ui.habits.HabitsFragment
import com.wellness.brightwell.ui.hydration.HydrationFragment
import com.wellness.brightwell.ui.mood.MoodFragment
import com.wellness.brightwell.ui.settings.SettingsFragment
import com.wellness.brightwell.ui.stats.StatsFragment
import com.wellness.brightwell.ui.templates.TemplatesFragment
import com.wellness.brightwell.utils.NotificationScheduler

/**
 * Main Activity that hosts the navigation drawer and fragments
 * Manages navigation between Habits, Hydration, Mood Journal, Statistics, and Settings screens
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefsManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize PreferencesManager
        prefsManager = PreferencesManager(this)
        
        // Apply dark mode setting
        applyTheme()
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar
        setSupportActionBar(binding.toolbar)

        // Set up first launch
        setupFirstLaunch()

        // Set up navigation drawer
        setupNavigationDrawer()

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(HabitsFragment())
            binding.navigationView.setCheckedItem(R.id.nav_habits)
        }
    }

    /**
     * Apply the saved theme preference
     */
    private fun applyTheme() {
        val isDarkMode = prefsManager.isDarkModeEnabled()
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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
     * Set up navigation drawer
     */
    private fun setupNavigationDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    /**
     * Handle navigation item clicks
     */
    override fun onNavigationItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_habits -> {
                loadFragment(HabitsFragment())
                supportActionBar?.title = "Habits"
            }
            R.id.nav_hydration -> {
                loadFragment(HydrationFragment())
                supportActionBar?.title = "Hydration"
            }
            R.id.nav_mood -> {
                loadFragment(MoodFragment())
                supportActionBar?.title = "Mood Journal"
            }
            R.id.nav_stats -> {
                loadFragment(StatsFragment())
                supportActionBar?.title = "Statistics"
            }
            R.id.nav_achievements -> {
                loadFragment(AchievementsFragment())
                supportActionBar?.title = "Achievements"
            }
            R.id.nav_templates -> {
                loadFragment(TemplatesFragment())
                supportActionBar?.title = "Habit Templates"
            }
            R.id.nav_settings -> {
                loadFragment(SettingsFragment())
                supportActionBar?.title = "Settings"
            }
            R.id.nav_about -> {
                showAboutDialog()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * Show about dialog
     */
    private fun showAboutDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("About BrightWell")
            .setMessage("BrightWell v1.0\n\nYour personal wellness companion for tracking habits, mood, and hydration.\n\nDeveloped for Mobile App Development Lab Exam 03")
            .setPositiveButton("OK", null)
            .show()
    }

    /**
     * Handle back button press
     */
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
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
