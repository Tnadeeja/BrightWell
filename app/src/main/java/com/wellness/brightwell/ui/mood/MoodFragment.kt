package com.wellness.brightwell.ui.mood

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.wellness.brightwell.R
import com.wellness.brightwell.data.MoodEntry
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.databinding.FragmentMoodBinding

/**
 * Fragment for logging and viewing mood journal entries
 * Users can select an emoji to represent their mood and add optional notes
 */
class MoodFragment : Fragment() {

    private var _binding: FragmentMoodBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefsManager: PreferencesManager
    private lateinit var moodAdapter: MoodAdapter
    private val moods = mutableListOf<MoodEntry>()

    // Available mood emojis
    private val moodEmojis = listOf(
        "\uD83D\uDE0A", "\uD83D\uDE03", "\uD83D\uDE04", "\uD83D\uDE01",
        "\uD83E\uDD70", "\uD83D\uDE0D", "\uD83E\uDD17", "\uD83D\uDE0C",
        "\uD83D\uDE10", "\uD83D\uDE11", "\uD83D\uDE36", "\uD83D\uDE42",
        "\uD83D\uDE0F", "\uD83D\uDE12", "\uD83D\uDE44", "\uD83D\uDE2C",
        "\uD83D\uDE14", "\uD83D\uDE1E", "\uD83D\uDE1F", "\uD83D\uDE22",
        "\uD83D\uDE2D", "\uD83D\uDE29", "\uD83D\uDE2B", "\uD83D\uDE24",
        "\uD83D\uDE20", "\uD83D\uDE21", "\uD83E\uDD2C", "\uD83D\uDE31",
        "\uD83D\uDE28", "\uD83D\uDE30", "\uD83D\uDE25", "\uD83D\uDE13",
        "\uD83E\uDD12", "\uD83E\uDD15", "\uD83E\uDD22", "\uD83E\uDD2E",
        "\uD83E\uDD71", "\uD83D\uDE34", "\uD83D\uDE2A", "\uD83E\uDD2F"
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

        // Initialize PreferencesManager
        prefsManager = PreferencesManager(requireContext())

        // Set up RecyclerView
        setupRecyclerView()

        // Load moods from storage
        loadMoods()

        // Set up FAB to add new mood
        binding.fabAddMood.setOnClickListener {
            showAddMoodDialog()
        }

        // Set up share button
        binding.buttonShare.setOnClickListener {
            shareMoodSummary()
        }
    }

    /**
     * Set up the RecyclerView with adapter and layout manager
     */
    private fun setupRecyclerView() {
        moodAdapter = MoodAdapter(
            moods = moods,
            onMoodDelete = { mood ->
                showDeleteConfirmation(mood)
            }
        )

        binding.recyclerViewMoods.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = moodAdapter
        }
    }

    /**
     * Load moods from SharedPreferences and update UI
     */
    private fun loadMoods() {
        moods.clear()
        moods.addAll(prefsManager.loadMoods())
        moodAdapter.notifyDataSetChanged()
        updateEmptyState()
    }

    /**
     * Show dialog to add a new mood entry
     */
    private fun showAddMoodDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_mood, null)
        val noteInput = dialogView.findViewById<TextInputEditText>(R.id.editTextMoodNote)
        val emojiGrid = dialogView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerViewEmojis)

        var selectedEmoji = moodEmojis[0]

        // Set up emoji grid
        val emojiAdapter = EmojiAdapter(moodEmojis) { emoji ->
            selectedEmoji = emoji
        }
        emojiGrid.layoutManager = GridLayoutManager(requireContext(), 8)
        emojiGrid.adapter = emojiAdapter

        AlertDialog.Builder(requireContext())
            .setTitle("Log Your Mood")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val note = noteInput.text.toString().trim()
                val newMood = MoodEntry(
                    emoji = selectedEmoji,
                    note = note
                )
                prefsManager.addMood(newMood)
                loadMoods()
                Toast.makeText(requireContext(), "Mood logged!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Show confirmation dialog before deleting a mood
     * @param mood The mood entry to delete
     */
    private fun showDeleteConfirmation(mood: MoodEntry) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Mood Entry")
            .setMessage("Are you sure you want to delete this mood entry?")
            .setPositiveButton("Delete") { _, _ ->
                prefsManager.deleteMood(mood.id)
                loadMoods()
                Toast.makeText(requireContext(), "Mood entry deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Share a summary of recent moods
     */
    private fun shareMoodSummary() {
        if (moods.isEmpty()) {
            Toast.makeText(requireContext(), "No moods to share", Toast.LENGTH_SHORT).show()
            return
        }

        val summary = buildString {
            appendLine("My Mood Journal Summary")
            appendLine("========================")
            appendLine()
            moods.take(5).forEach { mood ->
                appendLine("${mood.emoji} - ${com.wellness.brightwell.utils.DateUtils.formatDateTime(mood.timestamp)}")
                if (mood.note.isNotEmpty()) {
                    appendLine("   ${mood.note}")
                }
                appendLine()
            }
            appendLine("Tracked with BrightWell")
        }

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, summary)
            type = "text/plain"
        }

        startActivity(Intent.createChooser(shareIntent, "Share Mood Summary"))
    }

    /**
     * Show or hide empty state view based on mood count
     */
    private fun updateEmptyState() {
        if (moods.isEmpty()) {
            binding.recyclerViewMoods.visibility = View.GONE
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.buttonShare.isEnabled = false
        } else {
            binding.recyclerViewMoods.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
            binding.buttonShare.isEnabled = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
