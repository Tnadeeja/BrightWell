package com.wellness.brightwell.ui.habits

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wellness.brightwell.R
import com.wellness.brightwell.databinding.ItemIconPickerBinding

class IconPickerAdapter(
    private val icons: List<String>,
    private val onIconClick: (String) -> Unit
) : RecyclerView.Adapter<IconPickerAdapter.IconViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val binding = ItemIconPickerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IconViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        holder.bind(icons[position], position == selectedPosition)
    }

    override fun getItemCount() = icons.size

    fun setSelectedPosition(position: Int) {
        val oldPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(oldPosition)
        notifyItemChanged(selectedPosition)
    }

    inner class IconViewHolder(
        private val binding: ItemIconPickerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(icon: String, isSelected: Boolean) {
            binding.textViewIcon.text = icon

            if (isSelected) {
                binding.cardView.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.primary)
                )
                binding.cardView.strokeWidth = 4
                binding.cardView.strokeColor = ContextCompat.getColor(binding.root.context, R.color.primary_dark)
            } else {
                binding.cardView.setCardBackgroundColor(Color.WHITE)
                binding.cardView.strokeWidth = 1
                binding.cardView.strokeColor = ContextCompat.getColor(binding.root.context, R.color.divider)
            }

            binding.root.setOnClickListener {
                setSelectedPosition(adapterPosition)
                onIconClick(icon)
            }
        }
    }

    companion object {
        fun getDefaultIcons(): List<String> {
            return listOf(
                "✓", "💪", "🏃", "🧘", "📚", "💧", "🍎", "😊",
                "⭐", "🎯", "🔥", "💡", "🎨", "🎵", "☕", "🌟",
                "🌱", "🏆", "❤️", "🧠", "✨", "🌈", "🎉", "🚀"
            )
        }
    }
}
