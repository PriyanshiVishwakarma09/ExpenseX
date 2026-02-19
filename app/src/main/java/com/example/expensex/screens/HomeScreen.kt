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


        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2F8F83)
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 22.dp
                )
            ) {

                Text(
                    "Total Balance",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    "₹ ${"%,.2f".format(balance)}",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column {
                        Text(
                            "Income",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            "₹ ${"%,.2f".format(income)}",
                            color = Color(0xFFB9F6CA),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }

                    Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                        Text(
                            "Expenses",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            "₹ ${"%,.2f".format(expense)}",
                            color = Color(0xFFFFCDD2),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Transactions History",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                bottom = 24.dp
            )
        ) {
            items(recent) { tx ->
                TransactionRow(tx)
            }
        }
    }
}


@Composable
fun TransactionRow(tx: TransactionEntity) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(
                    tx.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    SimpleDateFormat("dd MMM", Locale.getDefault())
                        .format(Date(tx.date)),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Text(
                (if (tx.type == "INCOME") "+" else "-") +
                        "₹${"%,.2f".format(tx.amount)}",
                color = if (tx.type == "INCOME")
                    Color(0xFF2E7D32)
                else
                    Color(0xFFC62828),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}
