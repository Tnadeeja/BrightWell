package com.wellness.brightwell.ui.mood

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wellness.brightwell.R
import com.wellness.brightwell.data.MoodEntry
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.databinding.FragmentMoodBinding

/**
 * Fragment for mood journaling with emoji selector
 * Allows users to log their mood with an emoji and optional note
 */
class MoodFragment : Fragment() {

    private var _binding: FragmentMoodBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var prefsManager: PreferencesManager
    private lateinit var moodAdapter: MoodAdapter
    private val moodEntries = mutableListOf<MoodEntry>()
    
    // Available mood emojis
    private val moodEmojis = listOf(
        "😊", "😃", "😄", "😁", "🥰",
        "😌", "😔", "😢", "😭", "😡",
        "😤", "😰", "😱", "🤗", "🤔",
        "😴", "🤒", "🤕", "😷", "🥳"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        prefsManager = PreferencesManager(requireContext())
        setupRecyclerView()
        loadMoodEntries()
        setupClickListeners()
        updateUI()
    }

    /**
     * Set up RecyclerView with adapter
     */
    private fun setupRecyclerView() {
        moodAdapter = MoodAdapter(
            moodEntries = moodEntries,
            onMoodDelete = { entry ->
                showDeleteConfirmation(entry)
            }
        )
        
        binding.recyclerViewMoods.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = moodAdapter
        }
    }

    /**
     * Load mood entries from SharedPreferences
     */
    private fun loadMoodEntries() {
        moodEntries.clear()
        moodEntries.addAll(prefsManager.getMoodEntries())
        moodAdapter.notifyDataSetChanged()
    }

    /**
     * Set up click listeners
     */
    private fun setupClickListeners() {
        binding.fabAddMood.setOnClickListener {
            showMoodSelectorDialog()
        }
        
        binding.buttonShareMood.setOnClickListener {
            shareMoodSummary()
        }
    }

    /**
     * Update UI based on mood entries
     */
    private fun updateUI() {
        if (moodEntries.isEmpty()) {
            binding.recyclerViewMoods.visibility = View.GONE
            binding.emptyStateLayout.visibility = View.VISIBLE
            binding.buttonShareMood.visibility = View.GONE
        } else {
            binding.recyclerViewMoods.visibility = View.VISIBLE
            binding.emptyStateLayout.visibility = View.GONE
            binding.buttonShareMood.visibility = View.VISIBLE
            updateMoodSummary()
        }
    }

    /**
     * Update mood summary card
     */
    private fun updateMoodSummary() {
        if (moodEntries.isEmpty()) {
            binding.moodSummaryCard.visibility = View.GONE
            return
        }
        
        binding.moodSummaryCard.visibility = View.VISIBLE
        
        // Get today's mood entries
        val today = com.wellness.brightwell.utils.DateUtils.getTodayString()
        val todayMoods = moodEntries.filter { it.getDateKey() == today }
        
        if (todayMoods.isNotEmpty()) {
            val latestMood = todayMoods.first()
            binding.textCurrentMood.text = "Today's mood: ${latestMood.emoji}"
            binding.textMoodCount.text = "${todayMoods.size} mood(s) logged today"
        } else {
            binding.textCurrentMood.text = "No mood logged today"
            binding.textMoodCount.text = "Tap + to log your mood"
        }
        
        // Show total entries
        binding.textTotalEntries.text = "Total entries: ${moodEntries.size}"
    }

    /**
     * Show emoji selector dialog
     */
    private fun showMoodSelectorDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_mood_selector, null)
        
        val gridEmojis = dialogView.findViewById<GridLayout>(R.id.gridEmojis)
        val editNote = dialogView.findViewById<EditText>(R.id.editMoodNote)
        
        var selectedEmoji = ""
        
        // Create emoji buttons
        for (emoji in moodEmojis) {
            val emojiCard = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_emoji, gridEmojis, false) as CardView
            
            val textEmoji = emojiCard.findViewById<TextView>(R.id.textEmoji)
            textEmoji.text = emoji
            
            emojiCard.setOnClickListener {
                selectedEmoji = emoji
                // Highlight selected emoji
                highlightSelectedEmoji(gridEmojis, emojiCard)
            }
            
            gridEmojis.addView(emojiCard)
        }
        
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("How are you feeling?")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                if (selectedEmoji.isNotEmpty()) {
                    val note = editNote.text.toString().trim()
                    val entry = MoodEntry(
                        emoji = selectedEmoji,
                        note = note
                    )
                    prefsManager.addMoodEntry(entry)
                    loadMoodEntries()
                    updateUI()
                    Toast.makeText(requireContext(), "Mood logged!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Please select an emoji", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        
        dialog.show()
    }

    /**
     * Highlight the selected emoji in the grid
     */
    private fun highlightSelectedEmoji(gridLayout: GridLayout, selectedCard: CardView) {
        // Reset all cards
        for (i in 0 until gridLayout.childCount) {
            val card = gridLayout.getChildAt(i) as? CardView
            card?.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.emoji_card_background)
            )
        }
        
        // Highlight selected
        selectedCard.setCardBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.emoji_card_selected)
        )
    }

    /**
     * Show delete confirmation dialog
     */
    private fun showDeleteConfirmation(entry: MoodEntry) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Mood Entry")
            .setMessage("Are you sure you want to delete this mood entry?")
            .setPositiveButton("Delete") { _, _ ->
                prefsManager.deleteMoodEntry(entry.id)
                loadMoodEntries()
                updateUI()
                Toast.makeText(requireContext(), "Mood entry deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Share mood summary using implicit intent
     */
    private fun shareMoodSummary() {
        if (moodEntries.isEmpty()) {
            Toast.makeText(requireContext(), "No mood entries to share", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Create summary text
        val summary = buildString {
            appendLine("My Mood Journal Summary 📊")
            appendLine()
            appendLine("Total entries: ${moodEntries.size}")
            appendLine()
            appendLine("Recent moods:")
            
            moodEntries.take(5).forEach { entry ->
                appendLine("${entry.emoji} - ${entry.getFormattedDate()} at ${entry.getFormattedTime()}")
                if (entry.note.isNotEmpty()) {
                    appendLine("  Note: ${entry.note}")
                }
                appendLine()
            }
            
            appendLine("Logged with BrightWell Wellness App")
        }
        
        // Create share intent
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, summary)
            putExtra(Intent.EXTRA_SUBJECT, "My Mood Journal Summary")
        }
        
        startActivity(Intent.createChooser(shareIntent, "Share mood summary via"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
