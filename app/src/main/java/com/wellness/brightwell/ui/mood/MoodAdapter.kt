package com.wellness.brightwell.ui.mood

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wellness.brightwell.data.MoodEntry
import com.wellness.brightwell.databinding.ItemMoodBinding
import com.wellness.brightwell.utils.DateUtils

/**
 * RecyclerView adapter for displaying mood entries
 * @property moods List of mood entries to display
 * @property onMoodDelete Callback when mood deletion is requested
 */
class MoodAdapter(
    private val moods: List<MoodEntry>,
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
        holder.bind(moods[position])
    }

    override fun getItemCount(): Int = moods.size

    /**
     * ViewHolder for mood items
     */
    inner class MoodViewHolder(
        private val binding: ItemMoodBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(mood: MoodEntry) {
            // Display emoji
            binding.textViewEmoji.text = mood.emoji

            // Display timestamp
            binding.textViewTimestamp.text = DateUtils.getRelativeTimeString(mood.timestamp)

            // Display note if available
            if (mood.note.isNotEmpty()) {
                binding.textViewNote.text = mood.note
                binding.textViewNote.visibility = android.view.View.VISIBLE
            } else {
                binding.textViewNote.visibility = android.view.View.GONE
            }

            // Set up delete button
            binding.buttonDelete.setOnClickListener {
                onMoodDelete(mood)
            }
        }
    }
}
