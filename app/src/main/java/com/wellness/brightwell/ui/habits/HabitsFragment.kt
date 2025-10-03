package com.wellness.brightwell.ui.habits

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.wellness.brightwell.R
import com.wellness.brightwell.data.Habit
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.databinding.DialogAddHabitEnhancedBinding
import com.wellness.brightwell.databinding.FragmentHabitsBinding
import com.wellness.brightwell.utils.DateUtils
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Fragment for managing daily wellness habits
 * Allows users to add, edit, delete, and track completion of habits
 */
class HabitsFragment : Fragment() {

    private var _binding: FragmentHabitsBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefsManager: PreferencesManager
    private lateinit var habitAdapter: HabitAdapter
    private lateinit var calendarAdapter: CalendarDateAdapter
    private val habits = mutableListOf<Habit>()
    private val calendarDates = mutableListOf<CalendarDate>()
    private var selectedDate: String = DateUtils.getTodayString()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize PreferencesManager
        prefsManager = PreferencesManager(requireContext())
        
        // Setup calendar
        setupCalendar()

        // Set up RecyclerView
        setupRecyclerView()

        // Load habits from storage
        loadHabits()

        // Set up FAB to add new habit
        binding.fabAddHabit.setOnClickListener {
            showAddHabitDialog()
        }

