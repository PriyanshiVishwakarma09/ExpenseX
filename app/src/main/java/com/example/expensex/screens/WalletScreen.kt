package com.example.expensex.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensex.repository.WalletRepository
import com.example.expensex.viewmodel.walletViewModel

@Composable
fun WalletScreen(vm : walletViewModel) {
    var amount by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("Initial Income") }

    LaunchedEffect(Unit) {
        vm.load()
    }

    Column(modifier = Modifier.padding((20.dp))) {
        Text("Total Balance" , fontSize = 20.sp)
        Text("₹ ${vm.balance}" , fontSize = 32.sp)

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = title ,
            onValueChange = {title = it},
            label = {Text("Source")}
        )

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") }
        )

        Button(onClick = {
            vm.addIncome( title, amount.toDouble())
            amount = ""
        }) {
            Text("Add Income")
        }

    }
}
