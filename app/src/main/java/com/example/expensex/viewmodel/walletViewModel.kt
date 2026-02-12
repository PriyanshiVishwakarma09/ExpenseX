package com.example.expensex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensex.db.AccountDao
import com.example.expensex.db.CategoryDao
import com.example.expensex.db.CategoryEntity
import com.example.expensex.db.CategorySum
import com.example.expensex.db.TransactionDao
import com.example.expensex.db.TransactionEntity
import com.example.expensex.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch


@HiltViewModel
class WalletViewModel @Inject constructor(
    private val repo: WalletRepository,
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
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

    var categories by mutableStateOf<List<CategoryEntity>>(emptyList())
        private set

    var categoryStats by mutableStateOf<List<CategorySum>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    fun load(type: String = "INCOME") {
        viewModelScope.launch {
            loading = true

            val account = repo.getMainAccount(uid)
            balance = account?.balance ?: 0.0

            incomeTotal = transactionDao.getTotalIncome(uid) ?: 0.0
            expenseTotal = transactionDao.getTotalExpense(uid) ?: 0.0

            recent = transactionDao.getRecent(uid)
            categories = categoryDao.getCategories(uid, type)
            categoryStats = transactionDao.getCategoryStats(uid)

            loading = false
        }
    }

    fun addIncome(title: String, amount: Double, categoryId: Int?) {
        viewModelScope.launch {
            val account = repo.getMainAccount(uid) ?: return@launch //get the user's main account if it doesn't exist , stop this function immediately .
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
