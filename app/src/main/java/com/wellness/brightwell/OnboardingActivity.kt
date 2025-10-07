package com.wellness.brightwell

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.textfield.TextInputEditText
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.databinding.ActivityOnboardingBinding

/**
 * Onboarding Activity - Asks user questions to personalize their experience
 */
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var prefsManager: PreferencesManager
    private val layouts = listOf(
        R.layout.onboarding_page_welcome,
        R.layout.onboarding_page_name,
        R.layout.onboarding_page_goal,
        R.layout.onboarding_page_reminder
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefsManager = PreferencesManager(this)

        setupViewPager()
        setupIndicators()
        setupButtons()
    }

    private fun setupViewPager() {
        val adapter = OnboardingPagerAdapter(layouts)
        binding.viewPager.adapter = adapter
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicators(position)
                updateButtons(position)
            }
        })
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(layouts.size)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)

        for (i in indicators.indices) {
            indicators[i] = ImageView(this)
            indicators[i]?.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.indicator_inactive)
            )
            indicators[i]?.layoutParams = layoutParams
            binding.indicatorLayout.addView(indicators[i])
        }

        // Set first indicator as active
        indicators[0]?.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.indicator_active)
        )
    }

    private fun updateIndicators(position: Int) {
        val childCount = binding.indicatorLayout.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicatorLayout.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.indicator_active)
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.indicator_inactive)
                )
            }
        }
    }

    private fun setupButtons() {
        binding.buttonNext.setOnClickListener {
            val current = binding.viewPager.currentItem
            if (current < layouts.size - 1) {
                binding.viewPager.currentItem = current + 1
            } else {
                finishOnboarding()
            }
        }

        binding.buttonSkip.setOnClickListener {
            finishOnboarding()
        }
    }

    private fun updateButtons(position: Int) {
        if (position == layouts.size - 1) {
            binding.buttonNext.text = "Get Started"
            binding.buttonSkip.visibility = View.GONE
        } else {
            binding.buttonNext.text = "Next"
            binding.buttonSkip.visibility = View.VISIBLE
        }
    }

    private fun finishOnboarding() {
        // Save user responses
        saveUserData()

        // Mark onboarding as complete
        prefsManager.setOnboardingComplete()

        // Navigate to main activity
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun saveUserData() {
        // Get name from page 2
        val nameView = binding.viewPager.findViewWithTag<View>("page_1")
        val nameInput = nameView?.findViewById<TextInputEditText>(R.id.editTextName)
        val userName = nameInput?.text?.toString() ?: "User"
        prefsManager.saveUserName(userName)

        // Save other preferences as needed
        // Goal and reminder preferences can be saved similarly
    }

    /**
     * ViewPager2 Adapter for onboarding pages
     */
    inner class OnboardingPagerAdapter(private val layouts: List<Int>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<OnboardingPagerAdapter.ViewHolder>() {

        inner class ViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(viewType, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.view.tag = "page_$position"
        }

        override fun getItemCount(): Int = layouts.size

        override fun getItemViewType(position: Int): Int = layouts[position]
    }
}