        // Update the date display
        updateDateDisplay()
    }

    /**
     * Set up the RecyclerView with adapter and layout manager
     */
    private fun setupRecyclerView() {
        habitAdapter = HabitAdapter(
            habits = habits,
            onHabitChecked = { habit, isChecked ->
                handleHabitChecked(habit, isChecked)
            },
            onHabitEdit = { habit ->
                showEditHabitDialog(habit)
            },
            onHabitDelete = { habit ->
                showDeleteConfirmation(habit)
            }
        )

        binding.recyclerViewHabits.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = habitAdapter
        }
    }

    /**
     * Load habits from SharedPreferences and update UI
     */
    private fun loadHabits() {
        habits.clear()
        habits.addAll(prefsManager.loadHabits())
        habitAdapter.notifyDataSetChanged()
        updateEmptyState()
        updateProgressBar()
    }

    /**
     * Update the date display to show today's date
     */
    private fun updateDateDisplay() {
        val today = DateUtils.formatDate(System.currentTimeMillis())
        binding.textViewDate.text = today
    }

    /**
     * Handle habit checkbox toggle
     * @param habit The habit that was checked/unchecked
     * @param isChecked New checked state (unused - we toggle based on current state)
     */
    private fun handleHabitChecked(habit: Habit, @Suppress("UNUSED_PARAMETER") isChecked: Boolean) {
        val today = DateUtils.getTodayString()
        habit.toggleCompletion(today)
        prefsManager.updateHabit(habit)
        updateProgressBar()
        
        // Update widget
        updateWidget()
    }

    /**
     * Show enhanced dialog to add a new habit with all details
     */
    private fun showAddHabitDialog() {
        val binding = DialogAddHabitEnhancedBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()

        // Variables to store selections
        var selectedIcon = "✓"
        var selectedColor = "#6366F1"
        
        // Setup Icon Picker
        val iconAdapter = IconPickerAdapter(IconPickerAdapter.getDefaultIcons()) { icon ->
            selectedIcon = icon
        }
        binding.recyclerViewIcons.apply {
            adapter = iconAdapter
            layoutManager = GridLayoutManager(requireContext(), 6)
        }
        
        // Setup Color Picker
        val colorAdapter = ColorPickerAdapter(ColorPickerAdapter.getDefaultColors()) { color ->
            selectedColor = color
        }
        binding.recyclerViewColors.apply {
            adapter = colorAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        
        // Setup Category Spinner
        val categories = arrayOf("Health", "Study", "Work", "Finance", "Personal Growth", "Fitness", "Mindfulness", "Hobby")
        val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoryAdapter
        
        // Setup Frequency Radio Buttons
        binding.radioGroupFrequency.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioDaily, R.id.radioWeekly -> {
                    binding.layoutCustomDays.visibility = View.GONE
                }
                R.id.radioCustom -> {
                    binding.layoutCustomDays.visibility = View.VISIBLE
                }
            }
        }
        
        // Save Button
        binding.buttonSave.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            val description = binding.editTextDescription.text.toString().trim()
            
            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a habit name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Get frequency
            val frequency = when (binding.radioGroupFrequency.checkedRadioButtonId) {
                R.id.radioDaily -> "Daily"
                R.id.radioWeekly -> "Weekly"
                R.id.radioCustom -> "Custom"
                else -> "Daily"
            }
            
            // Get custom days if selected
            val customDays = if (frequency == "Custom") {
                val days = mutableListOf<String>()
                if (binding.checkMon.isChecked) days.add("Mon")
                if (binding.checkTue.isChecked) days.add("Tue")
                if (binding.checkWed.isChecked) days.add("Wed")
                if (binding.checkThu.isChecked) days.add("Thu")
                if (binding.checkFri.isChecked) days.add("Fri")
                if (binding.checkSat.isChecked) days.add("Sat")
                if (binding.checkSun.isChecked) days.add("Sun")
                days
            } else {
                listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            }
            
            // Get reminder enabled
            val reminderEnabled = binding.switchReminder.isChecked
            
            // Get category
            val category = binding.spinnerCategory.selectedItem.toString()
            
            // Get difficulty
            val difficulty = when (binding.radioGroupDifficulty.checkedRadioButtonId) {
                R.id.radioEasy -> "Easy"
                R.id.radioMedium -> "Medium"
                R.id.radioHard -> "Hard"
                else -> "Medium"
            }
            
            // Create new habit with all details
            val newHabit = Habit(
                name = name,
                description = description,
                frequency = frequency,
                customDays = customDays,
                reminderEnabled = reminderEnabled,
                category = category,
                difficulty = difficulty,
                color = selectedColor,
                icon = selectedIcon
            )
            
            prefsManager.addHabit(newHabit)
            loadHabits()
            dialog.dismiss()
            Toast.makeText(requireContext(), "Habit added successfully!", Toast.LENGTH_SHORT).show()
        }
        
        // Cancel Button
        binding.buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.show()
    }

    /**
     * Show dialog to edit an existing habit
     * @param habit The habit to edit
     */
    private fun showEditHabitDialog(habit: Habit) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_habit, null)
        val nameInput = dialogView.findViewById<TextInputEditText>(R.id.editTextHabitName)
        val descInput = dialogView.findViewById<TextInputEditText>(R.id.editTextHabitDescription)

        // Pre-fill with existing values
        nameInput.setText(habit.name)
        descInput.setText(habit.description)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Habit")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val name = nameInput.text.toString().trim()
                val description = descInput.text.toString().trim()

                if (name.isNotEmpty()) {
                    val updatedHabit = habit.copy(
                        name = name,
                        description = description
                    )
                    prefsManager.updateHabit(updatedHabit)
                    loadHabits()
                    Toast.makeText(requireContext(), "Habit updated!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Please enter a habit name", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Show confirmation dialog before deleting a habit
     * @param habit The habit to delete
     */
    private fun showDeleteConfirmation(habit: Habit) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Habit")
            .setMessage("Are you sure you want to delete '${habit.name}'?")
            .setPositiveButton("Delete") { _, _ ->
                prefsManager.deleteHabit(habit.id)
                loadHabits()
                Toast.makeText(requireContext(), "Habit deleted", Toast.LENGTH_SHORT).show()
                updateWidget()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Update the progress bar showing today's completion percentage
     */
    private fun updateProgressBar() {
        if (habits.isEmpty()) {
            binding.progressBarHabits.progress = 0
            binding.textViewProgress.text = "0%"
            return
        }

        val today = DateUtils.getTodayString()
        val completedCount = habits.count { it.isCompletedOn(today) }
        val percentage = (completedCount * 100) / habits.size

        binding.progressBarHabits.progress = percentage
        binding.textViewProgress.text = "$percentage%"
        
        // Celebrate if all habits are completed!
        if (percentage == 100 && habits.isNotEmpty()) {
            celebrateCompletion()
        }
    }

    /**
     * Show confetti animation when all habits are completed
     */
    private fun celebrateCompletion() {
        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 2, TimeUnit.SECONDS).max(100),
            position = Position.Relative(0.5, 0.3)
        )
        binding.konfettiView.start(party)
    }

    /**
     * Show or hide empty state view based on habit count
     */
    private fun updateEmptyState() {
        if (habits.isEmpty()) {
            binding.recyclerViewHabits.visibility = View.GONE
            binding.layoutEmpty.visibility = View.VISIBLE
        } else {
            binding.recyclerViewHabits.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    /**
     * Update the home screen widget with latest data
     */
    private fun updateWidget() {
        // Trigger widget update
        com.wellness.brightwell.widget.HabitWidgetProvider.updateWidget(requireContext())
    }

    /**
     * Setup horizontal calendar bar
     */
    private fun setupCalendar() {
        calendarDates.clear()
        val calendar = Calendar.getInstance()
        val today = calendar.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
        
        // Generate 30 days (15 before, today, 14 after)
        calendar.add(Calendar.DAY_OF_YEAR, -15)
        
        for (i in 0 until 30) {
            val date = calendar.time
            val dateString = dateFormat.format(date)
            val dayName = dayFormat.format(date)
            val dayNumber = calendar.get(Calendar.DAY_OF_MONTH)
            val isToday = dateFormat.format(date) == dateFormat.format(today)
            
            calendarDates.add(
                CalendarDate(
                    date = date,
                    dayName = dayName,
                    dayNumber = dayNumber,
                    dateString = dateString,
                    isToday = isToday
                )
            )
            
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        
        // Setup adapter
        calendarAdapter = CalendarDateAdapter(calendarDates) { selectedCalendarDate ->
            selectedDate = selectedCalendarDate.dateString
            loadHabits() // Reload habits for selected date
        }
        
        binding.recyclerViewCalendar.apply {
            adapter = calendarAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            
            // Scroll to today
            val todayPosition = calendarDates.indexOfFirst { it.isToday }
            if (todayPosition != -1) {
                scrollToPosition(todayPosition)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
