package com.wellness.brightwell

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.databinding.ActivitySplashBinding

/**
 * Splash screen shown on app launch
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var prefsManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Apply theme before setting content view
        prefsManager = PreferencesManager(this)
        applyTheme()
        
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Start animations
        startAnimations()

        // Navigate to main activity after delay
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            // Add fade transition
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }, 2500) // 2.5 seconds
    }

    /**
     * Apply saved theme
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
     * Start splash screen animations
     */
    private fun startAnimations() {
        // Fade in animation for logo
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeIn.duration = 1000
        binding.imageViewLogo.startAnimation(fadeIn)

        // Slide up animation for app name
        val slideUp = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        slideUp.duration = 800
        slideUp.startOffset = 300
        binding.textViewAppName.startAnimation(slideUp)

        // Fade in animation for tagline
        val fadeInSlow = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeInSlow.duration = 1200
        fadeInSlow.startOffset = 600
        binding.textViewTagline.startAnimation(fadeInSlow)
    }
}
