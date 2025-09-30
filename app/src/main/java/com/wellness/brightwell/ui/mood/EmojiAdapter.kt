package com.wellness.brightwell.ui.mood

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wellness.brightwell.databinding.ItemEmojiBinding

/**
 * RecyclerView adapter for emoji selection grid
 * @property emojis List of emojis to display
 * @property onEmojiSelected Callback when an emoji is selected
 */
class EmojiAdapter(
    private val emojis: List<String>,
    private val onEmojiSelected: (String) -> Unit
) : RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
        val binding = ItemEmojiBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EmojiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
        holder.bind(emojis[position], position)
    }

    override fun getItemCount(): Int = emojis.size

    /**
     * ViewHolder for emoji items
     */
    inner class EmojiViewHolder(
        private val binding: ItemEmojiBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(emoji: String, position: Int) {
            binding.textViewEmoji.text = emoji

            // Highlight selected emoji
            if (position == selectedPosition) {
                binding.root.setBackgroundColor(Color.parseColor("#E0E7FF"))
            } else {
                binding.root.setBackgroundColor(Color.TRANSPARENT)
            }

            // Handle emoji selection
            binding.root.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                onEmojiSelected(emoji)
            }
        }
    }
}
