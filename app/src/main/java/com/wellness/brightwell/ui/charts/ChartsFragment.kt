package com.wellness.brightwell.ui.charts

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.wellness.brightwell.data.PreferencesManager
import com.wellness.brightwell.databinding.FragmentChartsBinding
import com.wellness.brightwell.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment for displaying charts and graphs
 */
class ChartsFragment : Fragment() {

    private var _binding: FragmentChartsBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefsManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefsManager = PreferencesManager(requireContext())

        setupCharts()
        loadData()
    }

    /**
     * Set up all charts
     */
    private fun setupCharts() {
        setupLineChart()
        setupBarChart()
        setupPieChart()
    }

    /**
     * Set up line chart for habit trends
     */
    private fun setupLineChart() {
        binding.lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            setDrawGridBackground(false)
            
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
            }
            
            axisLeft.apply {
                setDrawGridLines(true)
                axisMinimum = 0f
            }
            
            axisRight.isEnabled = false
            legend.isEnabled = true
            
            animateX(1000, Easing.EaseInOutQuad)
        }
    }

    /**
     * Set up bar chart for weekly comparison
     */
    private fun setupBarChart() {
        binding.barChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            setDrawGridBackground(false)
            
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
            }
            
            axisLeft.apply {
                setDrawGridLines(true)
                axisMinimum = 0f
            }
            
            axisRight.isEnabled = false
            legend.isEnabled = true
            
            animateY(1000, Easing.EaseInOutQuad)
        }
    }

    /**
     * Set up pie chart for mood distribution
     */
    private fun setupPieChart() {
        binding.pieChart.apply {
            description.isEnabled = false
            setUsePercentValues(true)
            setDrawHoleEnabled(true)
            setHoleColor(Color.WHITE)
            setTransparentCircleRadius(61f)
            setDrawEntryLabels(false)
            
            legend.isEnabled = true
            
            animateY(1000, Easing.EaseInOutQuad)
        }
    }

    /**
     * Load data into charts
     */
    private fun loadData() {
        loadLineChartData()
        loadBarChartData()
        loadPieChartData()
    }

    /**
     * Load habit completion trend data
     */
    private fun loadLineChartData() {
        val habits = prefsManager.loadHabits()
        if (habits.isEmpty()) {
            binding.lineChart.visibility = View.GONE
            binding.textNoLineData.visibility = View.VISIBLE
            return
        }

        val entries = mutableListOf<Entry>()
        val labels = mutableListOf<String>()
        val calendar = Calendar.getInstance()

        // Last 7 days
        for (i in 6 downTo 0) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            val dayLabel = SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)
            
            val completed = habits.count { it.isCompletedOn(dateString) }
            val percentage = if (habits.isNotEmpty()) (completed * 100f) / habits.size else 0f
            
            entries.add(Entry((6 - i).toFloat(), percentage))
            labels.add(dayLabel)
        }

        val dataSet = LineDataSet(entries, "Habit Completion %").apply {
            color = Color.parseColor("#6366F1")
            setCircleColor(Color.parseColor("#6366F1"))
            lineWidth = 2f
            circleRadius = 4f
            setDrawCircleHole(false)
            valueTextSize = 10f
            setDrawFilled(true)
            fillColor = Color.parseColor("#A5B4FC")
        }

        binding.lineChart.apply {
            data = LineData(dataSet)
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            invalidate()
            visibility = View.VISIBLE
        }
        binding.textNoLineData.visibility = View.GONE
    }

    /**
     * Load weekly habit comparison data
     */
    private fun loadBarChartData() {
        val habits = prefsManager.loadHabits()
        if (habits.isEmpty()) {
            binding.barChart.visibility = View.GONE
            binding.textNoBarData.visibility = View.VISIBLE
            return
        }

        val entries = mutableListOf<BarEntry>()
        val labels = mutableListOf<String>()
        val calendar = Calendar.getInstance()

        // Last 7 days
        for (i in 6 downTo 0) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            val dayLabel = SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)
            
            val completed = habits.count { it.isCompletedOn(dateString) }.toFloat()
            
            entries.add(BarEntry((6 - i).toFloat(), completed))
            labels.add(dayLabel)
        }

        val dataSet = BarDataSet(entries, "Habits Completed").apply {
            color = Color.parseColor("#34D399")
            valueTextSize = 10f
        }

        binding.barChart.apply {
            data = BarData(dataSet)
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            invalidate()
            visibility = View.VISIBLE
        }
        binding.textNoBarData.visibility = View.GONE
    }

    /**
     * Load mood distribution data
     */
    private fun loadPieChartData() {
        val moods = prefsManager.loadMoods()
        if (moods.isEmpty()) {
            binding.pieChart.visibility = View.GONE
            binding.textNoPieData.visibility = View.VISIBLE
            return
        }

        val moodCounts = moods.groupingBy { it.emoji }.eachCount()
        val entries = moodCounts.map { (emoji, count) ->
            PieEntry(count.toFloat(), emoji)
        }

        val colors = listOf(
            Color.parseColor("#F87171"), // Red
            Color.parseColor("#FBBF24"), // Yellow
            Color.parseColor("#34D399"), // Green
            Color.parseColor("#60A5FA"), // Blue
            Color.parseColor("#A78BFA"), // Purple
            Color.parseColor("#F472B6")  // Pink
        )

        val dataSet = PieDataSet(entries, "Mood Distribution").apply {
            this.colors = colors
            valueTextSize = 12f
            valueTextColor = Color.WHITE
            valueFormatter = PercentFormatter(binding.pieChart)
        }

        binding.pieChart.apply {
            data = PieData(dataSet)
            invalidate()
            visibility = View.VISIBLE
        }
        binding.textNoPieData.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
