package com.example.expensex.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.expensex.db.TransactionEntity
import com.example.expensex.model.FilterType
import com.example.expensex.viewmodel.TransactionViewModel

@Composable
fun TransactionScreen(viewModel: TransactionViewModel) {
    val transactions by viewModel.filteredTransactions.collectAsState()
    val selectedFilter by viewModel.filter.collectAsState()

    Column {
        TransactionToggle(
            selected = selectedFilter,
            onSelected = { viewModel.setFilter(it) }
        )
        LazyColumn {
            items(transactions) { tx ->
                TransactionItem(tx)
            }
        }
    }
}



@Composable
fun TransactionToggle(
    selected: FilterType,
    onSelected: (FilterType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        FilterButton("All", FilterType.ALL, selected, onSelected)
        FilterButton("Income", FilterType.INCOME, selected, onSelected)
        FilterButton("Expense", FilterType.EXPENSE, selected, onSelected)
    }
}

@Composable
fun FilterButton(
    text: String,
    type: FilterType,
    selected: FilterType,
    onSelected: (FilterType) -> Unit
) {
    val isSelected = type == selected

    Text(
        text = text,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Color.Black else Color.LightGray)
            .clickable { onSelected(type) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = if (isSelected) Color.White else Color.Black
    )
}


@Composable
fun TransactionItem(tx: TransactionEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = tx.title)
            Text(text = tx.date.toString())
        }
        Text(
            text = "₹${tx.amount}",
            color = if (tx.type == "INCOME") Color.Green else Color.Red
        )
    }
}