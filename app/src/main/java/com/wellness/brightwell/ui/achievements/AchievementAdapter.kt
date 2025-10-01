package com.wellness.brightwell.ui.achievements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wellness.brightwell.data.Achievement
import com.wellness.brightwell.databinding.ItemAchievementBinding

/**
 * Adapter for displaying achievements in a grid
 */
class AchievementAdapter(
    private val achievements: List<Achievement>
) : RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val binding = ItemAchievementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AchievementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        holder.bind(achievements[position])
    }

    override fun getItemCount() = achievements.size

    class AchievementViewHolder(
        private val binding: ItemAchievementBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(achievement: Achievement) {
            binding.textViewIcon.text = achievement.icon
            binding.textViewTitle.text = achievement.title
            binding.textViewDescription.text = achievement.description
            binding.textViewProgress.text = "${achievement.progress} / ${achievement.target}"
            
            // Calculate progress percentage
            val percentage = if (achievement.target > 0) {
                (achievement.progress * 100) / achievement.target
            } else 0
            binding.progressBar.progress = percentage.coerceIn(0, 100)
            
            // Show/hide locked overlay
            if (achievement.isUnlocked) {
                binding.viewLocked.visibility = View.GONE
                binding.textViewLocked.visibility = View.GONE
                binding.cardView.alpha = 1.0f
            } else {
                binding.viewLocked.visibility = View.VISIBLE
                binding.textViewLocked.visibility = View.VISIBLE
                binding.cardView.alpha = 0.6f
            }
        }
    }
}
