package com.wellness.brightwell.ui.mood

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wellness.brightwell.R
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
            // Map emoji to mood type and background
            val (moodType, backgroundDrawable) = getMoodTypeAndBackground(mood.emoji)
            
            // Display emoji and mood type
            binding.textViewEmoji.text = mood.emoji
            binding.textViewMoodType.text = moodType
            
            // Set dynamic background color based on mood
            binding.iconContainer.background = ContextCompat.getDrawable(binding.root.context, backgroundDrawable)

            // Display timestamp
            binding.textViewTimestamp.text = DateUtils.getRelativeTimeString(mood.timestamp)

            // Display note if available
            if (mood.note.isNotEmpty()) {
                binding.textViewNote.text = mood.note
                binding.noteLayout.visibility = android.view.View.VISIBLE
            } else {
                binding.noteLayout.visibility = android.view.View.GONE
            }

            // Set up delete button
            binding.buttonDelete.setOnClickListener {
                onMoodDelete(mood)
            }
        }
        
        /**
         * Maps emoji to mood type name and background gradient
         */
        private fun getMoodTypeAndBackground(emoji: String): Pair<String, Int> {
            return when (emoji) {
                "😊", "🤩", "😍", "🥰", "😄" -> Pair("Excellent", R.drawable.bg_gradient_green)
                "🙂", "😌", "👍", "✨", "💪" -> Pair("Good", R.drawable.bg_gradient_blue)
                "😐", "😑", "🤷", "😶", "🙄" -> Pair("Neutral", R.drawable.bg_gradient_teal)
                "😔", "😞", "😟", "😕", "🙁" -> Pair("Bad", R.drawable.bg_gradient_orange)
                "😢", "😭", "😡", "😤", "💔" -> Pair("Terrible", R.drawable.bg_gradient_purple)
                else -> Pair("Neutral", R.drawable.bg_gradient_teal) // Default fallback
            }
        }
    }
}
