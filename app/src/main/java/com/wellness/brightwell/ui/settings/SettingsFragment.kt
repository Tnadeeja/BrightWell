package com.wellness.brightwell.ui.settings

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.wellness.brightwell.R
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.databinding.FragmentSettingsBinding
import com.wellness.brightwell.notifications.NotificationScheduler

/**
 * Fragment for app settings and preferences
 * Manages hydration reminder settings and other app configurations
 */
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var prefsManager: PreferencesManager
    
    // Notification permission launcher for Android 13+
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            enableHydrationReminders()
        } else {
            Toast.makeText(
                requireContext(),
                "Notification permission is required for reminders",
                Toast.LENGTH_LONG
            ).show()
            binding.switchHydrationReminder.isChecked = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        prefsManager = PreferencesManager(requireContext())
        loadSettings()
        setupClickListeners()
    }

    /**
     * Load current settings from SharedPreferences
     */
    private fun loadSettings() {
        // Load hydration reminder settings
        val isEnabled = prefsManager.isHydrationEnabled()
        val interval = prefsManager.getHydrationInterval()
        
        binding.switchHydrationReminder.isChecked = isEnabled
        binding.textIntervalValue.text = formatInterval(interval)
        
        // Update reminder status text
        updateReminderStatusText()
    }

    /**
     * Set up click listeners for settings controls
     */
    private fun setupClickListeners() {
        // Hydration reminder toggle
        binding.switchHydrationReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Check notification permission for Android 13+
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        enableHydrationReminders()
                    } else {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                } else {
                    enableHydrationReminders()
                }
            } else {
                disableHydrationReminders()
            }
        }
        
        // Change interval button
        binding.buttonChangeInterval.setOnClickListener {
            showIntervalPickerDialog()
        }
        
        // About section
        binding.cardAbout.setOnClickListener {
            showAboutDialog()
        }
        
        // Clear data button
        binding.buttonClearData.setOnClickListener {
            showClearDataConfirmation()
        }
    }

    /**
     * Enable hydration reminders
     */
    private fun enableHydrationReminders() {
        prefsManager.setHydrationEnabled(true)
        NotificationScheduler.scheduleHydrationReminders(requireContext())
        updateReminderStatusText()
        Toast.makeText(requireContext(), "Hydration reminders enabled", Toast.LENGTH_SHORT).show()
    }

    /**
     * Disable hydration reminders
     */
    private fun disableHydrationReminders() {
        prefsManager.setHydrationEnabled(false)
        NotificationScheduler.cancelHydrationReminders(requireContext())
        updateReminderStatusText()
        Toast.makeText(requireContext(), "Hydration reminders disabled", Toast.LENGTH_SHORT).show()
    }

    /**
     * Show dialog to select reminder interval
     */
    private fun showIntervalPickerDialog() {
        val intervals = arrayOf(
            "15 minutes",
            "30 minutes",
            "1 hour",
            "2 hours",
            "3 hours",
            "4 hours"
        )
        
        val intervalValues = arrayOf(15, 30, 60, 120, 180, 240)
        
        val currentInterval = prefsManager.getHydrationInterval()
        val currentIndex = intervalValues.indexOf(currentInterval).takeIf { it >= 0 } ?: 3
        
        AlertDialog.Builder(requireContext())
            .setTitle("Select Reminder Interval")
            .setSingleChoiceItems(intervals, currentIndex) { dialog, which ->
                val selectedInterval = intervalValues[which]
                prefsManager.setHydrationInterval(selectedInterval)
                binding.textIntervalValue.text = formatInterval(selectedInterval)
                
                // Reschedule reminders if enabled
                if (prefsManager.isHydrationEnabled()) {
                    NotificationScheduler.scheduleHydrationReminders(requireContext())
                }
                
                Toast.makeText(
                    requireContext(),
                    "Interval updated to ${intervals[which]}",
                    Toast.LENGTH_SHORT
                ).show()
                
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Format interval minutes to readable string
     */
    private fun formatInterval(minutes: Int): String {
        return when {
            minutes < 60 -> "$minutes minutes"
            minutes == 60 -> "1 hour"
            minutes % 60 == 0 -> "${minutes / 60} hours"
            else -> "$minutes minutes"
        }
    }

    /**
     * Update reminder status text
     */
    private fun updateReminderStatusText() {
        if (prefsManager.isHydrationEnabled()) {
            val interval = prefsManager.getHydrationInterval()
            binding.textReminderStatus.text = "You'll receive reminders every ${formatInterval(interval)}"
            binding.textReminderStatus.visibility = View.VISIBLE
        } else {
            binding.textReminderStatus.visibility = View.GONE
        }
    }

    /**
     * Show about dialog with app information
     */
    private fun showAboutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("About BrightWell")
            .setMessage(
                "BrightWell - Your Personal Wellness Companion\n\n" +
                "Version 1.0\n\n" +
                "Features:\n" +
                "• Daily Habit Tracker\n" +
                "• Mood Journal with Emoji Selector\n" +
                "• Hydration Reminders\n" +
                "• Home Screen Widget\n\n" +
                "Developed as part of Mobile App Development Lab Exam 03\n\n" +
                "Stay healthy, stay happy! 🌟"
            )
            .setPositiveButton("OK", null)
            .show()
    }

    /**
     * Show confirmation dialog before clearing all data
     */
    private fun showClearDataConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Clear All Data")
            .setMessage(
                "This will permanently delete:\n" +
                "• All habits\n" +
                "• All mood entries\n" +
                "• All settings\n\n" +
                "This action cannot be undone. Are you sure?"
            )
            .setPositiveButton("Clear") { _, _ ->
                clearAllData()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Clear all app data
     */
    private fun clearAllData() {
        // Cancel reminders
        NotificationScheduler.cancelHydrationReminders(requireContext())
        
        // Clear all data from SharedPreferences
        prefsManager.clearAllData()
        
        // Reset UI
        loadSettings()
        
        Toast.makeText(
            requireContext(),
            "All data cleared successfully",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
