package com.wellness.brightwell.ui.habits

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.wellness.brightwell.R
import com.wellness.brightwell.data.Habit
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.databinding.FragmentHabitsBinding
import com.wellness.brightwell.utils.DateUtils
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
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
    private val habits = mutableListOf<Habit>()

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
     * Show dialog to add a new habit
     */
    private fun showAddHabitDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_habit, null)
        val nameInput = dialogView.findViewById<TextInputEditText>(R.id.editTextHabitName)
        val descInput = dialogView.findViewById<TextInputEditText>(R.id.editTextHabitDescription)

        AlertDialog.Builder(requireContext())
            .setTitle("Add New Habit")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.text.toString().trim()
                val description = descInput.text.toString().trim()

                if (name.isNotEmpty()) {
                    val newHabit = Habit(
                        name = name,
                        description = description
                    )
                    prefsManager.addHabit(newHabit)
                    loadHabits()
                    Toast.makeText(requireContext(), "Habit added!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Please enter a habit name", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
