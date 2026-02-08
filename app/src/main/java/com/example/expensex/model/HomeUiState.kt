package com.example.expensex.model

import com.example.expensex.db.TransactionEntity

data class HomeUiState (
    val totalIncome : Double = 0.0,
    val totalExpense : Double = 0.0 ,
    val totalBalance : Double = 0.0 ,
    val recent : List<TransactionEntity> = emptyList()
)