package com.example.expensex.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.expensex.db.CategoryEntity
import com.example.expensex.viewmodel.WalletViewModel


@Composable
fun AddTransactionScreen(vm: WalletViewModel){
    var type by remember { mutableStateOf("EXPENSE") }
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<CategoryEntity?>(null) }
    LaunchedEffect(Unit) {
        vm.ensureDefaultCategories ()
    }
    val categories by vm.categories.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(type) {
        vm.load(type)
        selectedCategory = null
    }

    LaunchedEffect(Unit) {
        vm.uiEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Row {
                Button(
                    onClick = { type = "INCOME" },
                    colors = ButtonDefaults.buttonColors(
                        if (type == "INCOME") MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondary
                    )
                ) { Text("Income") }

                Spacer(Modifier.width(10.dp))

                Button(
                    onClick = { type = "EXPENSE" },
                    colors = ButtonDefaults.buttonColors(
                        if (type == "EXPENSE") MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondary
                    )
                ) { Text("Expense") }
            }

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(Modifier.height(12.dp))

            var expanded by remember { mutableStateOf(false) }

            Box {

                OutlinedTextField(
                    value = selectedCategory?.name ?: "Select Category",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.clickable { expanded = true }
                        )
                    }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (categories.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No categories") },
                            onClick = { expanded = false }
                        )
                    }

                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(30.dp))

            Button(
                onClick = {
                    val amt = amount.toDoubleOrNull() ?: return@Button
                    if (type == "INCOME") {
                        vm.addIncome(title, amt, selectedCategory?.id)
                    } else {
                        val catId = selectedCategory?.id ?: return@Button
                        vm.addExpense(title, amt, catId)
                    }
                    title = ""
                    amount = ""
                    selectedCategory = null
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add $type")
            }
        }
    }
}


