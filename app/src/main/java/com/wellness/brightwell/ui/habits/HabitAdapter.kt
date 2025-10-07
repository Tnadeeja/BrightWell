package com.wellness.brightwell.ui.habits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.wellness.brightwell.R
import com.wellness.brightwell.data.Habit
import com.wellness.brightwell.databinding.ItemHabitBinding
import com.wellness.brightwell.utils.DateUtils

/**
 * RecyclerView adapter for displaying and managing habits
 * @property habits List of habits to display
 * @property onHabitChecked Callback when habit completion is toggled
 * @property onHabitEdit Callback when habit edit is requested
 * @property onHabitDelete Callback when habit deletion is requested
 */
class HabitAdapter(
    private val habits: List<Habit>,
    private val onHabitChecked: (Habit, Boolean) -> Unit,
    private val onHabitEdit: (Habit) -> Unit,
    private val onHabitDelete: (Habit) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

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

    /**
     * ViewHolder for habit items
     */
    inner class HabitViewHolder(
        private val binding: ItemHabitBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(habit: Habit) {
            // Set icon
            binding.textViewIcon.text = habit.icon
            
            // Set clean white card background with tech green icon
            binding.cardBackground.setBackgroundColor(android.graphics.Color.WHITE)
            
            // Set icon background to tech green gradient
            binding.textViewIcon.setBackgroundResource(com.wellness.brightwell.R.drawable.icon_background)
            
            // Use clean dark text on white background
            binding.textViewHabitName.setTextColor(android.graphics.Color.parseColor("#111827"))
            binding.textViewHabitDescription.setTextColor(android.graphics.Color.parseColor("#6B7280"))
            
            // Set habit name and description
            binding.textViewHabitName.text = habit.name
            
            if (habit.description.isNotEmpty()) {
                binding.textViewHabitDescription.text = habit.description
                binding.textViewHabitDescription.visibility = android.view.View.VISIBLE
            } else {
                binding.textViewHabitDescription.visibility = android.view.View.GONE
            }
            
            // Set category and difficulty
            binding.textViewCategory.text = habit.category
            binding.textViewDifficulty.text = habit.difficulty

            // Check if habit is completed today
            val today = DateUtils.getTodayString()
            val isCompleted = habit.isCompletedOn(today)
            
            // Set checkbox state without triggering listener
            binding.checkBoxHabit.setOnCheckedChangeListener(null)
            binding.checkBoxHabit.isChecked = isCompleted
            
            // Set up checkbox listener
            binding.checkBoxHabit.setOnCheckedChangeListener { _, isChecked ->
                onHabitChecked(habit, isChecked)
            }

            // Set up menu button click
            binding.buttonMenu.setOnClickListener { view ->
                showPopupMenu(view, habit)
            }

            // Show streak information
            val streak = calculateStreak(habit)
            if (streak > 0) {
                binding.textViewStreak.text = "🔥 $streak day streak"
                binding.textViewStreak.visibility = android.view.View.VISIBLE
            } else {
                binding.textViewStreak.visibility = android.view.View.GONE
            }
        }
        
        /**
         * Show popup menu with edit/delete options
         */
        private fun showPopupMenu(view: View, habit: Habit) {
            val popup = PopupMenu(view.context, view)
            popup.menuInflater.inflate(R.menu.habit_menu, popup.menu)
            
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_edit -> {
                        onHabitEdit(habit)
                        true
                    }
                    R.id.action_delete -> {
                        onHabitDelete(habit)
                        true
                    }
                    else -> false
                }
            }
            
            popup.show()
        }

        /**
         * Calculate the current streak for a habit
         * @param habit The habit to calculate streak for
         * @return Number of consecutive days the habit was completed
         */
        private fun calculateStreak(habit: Habit): Int {
            var streak = 0
            val calendar = java.util.Calendar.getInstance()
            
            // Check backwards from today
            while (true) {
                val dateString = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                    .format(calendar.time)
                
                if (habit.isCompletedOn(dateString)) {
                    streak++
                    calendar.add(java.util.Calendar.DAY_OF_YEAR, -1)
                } else {
                    break
                }
            }
            
            return streak
        }
    }
}
