package com.wellness.brightwell.ui.calendar

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.wellness.brightwell.R
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.databinding.FragmentCalendarBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment for displaying calendar view of habit completion
 */
class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefsManager: PreferencesManager
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefsManager = PreferencesManager(requireContext())

        setupButtons()
        updateCalendar()
    }

    /**
     * Set up navigation buttons
     */
    private fun setupButtons() {
        binding.buttonPrevMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            updateCalendar()
        }

        binding.buttonNextMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            updateCalendar()
        }

        binding.buttonToday.setOnClickListener {
            calendar.time = Date()
            updateCalendar()
        }
    }

    /**
     * Update calendar display
     */
    private fun updateCalendar() {
        // Update month/year text
        binding.textViewMonth.text = monthFormat.format(calendar.time)

        // Get habits
        val habits = prefsManager.loadHabits()

        // Clear previous calendar
        binding.calendarGrid.removeAllViews()

        // Add day headers
        val dayHeaders = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        dayHeaders.forEach { day ->
            val textView = TextView(requireContext()).apply {
                text = day
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
                textSize = 12f
                setPadding(8, 8, 8, 8)
            }
            binding.calendarGrid.addView(textView)
        }

        // Get first day of month
        val tempCal = calendar.clone() as Calendar
        tempCal.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) - 1

        // Add empty cells for days before month starts
        for (i in 0 until firstDayOfWeek) {
            binding.calendarGrid.addView(View(requireContext()))
        }

        // Get days in month
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Add day cells
        for (day in 1..daysInMonth) {
            tempCal.set(Calendar.DAY_OF_MONTH, day)
            val dateString = dateFormat.format(tempCal.time)

            val dayView = TextView(requireContext()).apply {
                text = day.toString()
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                textSize = 14f
                setPadding(16, 16, 16, 16)

                // Calculate completion for this day
                val completed = habits.count { it.isCompletedOn(dateString) }
                val total = habits.size

                // Set background color based on completion
                val backgroundColor = when {
                    total == 0 -> Color.TRANSPARENT
                    completed == 0 -> Color.parseColor("#FEE2E2") // Light red
                    completed == total -> Color.parseColor("#D1FAE5") // Light green
                    else -> Color.parseColor("#FEF3C7") // Light yellow
                }

                setBackgroundColor(backgroundColor)

                // Highlight today
                val today = dateFormat.format(Date())
                if (dateString == today) {
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                    textSize = 16f
                    setTypeface(null, android.graphics.Typeface.BOLD)
                } else {
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
                }

                // Show details on click
                setOnClickListener {
                    showDayDetails(dateString, completed, total)
                }
            }

            binding.calendarGrid.addView(dayView)
        }

        // Update legend
        updateLegend()
    }

    /**
     * Show details for a specific day
     */
    private fun showDayDetails(dateString: String, completed: Int, total: Int) {
        val date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            .format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)!!)

        val message = when {
            total == 0 -> "No habits tracked yet"
            completed == 0 -> "No habits completed"
            completed == total -> "All habits completed! 🎉"
            else -> "$completed of $total habits completed"
        }

        binding.textViewDayDetails.text = "$date\n$message"
        binding.textViewDayDetails.visibility = View.VISIBLE
    }

    /**
     * Update legend
     */
    private fun updateLegend() {
        val habits = prefsManager.loadHabits()
        val totalHabits = habits.size

        binding.textViewLegend.text = if (totalHabits > 0) {
            "🟢 All completed  🟡 Partial  🔴 None"
        } else {
            "Add habits to see completion tracking"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
