package com.wellness.brightwell.ui.templates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wellness.brightwell.data.HabitTemplate
import com.wellness.brightwell.databinding.ItemTemplateBinding

class TemplateAdapter(
    private val templates: List<HabitTemplate>,
    private val onTemplateClick: (HabitTemplate) -> Unit
) : RecyclerView.Adapter<TemplateAdapter.TemplateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateViewHolder {
        val binding = ItemTemplateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TemplateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TemplateViewHolder, position: Int) {
        holder.bind(templates[position], onTemplateClick)
    }

    override fun getItemCount() = templates.size

    class TemplateViewHolder(
        private val binding: ItemTemplateBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(template: HabitTemplate, onClick: (HabitTemplate) -> Unit) {
            binding.textViewIcon.text = template.icon
            binding.textViewName.text = template.name
            binding.textViewDescription.text = template.description
            binding.textViewHabitCount.text = "${template.habits.size} habits"
            
            binding.root.setOnClickListener {
                onClick(template)
            }
        }
    }
}
