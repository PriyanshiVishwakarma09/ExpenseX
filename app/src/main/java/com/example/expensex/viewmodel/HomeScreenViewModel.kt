package com.example.expensex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensex.SessionManager
import com.example.expensex.db.TransactionDao
import com.example.expensex.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlinx.coroutines.flow.combine


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repo: WalletRepository,
    private val transactionDao: TransactionDao,
    sessionManager: SessionManager
) : ViewModel() {

    private val uid = sessionManager.getUid()
        ?: throw IllegalStateException("User not logged in")

    val income = transactionDao.getTotalIncome(uid)
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

    val expense = transactionDao.getTotalExpense(uid)
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

    val recent = transactionDao.getRecent(uid)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
  //  Get recent transactions as a Flow from Room, convert it into a lifecycle-aware StateFlow inside the ViewModel, start observing only when UI is active, and use emptyList() as the initial value

    val balance = combine(income, expense) { inc, exp ->
        inc - exp
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

    }
