package com.wellness.brightwell.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.wellness.brightwell.R
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.databinding.FragmentSettingsBinding
import com.wellness.brightwell.utils.NotificationScheduler

/**
 * Fragment for app settings
 * Allows users to configure hydration reminders and view app information
 */
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefsManager: PreferencesManager

    // Permission launcher for notifications (Android 13+)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            enableHydrationReminders()
        } else {
            binding.switchHydrationReminder.isChecked = false
            Toast.makeText(
                requireContext(),
                "Notification permission is required for reminders",
                Toast.LENGTH_LONG
            ).show()
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

        // Initialize PreferencesManager
        prefsManager = PreferencesManager(requireContext())

        // Load current settings
        loadSettings()

        // Set up dark mode switch
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            toggleDarkMode(isChecked)
        }

        // Set up hydration reminder switch
        binding.switchHydrationReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkNotificationPermissionAndEnable()
            } else {
                disableHydrationReminders()
            }
        }

        // Set up interval SeekBar
        setupIntervalSeekBar()

        // Display app version
        displayAppInfo()
    }

    /**
     * Load current settings from SharedPreferences
     */
    private fun loadSettings() {
        val isEnabled = prefsManager.isHydrationEnabled()
        val interval = prefsManager.getHydrationInterval()
        val isDarkMode = prefsManager.isDarkModeEnabled()

        binding.switchDarkMode.isChecked = isDarkMode
        binding.switchHydrationReminder.isChecked = isEnabled
        binding.seekBarInterval.progress = intervalToProgress(interval)
        updateIntervalText(interval)
    }

    /**
     * Toggle dark mode
     * @param enabled true for dark mode, false for light mode
     */
    private fun toggleDarkMode(enabled: Boolean) {
        prefsManager.setDarkModeEnabled(enabled)
        if (enabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        Toast.makeText(
            requireContext(),
            if (enabled) "Dark mode enabled" else "Light mode enabled",
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * Set up the interval SeekBar
     */
    private fun setupIntervalSeekBar() {
        binding.seekBarInterval.max = 9 // 0-9 for 15, 30, 45, 60, 90, 120, 180, 240, 300, 360 minutes

        binding.seekBarInterval.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val interval = progressToInterval(progress)
                updateIntervalText(interval)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val interval = progressToInterval(seekBar?.progress ?: 0)
                prefsManager.setHydrationInterval(interval)
                
                // Reschedule reminders if enabled
                if (prefsManager.isHydrationEnabled()) {
                    NotificationScheduler.scheduleHydrationReminder(requireContext(), interval)
                    Toast.makeText(
                        requireContext(),
                        "Reminder interval updated to $interval minutes",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    /**
     * Convert SeekBar progress to interval in minutes
     */
    private fun progressToInterval(progress: Int): Int {
        return when (progress) {
            0 -> 15
            1 -> 30
            2 -> 45
            3 -> 60
            4 -> 90
            5 -> 120
            6 -> 180
            7 -> 240
            8 -> 300
            9 -> 360
            else -> 60
        }
    }

    /**
     * Convert interval in minutes to SeekBar progress
     */
    private fun intervalToProgress(interval: Int): Int {
        return when (interval) {
            15 -> 0
            30 -> 1
            45 -> 2
            60 -> 3
            90 -> 4
            120 -> 5
            180 -> 6
            240 -> 7
            300 -> 8
            360 -> 9
            else -> 3 // Default to 60 minutes
        }
    }

    /**
     * Update the interval text display
     */
    private fun updateIntervalText(interval: Int) {
        val text = if (interval >= 60) {
            val hours = interval / 60
            val minutes = interval % 60
            if (minutes == 0) {
                "$hours hour${if (hours > 1) "s" else ""}"
            } else {
                "$hours hour${if (hours > 1) "s" else ""} $minutes min"
            }
        } else {
            "$interval minutes"
        }
        binding.textViewIntervalValue.text = text
    }

    /**
     * Check notification permission and enable reminders
     */
    private fun checkNotificationPermissionAndEnable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    enableHydrationReminders()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            enableHydrationReminders()
        }
    }

    /**
     * Enable hydration reminders
     */
    private fun enableHydrationReminders() {
        prefsManager.setHydrationEnabled(true)
        val interval = prefsManager.getHydrationInterval()
        NotificationScheduler.scheduleHydrationReminder(requireContext(), interval)
        Toast.makeText(
            requireContext(),
            "Hydration reminders enabled",
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * Disable hydration reminders
     */
    private fun disableHydrationReminders() {
        prefsManager.setHydrationEnabled(false)
        NotificationScheduler.cancelHydrationReminder(requireContext())
        Toast.makeText(
            requireContext(),
            "Hydration reminders disabled",
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * Display app information
     */
    private fun displayAppInfo() {
        try {
            val packageInfo = requireContext().packageManager.getPackageInfo(
                requireContext().packageName,
                0
            )
            binding.textViewAppVersion.text = "Version ${packageInfo.versionName}"
        } catch (e: Exception) {
            binding.textViewAppVersion.text = getString(R.string.app_version)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
