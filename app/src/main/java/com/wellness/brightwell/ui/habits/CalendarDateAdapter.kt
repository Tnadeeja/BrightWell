package com.wellness.brightwell.ui.habits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wellness.brightwell.R
import com.wellness.brightwell.databinding.ItemCalendarDateBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Simple Calendar Adapter - Clean Implementation
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
            // Set day name and date number
            binding.textViewDay.text = date.dayName
            binding.textViewDate.text = date.dayNumber.toString()

            // Simple logic: if today, use green background and white text
            if (date.isToday) {
                // TODAY - Green background, white text
                binding.root.setBackgroundResource(R.drawable.calendar_today_background)
                binding.textViewDay.setTextColor(ContextCompat.getColor(binding.root.context, android.R.color.white))
                binding.textViewDate.setTextColor(ContextCompat.getColor(binding.root.context, android.R.color.white))
            } else {
                // NOT TODAY - White background, dark text
                binding.root.setBackgroundResource(R.drawable.calendar_item_background)
                binding.textViewDay.setTextColor(ContextCompat.getColor(binding.root.context, R.color.text_hint))
                binding.textViewDate.setTextColor(ContextCompat.getColor(binding.root.context, R.color.text_primary))
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
