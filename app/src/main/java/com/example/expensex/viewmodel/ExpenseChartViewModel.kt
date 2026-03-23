package com.example.expensex.ui.viewmodel // Adjust to your package name

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensex.SessionManager
import com.example.expensex.db.TransactionDao
import com.example.expensex.model.TimePeriodSum
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryOf
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId

enum class TimePeriod { Week, Month, Year }

data class ChartData(
    val entries: List<FloatEntry>,
    val labels: List<String>
)

@HiltViewModel
class ExpenseChartViewModel @Inject constructor(
    private val dao: TransactionDao,
    sessionManager: SessionManager
) : ViewModel() {
    private val _selectedPeriod = MutableStateFlow(TimePeriod.Week)
    val selectedPeriod: StateFlow<TimePeriod> = _selectedPeriod.asStateFlow()

    private val userId = sessionManager.getUid()
        ?: throw IllegalStateException("User not logged in")




    @OptIn(ExperimentalCoroutinesApi::class)
    val chartData: StateFlow<ChartData> = _selectedPeriod.flatMapLatest { period ->
        val now = LocalDate.now()
        val zone = ZoneId.systemDefault()

        when (period) {
            TimePeriod.Week -> {
                // Get timestamps for Monday 00:00:00 to Sunday 23:59:59
                val startOfWeek = now.with(DayOfWeek.MONDAY)
                    .atStartOfDay(zone).toInstant().toEpochMilli()
                val endOfWeek = now.with(DayOfWeek.SUNDAY)
                    .atTime(23, 59, 59).atZone(zone).toInstant().toEpochMilli()

                dao.getExpensesPerDay(userId, startOfWeek, endOfWeek)
                    .map {
                        val start = now.with(DayOfWeek.MONDAY)
                        formatWeek(it, start)
                    }
            }

            TimePeriod.Month -> {
                // Get timestamps for the 1st day to the last day of the current month
                val startOfMonth = now.withDayOfMonth(1)
                    .atStartOfDay(zone).toInstant().toEpochMilli()
                val endOfMonth = now.withDayOfMonth(now.lengthOfMonth())
                    .atTime(23, 59, 59).atZone(zone).toInstant().toEpochMilli()

                // Fetch daily data for the whole month, then group it into 4 weeks
                dao.getExpensesPerDay(userId, startOfMonth, endOfMonth)
                    .map { groupDaysIntoWeeks(it) }
            }

            TimePeriod.Year -> {
                // Get timestamps from Jan 1st to Dec 31st of the current year
                val startOfYear = now.withDayOfYear(1)
                    .atStartOfDay(zone).toInstant().toEpochMilli()
                val endOfYear = now.withDayOfYear(now.lengthOfYear())
                    .atTime(23, 59, 59).atZone(zone).toInstant().toEpochMilli()

                dao.getExpensesPerMonth(userId, startOfYear, endOfYear)
                    .map { formatYear(it) }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChartData(emptyList(), emptyList())
    )

    // Call this from your UI when the user clicks a tab
    fun setTimePeriod(period: TimePeriod) {
        _selectedPeriod.value = period
    }

    private fun formatWeek(data: List<TimePeriodSum>, start: LocalDate): ChartData {

        val map = data.associateBy { it.timeLabel }

        val entries = mutableListOf<FloatEntry>()
        val labels = mutableListOf<String>()

        for (i in 0..6) {
            val date = start.plusDays(i.toLong())
            val key = date.toString()

            val total = map[key]?.total ?: 0.0

            entries.add(entryOf(i.toFloat(), total.toFloat()))
            labels.add(key)
        }

        return ChartData(entries, labels)
    }

    private fun formatYear(data: List<TimePeriodSum>): ChartData {
        val map = data.associateBy { it.timeLabel } // "01", "02", ...

        val entries = mutableListOf<FloatEntry>()
        val labels = mutableListOf<String>()

        for (month in 1..12) {
            val key = month.toString().padStart(2, '0') // "01", "02"
            val total = map[key]?.total ?: 0.0

            entries.add(entryOf((month - 1).toFloat(), total.toFloat()))
            labels.add(key)
        }

        return ChartData(entries, labels)
    }

    /**
     * Takes up to 31 days of data from the DAO and bundles them into 4 "Weeks" for the Monthly view.
     */
    private fun groupDaysIntoWeeks(dailyData: List<TimePeriodSum>): ChartData {
        var week1Total = 0.0 // Days 1-7
        var week2Total = 0.0 // Days 8-14
        var week3Total = 0.0 // Days 15-21
        var week4Total = 0.0 // Days 22+
        dailyData.forEach { item ->
            // Extract the day number from "YYYY-MM-DD"
            val dayOfMonth = item.timeLabel.substringAfterLast("-").toIntOrNull() ?: 1
            when(dayOfMonth){
                in 1..7 -> week1Total += item.total
                in 8..14 -> week2Total += item.total
                in 15..21 -> week3Total += item.total
                else -> week4Total += item.total
            }
        }
        val entries = listOf(
            entryOf(0f, week1Total.toFloat()),
            entryOf(1f, week2Total.toFloat()),
            entryOf(2f, week3Total.toFloat()),
            entryOf(3f, week4Total.toFloat())
        )
        val labels = listOf("W1", "W2", "W3", "W4")
        return ChartData(entries, labels)
    }
}