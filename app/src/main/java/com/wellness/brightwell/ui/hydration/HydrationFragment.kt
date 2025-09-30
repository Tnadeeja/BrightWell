package com.wellness.brightwell.ui.hydration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wellness.brightwell.R
import com.wellness.brightwell.data.HydrationEntry
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.databinding.FragmentHydrationBinding
import com.wellness.brightwell.utils.DateUtils

/**
 * Fragment for tracking daily water intake
 * Allows users to log water consumption and view daily progress
 */
class HydrationFragment : Fragment() {

    private var _binding: FragmentHydrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefsManager: PreferencesManager
    private lateinit var hydrationAdapter: HydrationAdapter
    private val entries = mutableListOf<HydrationEntry>()

    // Daily goal in milliliters (2000ml = 2 liters)
    private var dailyGoal = 2000

    // Quick add amounts in ml
    private val quickAmounts = listOf(250, 500, 750, 1000)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHydrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize PreferencesManager
        prefsManager = PreferencesManager(requireContext())

        // Load daily goal
        dailyGoal = prefsManager.getHydrationGoal()

        // Set up RecyclerView
        setupRecyclerView()

        // Load today's entries
        loadTodayEntries()

        // Set up quick add buttons
        setupQuickAddButtons()

        // Set up custom amount button
        binding.buttonCustomAmount.setOnClickListener {
            showCustomAmountDialog()
        }

        // Set up goal button
        binding.buttonSetGoal.setOnClickListener {
            showSetGoalDialog()
        }

        // Update the date display
        updateDateDisplay()
    }

    /**
     * Set up the RecyclerView with adapter and layout manager
     */
    private fun setupRecyclerView() {
        hydrationAdapter = HydrationAdapter(
            entries = entries,
            onEntryDelete = { entry ->
                deleteEntry(entry)
            }
        )

        binding.recyclerViewHydration.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = hydrationAdapter
        }
    }

    /**
     * Set up quick add buttons for common water amounts
     */
    private fun setupQuickAddButtons() {
        binding.button250ml.setOnClickListener { addWater(250) }
        binding.button500ml.setOnClickListener { addWater(500) }
        binding.button750ml.setOnClickListener { addWater(750) }
        binding.button1000ml.setOnClickListener { addWater(1000) }
    }

    /**
     * Load today's hydration entries from SharedPreferences
     */
    private fun loadTodayEntries() {
        val today = DateUtils.getTodayString()
        entries.clear()
        entries.addAll(prefsManager.loadHydrationEntries().filter { it.date == today })
        hydrationAdapter.notifyDataSetChanged()
        updateProgress()
        updateEmptyState()
    }

    /**
     * Add water intake
     * @param amount Amount in milliliters
     */
    private fun addWater(amount: Int) {
        val today = DateUtils.getTodayString()
        val entry = HydrationEntry(
            amount = amount,
            date = today
        )
        prefsManager.addHydrationEntry(entry)
        loadTodayEntries()
        
        // Show encouraging message
        val totalToday = getTodayTotal()
        if (totalToday >= dailyGoal) {
            Toast.makeText(
                requireContext(),
                "🎉 Daily goal achieved! Great job!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                "Added ${amount}ml! Keep going!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Delete a hydration entry
     * @param entry Entry to delete
     */
    private fun deleteEntry(entry: HydrationEntry) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Entry")
            .setMessage("Remove ${entry.amount}ml entry?")
            .setPositiveButton("Delete") { _, _ ->
                prefsManager.deleteHydrationEntry(entry.id)
                loadTodayEntries()
                Toast.makeText(requireContext(), "Entry deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Show dialog to add custom water amount
     */
    private fun showCustomAmountDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_custom_amount, null)
        val amountInput = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
            R.id.editTextAmount
        )

        AlertDialog.Builder(requireContext())
            .setTitle("Add Custom Amount")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val amountText = amountInput.text.toString().trim()
                if (amountText.isNotEmpty()) {
                    val amount = amountText.toIntOrNull()
                    if (amount != null && amount > 0 && amount <= 5000) {
                        addWater(amount)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Please enter a valid amount (1-5000ml)",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Show dialog to set daily goal
     */
    private fun showSetGoalDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_set_goal, null)
        val goalInput = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
            R.id.editTextGoal
        )
        goalInput.setText(dailyGoal.toString())

        AlertDialog.Builder(requireContext())
            .setTitle("Set Daily Goal")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val goalText = goalInput.text.toString().trim()
                if (goalText.isNotEmpty()) {
                    val goal = goalText.toIntOrNull()
                    if (goal != null && goal >= 500 && goal <= 10000) {
                        dailyGoal = goal
                        prefsManager.setHydrationGoal(goal)
                        updateProgress()
                        Toast.makeText(
                            requireContext(),
                            "Daily goal set to ${goal}ml",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Please enter a valid goal (500-10000ml)",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Get total water intake for today
     */
    private fun getTodayTotal(): Int {
        return entries.sumOf { it.amount }
    }

    /**
     * Update the progress display
     */
    private fun updateProgress() {
        val total = getTodayTotal()
        val percentage = ((total.toFloat() / dailyGoal) * 100).toInt().coerceAtMost(100)

        binding.progressBarHydration.max = dailyGoal
        binding.progressBarHydration.progress = total
        
        binding.textViewProgress.text = "$total ml / $dailyGoal ml"
        binding.textViewPercentage.text = "$percentage%"

        // Update glasses display (assuming 250ml per glass)
        val glasses = total / 250
        val totalGlasses = dailyGoal / 250
        binding.textViewGlasses.text = "💧 $glasses / $totalGlasses glasses"

        // Change color based on progress
        val color = when {
            percentage >= 100 -> requireContext().getColor(R.color.success)
            percentage >= 75 -> requireContext().getColor(R.color.secondary)
            percentage >= 50 -> requireContext().getColor(R.color.warning)
            else -> requireContext().getColor(R.color.primary)
        }
        binding.textViewPercentage.setTextColor(color)
    }

    /**
     * Update the date display to show today's date
     */
    private fun updateDateDisplay() {
        val today = DateUtils.formatDate(System.currentTimeMillis())
        binding.textViewDate.text = today
    }

    /**
     * Show or hide empty state view based on entry count
     */
    private fun updateEmptyState() {
        if (entries.isEmpty()) {
            binding.recyclerViewHydration.visibility = View.GONE
            binding.layoutEmpty.visibility = View.VISIBLE
        } else {
            binding.recyclerViewHydration.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
