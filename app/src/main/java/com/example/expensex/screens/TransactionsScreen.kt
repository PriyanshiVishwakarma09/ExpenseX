package com.example.expensex.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.expensex.db.TransactionEntity
import com.example.expensex.model.FilterType
import com.example.expensex.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.example.expensex.viewmodel.WalletViewModel

@Composable
fun TransactionScreen(viewModel: TransactionViewModel , vm: WalletViewModel) {
    val transactions by viewModel.filteredTransactions.collectAsState()
    val selectedFilter by viewModel.filter.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        TransactionToggle(
            selected = selectedFilter,
            onSelected = { viewModel.setFilter(it) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(transactions) { tx ->
                TransactionItem(tx , vm)
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FilterButton("All", FilterType.ALL, selected, onSelected)
        FilterButton("Income", FilterType.INCOME, selected, onSelected)
        FilterButton("Expense", FilterType.EXPENSE, selected, onSelected)
    }
    Spacer(modifier = Modifier.padding(6.dp))
    Divider(
        color = Color(0xFFE0E0E0),
        thickness = 0.8.dp,
        modifier = Modifier.padding(start = 4.dp)
    )
}

    @Composable
    fun FilterButton(
        text: String,
        type: FilterType,
        selected: FilterType,
        onSelected: (FilterType) -> Unit
    ) {
        val isSelected = type == selected
        val TealColor = Color(0xFF3B978F)
        Text(
            text = text,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(if (isSelected) TealColor else Color(0xFFF2F2F2))
                .clickable { onSelected(type) }
                .padding(horizontal = 20.dp, vertical = 10.dp),
            color = if (isSelected) Color.White else Color.Black
        )
    }

@Composable
fun TransactionItem(tx: TransactionEntity , vm: WalletViewModel) {
    val formattedDate = SimpleDateFormat(
        "MMM dd, yyyy",
        Locale.getDefault()
    ).format(Date(tx.date))
    val isIncome = tx.type == "INCOME"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0F2F1)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                tint = Color(0xFF3B978F)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = tx.title, color = Color.Black)

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = formattedDate, color = Color.Gray)
        }
        Text(
            text = (if (isIncome) "+ ₹" else "- ₹") + tx.amount,
            color = if (isIncome) Color(0xFF2E7D32) else Color(0xFFC62828)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = { vm.deleteTransaction(tx) }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Red
            )
        }
    }

    Divider(
        color = Color(0xFFE0E0E0),
        thickness = 0.8.dp,
        modifier = Modifier.padding(start = 68.dp)
    )
}