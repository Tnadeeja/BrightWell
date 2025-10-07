package com.wellness.brightwell.ui.habits

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wellness.brightwell.R
import com.wellness.brightwell.databinding.ItemCalendarDateBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter for horizontal calendar date picker
 */
class CalendarDateAdapter(
    private val dates: List<CalendarDate>,
    private val onDateClick: (CalendarDate) -> Unit
) : RecyclerView.Adapter<CalendarDateAdapter.DateViewHolder>() {

    private var selectedPosition = dates.indexOfFirst { it.isToday }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding = ItemCalendarDateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.bind(dates[position], position == selectedPosition)
    }

    override fun getItemCount() = dates.size

    fun setSelectedPosition(position: Int) {
        val oldPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(oldPosition)
        notifyItemChanged(selectedPosition)
    }

    inner class DateViewHolder(
        private val binding: ItemCalendarDateBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(date: CalendarDate, isSelected: Boolean) {
            binding.textViewDay.text = date.dayName
            binding.textViewDate.text = date.dayNumber.toString()

            // Set colors based on selection and today
            if (isSelected) {
                // Selected state - solid primary color
                binding.viewSelectionOverlay.visibility = android.view.View.VISIBLE
                binding.viewSelectionOverlay.setBackgroundResource(R.drawable.bg_calendar_selected_modern)
                binding.textViewDay.setTextColor(Color.WHITE)
                binding.textViewDate.setTextColor(Color.WHITE)
            } else if (date.isToday) {
                // Today state - light green with border
                binding.viewSelectionOverlay.visibility = android.view.View.VISIBLE
                binding.viewSelectionOverlay.setBackgroundResource(R.drawable.bg_calendar_today_modern)
                binding.textViewDay.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.primary)
                )
                binding.textViewDate.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.primary)
                )
            } else {
                // Default state
                binding.viewSelectionOverlay.visibility = android.view.View.GONE
                binding.textViewDay.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.text_hint)
                )
                binding.textViewDate.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.text_primary)
                )
            }

            binding.root.setOnClickListener {
                setSelectedPosition(adapterPosition)
                onDateClick(date)
            }
        }
    }
}

/**
 * Data class for calendar date
 */
data class CalendarDate(
    val date: Date,
    val dayName: String,
    val dayNumber: Int,
    val dateString: String,
    val isToday: Boolean = false
)
