package com.example.expensex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.first
import androidx.lifecycle.viewModelScope
import com.example.expensex.SessionManager
import com.example.expensex.db.AccountDao
import com.example.expensex.db.CategoryDao
import com.example.expensex.db.CategoryEntity
import com.example.expensex.db.TransactionDao
import com.example.expensex.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine


@HiltViewModel
class WalletViewModel @Inject constructor(
    private val repo: WalletRepository,
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val session: SessionManager
) : ViewModel() {
    // private val _type = MutableStateFlow("EXPENSE")

    val uid: String =
        session.getUid()
            ?: throw IllegalStateException("User not logged in")

    val incomeTotal = transactionDao.getTotalIncome(uid)
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val expenseTotal = transactionDao.getTotalExpense(uid)
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val recent = transactionDao.getRecent(uid)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _type = MutableStateFlow("INCOME")
    val categories = _type.flatMapLatest { type ->
        categoryDao.getCategories(uid, type)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val balance = combine(incomeTotal, expenseTotal) { inc, exp ->
        inc - exp
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    var loading by mutableStateOf(false)
        private set

    fun load(type: String) {
        _type.value = type
    }

    fun addIncome(title: String, amount: Double, categoryId: Int?) {
        viewModelScope.launch {
            val account = repo.getMainAccount(uid)
                ?: return@launch
            repo.addIncome(uid, title, amount, account.id, categoryId)
            load("INCOME")
        }
    }

    fun addExpense(title: String, amount: Double, categoryId: Int) {
        viewModelScope.launch {
            val account = repo.getMainAccount(uid) ?: return@launch
            repo.addExpense(uid, title, amount, account.id, categoryId)
            load("EXPENSE")
        }
    }
}


