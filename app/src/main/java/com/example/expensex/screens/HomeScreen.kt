package com.example.expensex.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensex.db.TransactionEntity
import com.example.expensex.viewmodel.HomeScreenViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(vm: HomeScreenViewModel) {

    val balance by vm.balance.collectAsState()
    val income by vm.income.collectAsState()
    val expense by vm.expense.collectAsState()
    val recent by vm.recent.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2F8F83)),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Total Balance", color = Color.White)
                Text(
                    "₹ $balance",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Income", color = Color.White)
                        Text("₹ $income", color = Color.Green)
                    }
                    Column {
                        Text("Expenses", color = Color.White)
                        Text("₹ $expense", color = Color.Red)
                    }
                }
            }
        }

        Text(
            "Transactions History",
            modifier = Modifier.padding(start = 16.dp, top = 12.dp),
            fontWeight = FontWeight.Bold
        )


        LazyColumn {
            items(recent) { tx ->
                TransactionRow(tx)
            }
        }
    }
}


@Composable
fun TransactionRow(tx: TransactionEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(tx.title, fontWeight = FontWeight.Bold)
            Text(
                SimpleDateFormat("dd MMM", Locale.getDefault())
                    .format(Date(tx.date)),
                fontSize = 12.sp
            )
        }

        Text(
            (if (tx.type == "INCOME") "+" else "-") + "₹${tx.amount}",
            color = if (tx.type == "INCOME") Color.Green else Color.Red,
            fontWeight = FontWeight.Bold
        )
    }
}
