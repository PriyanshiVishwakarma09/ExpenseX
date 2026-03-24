package com.example.expensex.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensex.db.BudgetDao
import com.example.expensex.db.MonthlyBudget
import com.example.expensex.db.TransactionDao
import com.example.expensex.model.BudgetUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import java.util.Calendar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState: StateFlow<BudgetUiState> = _uiState
  //  val exceededAmount: Double = 0.0

    private fun getCurrentMonthYear(): Pair<Int, Int> {
        val calendar = Calendar.getInstance()
        return Pair(
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.YEAR)
        )
    }

    fun loadBudget() {
        viewModelScope.launch {

            val (month, year) = getCurrentMonthYear()

            val budget = budgetDao.getBudget(month, year)

            val monthStr = String.format("%02d", month)
            val yearStr = year.toString()

            val spent = transactionDao.getMonthlyExpenses(monthStr, yearStr)

            val budgetAmount = budget?.amount ?: 0.0
            val remaining = (budgetAmount - spent).coerceAtLeast(0.0)

            val rawProgress = if (budgetAmount > 0)
                (spent / budgetAmount).toFloat()
            else 0f

            val progress = rawProgress.coerceIn(0f, 1f)
            val exceededAmount = if (spent > budgetAmount) {
                spent - budgetAmount
            } else 0.0

            _uiState.value = BudgetUiState(
                budgetAmount = budgetAmount,
                spentAmount = spent,
                remainingAmount = remaining,
                progress = progress,
                isExceeded = spent > budgetAmount,
                exceededAmount = exceededAmount,
                hasBudget = budget != null,
                isLoading = false
            )
        }
    }

    fun setBudget(amount: Double) {
        viewModelScope.launch {
            val (month, year) = getCurrentMonthYear()
            val budget = MonthlyBudget(
                amount = amount,
                month = month,
                year = year
            )
            budgetDao.insertBudget(budget)
            loadBudget()
        }
    }

    fun deleteBudget() {
        viewModelScope.launch {

            val (month, year) = getCurrentMonthYear()
            val budget = budgetDao.getBudget(month, year)

            budget?.let {
                budgetDao.deleteBudget(it)
            }

            loadBudget()
        }
    }

    fun getBudgetStatus(progress: Float): BudgetStatus {
        return when {
            progress < 0.7f -> BudgetStatus.SAFE
            progress < 0.9f -> BudgetStatus.WARNING
            else -> BudgetStatus.DANGER
        }
    }

    fun getStatusText(state: BudgetUiState): String {
        return when {
            !state.hasBudget && state.spentAmount > 0 ->
                "No budget set (Spent ₹${String.format("%.2f", state.spentAmount)})"

            !state.hasBudget ->
                "No budget set"

            state.isExceeded ->
                "Exceeded by ₹${String.format("%.2f", state.exceededAmount)}"

            else ->
                "Remaining ₹${String.format("%.2f", state.remainingAmount)}"
        }
    }
}

enum class BudgetStatus {
    SAFE, WARNING, DANGER
}
