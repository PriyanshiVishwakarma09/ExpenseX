package com.example.expensex.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.expensex.ui.viewmodel.ExpenseChartViewModel
import com.example.expensex.ui.viewmodel.TimePeriod
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ExpenseTrackerScreen(viewModel: ExpenseChartViewModel) {
    // 1. Collect State from your ViewModel
    val selectedPeriod by viewModel.selectedPeriod.collectAsState()
    val chartData by viewModel.chartData.collectAsState()

    // 2. Vico's Producer (this handles the smooth morphing animation)
    val modelProducer = remember { ChartEntryModelProducer() }

    // 3. Feed the database data into the chart whenever it changes
    LaunchedEffect(chartData) {
        if (chartData.entries.isNotEmpty()) {
            modelProducer.setEntries(chartData.entries)
        }
    }

    // Your brand colors
    val primaryTeal = Color(0xFF4F9A94)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- TIME TOGGLE ROW ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TimePeriod.values().forEach { period ->
                val isSelected = selectedPeriod == period
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) primaryTeal else Color.Transparent)
                        .clickable { viewModel.setTimePeriod(period) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = period.name,
                        color = if (isSelected) Color.White else Color.Gray,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        // --- THE LIVE CHART ---
        // Style the line and the gradient fill underneath
        val lineSpec = LineChart.LineSpec(
            lineColor = primaryTeal.toArgb(),
            lineThicknessDp = 3f,
            lineBackgroundShader = DynamicShaders.fromBrush(
                brush = Brush.verticalGradient(
                    listOf(primaryTeal.copy(alpha = 0.4f), Color.Transparent)
                )
            )
        )

        // Format the raw DAO dates ("2023-10-25" -> "Wed")
        val bottomAxisFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
            val rawLabel = chartData.labels.getOrNull(value.toInt()) ?: return@AxisValueFormatter ""

            when (selectedPeriod) {
                TimePeriod.Week -> {
                    try {
                        LocalDate.parse(rawLabel).dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    } catch (e: Exception) { rawLabel }
                }
                TimePeriod.Year -> {
                    try {
                        Month.of(rawLabel.toInt()).getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    } catch (e: Exception) { rawLabel }
                }
                else -> rawLabel // Month and Day views already have clean labels
            }
        }

        // Render the actual chart component
        if (chartData.entries.isNotEmpty()) {
            Chart(
                chart = lineChart(
                    lines = listOf(lineSpec),
                    spacing = 44.dp // Curve smoothness
                ),
                chartModelProducer = modelProducer,
                bottomAxis = rememberBottomAxis(
                    valueFormatter = bottomAxisFormatter,
                    guideline = null, // Removes vertical grid lines
                    tickLength = 0.dp // Hides the little tick marks on the axis
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        } else {
            // Optional: Show a "No data yet" message if the database is empty
            Text("No expenses recorded yet.", color = Color.Gray)
        }
    }
}