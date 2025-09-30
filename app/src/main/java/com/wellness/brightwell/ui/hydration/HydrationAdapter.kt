package com.wellness.brightwell.ui.hydration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wellness.brightwell.data.HydrationEntry
import com.wellness.brightwell.databinding.ItemHydrationBinding
import com.wellness.brightwell.utils.DateUtils

/**
 * RecyclerView adapter for displaying hydration entries
 * @property entries List of hydration entries to display
 * @property onEntryDelete Callback when entry deletion is requested
 */
class HydrationAdapter(
    private val entries: List<HydrationEntry>,
    private val onEntryDelete: (HydrationEntry) -> Unit
) : RecyclerView.Adapter<HydrationAdapter.HydrationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HydrationViewHolder {
        val binding = ItemHydrationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HydrationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HydrationViewHolder, position: Int) {
        holder.bind(entries[position])
    }

    override fun getItemCount(): Int = entries.size

    /**
     * ViewHolder for hydration entry items
     */
    inner class HydrationViewHolder(
        private val binding: ItemHydrationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: HydrationEntry) {
            // Display amount
            binding.textViewAmount.text = "${entry.amount} ml"

            // Display time
            binding.textViewTime.text = DateUtils.formatTime(entry.timestamp)

            // Display glasses equivalent (250ml = 1 glass)
            val glasses = entry.amount / 250.0
            val glassesText = when {
                glasses >= 1.0 -> String.format("%.1f glasses", glasses)
                else -> "Less than 1 glass"
            }
            binding.textViewGlasses.text = glassesText

            // Set up delete button
            binding.buttonDelete.setOnClickListener {
                onEntryDelete(entry)
            }

            // Show water drop icon based on amount
            val icon = when {
                entry.amount >= 1000 -> "💧💧💧"
                entry.amount >= 500 -> "💧💧"
                else -> "💧"
            }
            binding.textViewIcon.text = icon
        }
    }
}
