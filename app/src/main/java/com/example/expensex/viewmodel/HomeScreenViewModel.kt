package com.example.expensex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensex.db.TransactionDao
import com.example.expensex.db.TransactionEntity
import com.example.expensex.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repo: WalletRepository,
    private val transactionDao: TransactionDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val uid: String = savedStateHandle["uid"]!!

    var balance by mutableStateOf(0.0)
        private set

    var incomeTotal by mutableStateOf(0.0)
        private set

    var expenseTotal by mutableStateOf(0.0)
        private set

    var recent by mutableStateOf<List<TransactionEntity>>(emptyList())
        private set

    fun loadDashboard() {
        viewModelScope.launch {
            val account = repo.getMainAccount(uid)
            balance = account?.balance ?: 0.0

            incomeTotal = transactionDao.getTotalIncome(uid) ?: 0.0
            expenseTotal = transactionDao.getTotalExpense(uid) ?: 0.0
            recent = transactionDao.getRecent(uid)
        }
    }
}
