package com.example.expensex.model

data class BudgetUiState(
    val budgetAmount: Double = 0.0,
    val spentAmount: Double = 0.0,
    val remainingAmount: Double = 0.0,
    val progress: Float = 0f,
    val isExceeded: Boolean = false,
    val exceededAmount: Double = 0.0,
    val isLoading: Boolean = true,
    val hasBudget: Boolean = false
)