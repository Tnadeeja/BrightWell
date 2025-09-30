package com.wellness.brightwell.ui.mood

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wellness.brightwell.data.MoodEntry
import com.wellness.brightwell.databinding.ItemMoodBinding

/**
 * RecyclerView adapter for displaying mood entries
 */
class MoodAdapter(
    private val moodEntries: List<MoodEntry>,
    private val onMoodDelete: (MoodEntry) -> Unit
) : RecyclerView.Adapter<MoodAdapter.MoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val binding = ItemMoodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        holder.bind(moodEntries[position])
    }

    override fun getItemCount(): Int = moodEntries.size

    inner class MoodViewHolder(
        private val binding: ItemMoodBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: MoodEntry) {
            binding.textMoodEmoji.text = entry.emoji
            binding.textMoodDate.text = entry.getFormattedDate()
            binding.textMoodTime.text = entry.getFormattedTime()
            binding.textMoodNote.text = entry.note
            
            // Show/hide note based on content
            if (entry.note.isEmpty()) {
                binding.textMoodNote.visibility = android.view.View.GONE
            } else {
                binding.textMoodNote.visibility = android.view.View.VISIBLE
            }
            
            // Delete button click listener
            binding.buttonDeleteMood.setOnClickListener {
                onMoodDelete(entry)
            }
        }
    }
}
