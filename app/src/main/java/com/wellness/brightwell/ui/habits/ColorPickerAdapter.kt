package com.wellness.brightwell.ui.habits

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wellness.brightwell.R
import com.wellness.brightwell.databinding.ItemColorPickerBinding

class ColorPickerAdapter(
    private val colors: List<String>,
    private val onColorClick: (String) -> Unit
) : RecyclerView.Adapter<ColorPickerAdapter.ColorViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val binding = ItemColorPickerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ColorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colors[position], position == selectedPosition)
    }

    override fun getItemCount() = colors.size

    fun setSelectedPosition(position: Int) {
        val oldPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(oldPosition)
        notifyItemChanged(selectedPosition)
    }

    inner class ColorViewHolder(
        private val binding: ItemColorPickerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(colorHex: String, isSelected: Boolean) {
            binding.viewColor.setBackgroundColor(Color.parseColor(colorHex))

            if (isSelected) {
                binding.cardView.strokeWidth = 6
                binding.cardView.strokeColor = ContextCompat.getColor(binding.root.context, R.color.primary_dark)
            } else {
                binding.cardView.strokeWidth = 2
                binding.cardView.strokeColor = Color.parseColor(colorHex)
            }

            binding.root.setOnClickListener {
                setSelectedPosition(adapterPosition)
                onColorClick(colorHex)
            }
        }
    }

    companion object {
        fun getDefaultColors(): List<String> {
            return listOf(
                "#6366F1", "#8B5CF6", "#EC4899", "#EF4444", "#F59E0B",
                "#10B981", "#14B8A6", "#3B82F6", "#F97316", "#84CC16"
            )
        }
    }
}
