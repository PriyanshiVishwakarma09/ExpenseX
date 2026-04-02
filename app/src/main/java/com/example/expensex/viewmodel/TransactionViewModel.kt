package com.example.expensex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensex.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import com.example.expensex.db.TransactionDao
import com.example.expensex.model.FilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val dao: TransactionDao,
    sessionManager: SessionManager
) : ViewModel() {
    private val userId =  sessionManager.getUid()
        ?: throw IllegalStateException("User not logged in")
    private val _filter = MutableStateFlow(FilterType.ALL)
    val filter = _filter

    private val allTransactions = dao.getAllTransactions(userId)

    val filteredTransactions = combine(allTransactions, _filter) { list, filter ->
        when (filter) {
            FilterType.INCOME -> list.filter { it.type == "INCOME" }
            FilterType.EXPENSE -> list.filter { it.type == "EXPENSE" }
            FilterType.ALL -> list
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun setFilter(type: FilterType) {
        _filter.value = type
    }
}
