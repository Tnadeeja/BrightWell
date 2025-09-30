package com.wellness.brightwell.ui.habits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wellness.brightwell.data.Habit
import com.wellness.brightwell.databinding.ItemHabitBinding
import com.wellness.brightwell.utils.DateUtils

/**
 * RecyclerView adapter for displaying habits list
 */
class HabitsAdapter(
    private val habits: List<Habit>,
    private val onHabitChecked: (Habit, Boolean) -> Unit,
    private val onHabitEdit: (Habit) -> Unit,
    private val onHabitDelete: (Habit) -> Unit
) : RecyclerView.Adapter<HabitsAdapter.HabitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val binding = ItemHabitBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HabitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount(): Int = habits.size

    inner class HabitViewHolder(
        private val binding: ItemHabitBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(habit: Habit) {
            val today = DateUtils.getTodayString()
            val isCompleted = habit.isCompletedOn(today)
            
            binding.textHabitName.text = habit.name
            binding.textHabitDescription.text = habit.description
            binding.checkboxHabit.isChecked = isCompleted
            
            // Show/hide description based on content
            if (habit.description.isEmpty()) {
                binding.textHabitDescription.visibility = android.view.View.GONE
            } else {
                binding.textHabitDescription.visibility = android.view.View.VISIBLE
            }
            
            // Checkbox click listener
            binding.checkboxHabit.setOnClickListener {
                onHabitChecked(habit, binding.checkboxHabit.isChecked)
            }
            
            // Edit button click listener
            binding.buttonEdit.setOnClickListener {
                onHabitEdit(habit)
            }
            
            // Delete button click listener
            binding.buttonDelete.setOnClickListener {
                onHabitDelete(habit)
            }
            
            // Card click listener (also toggles checkbox)
            binding.cardHabit.setOnClickListener {
                binding.checkboxHabit.isChecked = !binding.checkboxHabit.isChecked
                onHabitChecked(habit, binding.checkboxHabit.isChecked)
            }
        }
    }
}
