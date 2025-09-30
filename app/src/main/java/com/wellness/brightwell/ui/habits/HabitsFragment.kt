package com.wellness.brightwell.ui.habits

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wellness.brightwell.R
import com.wellness.brightwell.data.Habit
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.databinding.FragmentHabitsBinding
import com.wellness.brightwell.utils.DateUtils

/**
 * Fragment for managing daily wellness habits
 * Allows users to add, edit, delete habits and track daily completion
 */
class HabitsFragment : Fragment() {

    private var _binding: FragmentHabitsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var prefsManager: PreferencesManager
    private lateinit var habitsAdapter: HabitsAdapter
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
        
        prefsManager = PreferencesManager(requireContext())
        setupRecyclerView()
        loadHabits()
        setupClickListeners()
        updateUI()
    }

    /**
     * Set up RecyclerView with adapter
     */
    private fun setupRecyclerView() {
        habitsAdapter = HabitsAdapter(
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
            adapter = habitsAdapter
        }
    }

    /**
     * Load habits from SharedPreferences
     */
    private fun loadHabits() {
        habits.clear()
        habits.addAll(prefsManager.getHabits())
        habitsAdapter.notifyDataSetChanged()
    }

    /**
     * Set up click listeners for buttons
     */
    private fun setupClickListeners() {
        binding.fabAddHabit.setOnClickListener {
            showAddHabitDialog()
        }
    }

    /**
     * Update UI based on habits list
     */
    private fun updateUI() {
        if (habits.isEmpty()) {
            binding.recyclerViewHabits.visibility = View.GONE
            binding.emptyStateLayout.visibility = View.VISIBLE
        } else {
            binding.recyclerViewHabits.visibility = View.VISIBLE
            binding.emptyStateLayout.visibility = View.GONE
            updateProgressCard()
        }
    }

    /**
     * Update progress card showing today's completion percentage
     */
    private fun updateProgressCard() {
        if (habits.isEmpty()) {
            binding.progressCard.visibility = View.GONE
            return
        }
        
        binding.progressCard.visibility = View.VISIBLE
        val today = DateUtils.getTodayString()
        val completedCount = habits.count { it.isCompletedOn(today) }
        val totalCount = habits.size
        val percentage = if (totalCount > 0) (completedCount * 100) / totalCount else 0
        
        binding.textProgress.text = "$percentage%"
        binding.textProgressDetails.text = "$completedCount of $totalCount habits completed today"
        binding.progressBar.progress = percentage
    }

    /**
     * Handle habit checkbox toggle
     */
    private fun handleHabitChecked(habit: Habit, isChecked: Boolean) {
        val today = DateUtils.getTodayString()
        habit.toggleCompletion(today)
        prefsManager.updateHabit(habit)
        updateProgressCard()
        
        // Update widget
        updateWidget()
    }

    /**
     * Show dialog to add a new habit
     */
    private fun showAddHabitDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_habit, null)
        
        val editName = dialogView.findViewById<EditText>(R.id.editHabitName)
        val editDescription = dialogView.findViewById<EditText>(R.id.editHabitDescription)
        
        AlertDialog.Builder(requireContext())
            .setTitle("Add New Habit")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = editName.text.toString().trim()
                val description = editDescription.text.toString().trim()
                
                if (name.isNotEmpty()) {
                    val newHabit = Habit(
                        name = name,
                        description = description
                    )
                    prefsManager.addHabit(newHabit)
                    loadHabits()
                    updateUI()
                    Toast.makeText(requireContext(), "Habit added!", Toast.LENGTH_SHORT).show()
                    
                    // Update widget
                    updateWidget()
                } else {
                    Toast.makeText(requireContext(), "Please enter a habit name", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Show dialog to edit an existing habit
     */
    private fun showEditHabitDialog(habit: Habit) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_habit, null)
        
        val editName = dialogView.findViewById<EditText>(R.id.editHabitName)
        val editDescription = dialogView.findViewById<EditText>(R.id.editHabitDescription)
        
        // Pre-fill with existing data
        editName.setText(habit.name)
        editDescription.setText(habit.description)
        
        AlertDialog.Builder(requireContext())
            .setTitle("Edit Habit")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val name = editName.text.toString().trim()
                val description = editDescription.text.toString().trim()
                
                if (name.isNotEmpty()) {
                    val updatedHabit = habit.copy(
                        name = name,
                        description = description
                    )
                    prefsManager.updateHabit(updatedHabit)
                    loadHabits()
                    Toast.makeText(requireContext(), "Habit updated!", Toast.LENGTH_SHORT).show()
                    
                    // Update widget
                    updateWidget()
                } else {
                    Toast.makeText(requireContext(), "Please enter a habit name", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Show confirmation dialog before deleting a habit
     */
    private fun showDeleteConfirmation(habit: Habit) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Habit")
            .setMessage("Are you sure you want to delete '${habit.name}'?")
            .setPositiveButton("Delete") { _, _ ->
                prefsManager.deleteHabit(habit.id)
                loadHabits()
                updateUI()
                Toast.makeText(requireContext(), "Habit deleted", Toast.LENGTH_SHORT).show()
                
                // Update widget
                updateWidget()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Update the home screen widget
     */
    private fun updateWidget() {
        // This will be implemented when we create the widget
        // For now, we'll just trigger a widget update
        try {
            val intent = android.content.Intent(requireContext(), 
                Class.forName("com.wellness.brightwell.widget.HabitWidgetProvider"))
            intent.action = android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE
            requireContext().sendBroadcast(intent)
        } catch (e: Exception) {
            // Widget class not yet created, ignore
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
