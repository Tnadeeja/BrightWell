package com.wellness.brightwell.ui.templates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wellness.brightwell.data.Habit
import com.wellness.brightwell.data.HabitTemplate
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.databinding.FragmentTemplatesBinding

/**
 * Fragment for displaying and using habit templates
 */
class TemplatesFragment : Fragment() {

    private var _binding: FragmentTemplatesBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefsManager: PreferencesManager
    private lateinit var adapter: TemplateAdapter
    private val templates = mutableListOf<HabitTemplate>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTemplatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefsManager = PreferencesManager(requireContext())

        setupRecyclerView()
        loadTemplates()
    }

    /**
     * Set up RecyclerView
     */
    private fun setupRecyclerView() {
        adapter = TemplateAdapter(templates) { template ->
            showTemplateDialog(template)
        }
        binding.recyclerViewTemplates.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTemplates.adapter = adapter
    }

    /**
     * Load all templates
     */
    private fun loadTemplates() {
        templates.clear()
        templates.addAll(HabitTemplate.getAllTemplates())
        adapter.notifyDataSetChanged()
    }

    /**
     * Show dialog to confirm adding template
     */
    private fun showTemplateDialog(template: HabitTemplate) {
        val habitsList = template.habits.joinToString("\n") { "• ${it.first}" }
        
        AlertDialog.Builder(requireContext())
            .setTitle("Add ${template.name}?")
            .setMessage("This will add the following habits:\n\n$habitsList\n\nYou can customize them later.")
            .setPositiveButton("Add All") { _, _ ->
                addTemplateHabits(template)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Add all habits from template
     */
    private fun addTemplateHabits(template: HabitTemplate) {
        var addedCount = 0
        
        template.habits.forEach { (name, description) ->
            val habit = Habit(
                name = name,
                description = description
            )
            prefsManager.addHabit(habit)
            addedCount++
        }
        
        Toast.makeText(
            requireContext(),
            "Added $addedCount habits from ${template.name}!",
            Toast.LENGTH_LONG
        ).show()
        
        // Show success message
        binding.textViewSuccess.visibility = View.VISIBLE
        binding.textViewSuccess.text = "✅ ${template.name} added successfully!"
        
        // Hide after 3 seconds
        binding.textViewSuccess.postDelayed({
            binding.textViewSuccess.visibility = View.GONE
        }, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
