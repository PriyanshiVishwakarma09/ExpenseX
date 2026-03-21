package com.example.expensex.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensex.model.Routes
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
    val selectedPeriod by viewModel.selectedPeriod.collectAsState()
    val chartData by viewModel.chartData.collectAsState()
    val BgLight = Color(0xFFF8F9FA)
    val modelProducer = remember { ChartEntryModelProducer() }

    LaunchedEffect(chartData) {
        if (chartData.entries.isNotEmpty()) {
            modelProducer.setEntries(chartData.entries)
        }
    }

    val primaryTeal = Color(0xFF4F9A94)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(TopBgColor)
        )
        Column {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .height(60.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Budget",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 30.sp,
                    color = Color.White,
                    modifier = Modifier.padding(start = 30.dp, top = 10.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBgColor),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Monthly balance",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "monthly budget",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(150.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Default.Assessment,
                        contentDescription = "Stats",
                        tint = primaryTeal,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "ANALYTICS",
                        fontWeight = FontWeight.Bold,
                        color = primaryTeal,
                        fontSize = 16.sp
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    //.weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
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
                val lineSpec = LineChart.LineSpec(
                    lineColor = primaryTeal.toArgb(),
                    lineThicknessDp = 3f,
                    lineBackgroundShader = DynamicShaders.fromBrush(
                        brush = Brush.verticalGradient(
                            listOf(primaryTeal.copy(alpha = 0.4f), Color.Transparent)
                        )
                    )
                )

                val bottomAxisFormatter =
                    AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
                        val rawLabel =
                            chartData.labels.getOrNull(value.toInt())
                                ?: return@AxisValueFormatter ""

                        when (selectedPeriod) {
                            TimePeriod.Week -> {
                                try {
                                    LocalDate.parse(rawLabel).dayOfWeek.getDisplayName(
                                        TextStyle.SHORT,
                                        Locale.getDefault()
                                    )
                                } catch (e: Exception) {
                                    rawLabel
                                }
                            }

                            TimePeriod.Year -> {
                                try {
                                    Month.of(rawLabel.toInt())
                                        .getDisplayName(TextStyle.SHORT, Locale.getDefault())
                                } catch (e: Exception) {
                                    rawLabel
                                }
                            }

                            else -> rawLabel
                        }
                    }
                if (chartData.entries.isNotEmpty()) {
                    Chart(
                        chart = lineChart(
                            lines = listOf(lineSpec),
                            spacing = 44.dp
                        ),
                        chartModelProducer = modelProducer,
                        bottomAxis = rememberBottomAxis(
                            valueFormatter = bottomAxisFormatter,
                            guideline = null,
                            tickLength = 0.dp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )
                } else {
                    Text("No expenses recorded yet.", color = Color.Gray)
                }
            }
        }
    }

}