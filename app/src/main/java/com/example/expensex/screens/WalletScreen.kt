package com.example.expensex.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensex.db.CategoryEntity
import com.example.expensex.viewmodel.WalletViewModel

val TealColor = Color(0xFF3B978F)
val BgLightColor = Color(0xFFF8F9FA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    vm: WalletViewModel,
    snackbarHostState: SnackbarHostState
) {
    var type by remember { mutableStateOf("EXPENSE") }
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<CategoryEntity?>(null) }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }

    fun formatDate(millis: Long): String {
        val formatter = java.text.SimpleDateFormat("dd MM yyyy" , java.util.Locale.getDefault())
        return formatter.format(java.util.Date(millis))
    }

    LaunchedEffect(Unit) {
        vm.ensureDefaultCategories()
    }

    val categories by vm.categories.collectAsState()

    LaunchedEffect(type) {
        vm.load(type)
        selectedCategory = null
    }

    LaunchedEffect(Unit) {
        vm.uiEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Column(
        modifier = Modifier
            .background(BgLightColor)
          //  .statusBarsPadding()
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
                modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(TealColor)
            )

            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(24.dp))
                    Text("Add ${type.lowercase().replaceFirstChar { it.uppercase() }}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Icon(Icons.Default.MoreHoriz, contentDescription = "More", tint = Color.White, modifier = Modifier.size(28.dp))
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(32.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        TransactionTypeToggle(
                            selectedType = type,
                            onTypeSelected = { type = it })

                        Spacer(modifier = Modifier.height(24.dp))

                        FormLabel(text = "Name")
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TealColor,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            )
                        )

                        Spacer(Modifier.height(20.dp))

                        FormLabel(text = "Amount")
                        OutlinedTextField(
                            value = amount,
                            onValueChange = {
                                if (it.matches(Regex("^\\d{0,7}(\\.\\d{0,2})?$"))) {
                                    amount = it
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            leadingIcon = {
                                Text(" ", color = TealColor, fontWeight = FontWeight.Bold)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TealColor,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            )
                        )
                        Spacer(Modifier.height(20.dp))

                        FormLabel(text = "Date")
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showDatePicker = true }
                        ) {
                            OutlinedTextField(
                                value = formatDate(selectedDate),
                                onValueChange = {},
                                readOnly = true,
                                enabled = false,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        tint = Color.Gray
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = TealColor,
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    disabledBorderColor = Color(0xFFE0E0E0)
                                )
                            )
                        }

                        Spacer(Modifier.height(20.dp))

                        FormLabel(text = "Category")
                        var expanded by remember { mutableStateOf(false) }
                        Box {
                            OutlinedTextField(
                                value = selectedCategory?.name ?: "Select Category",
                                onValueChange = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        expanded = true
                                    },
                                readOnly = true,
                                enabled = false,
                                shape = RoundedCornerShape(12.dp),
                                trailingIcon = {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = Color.Black,
                                    disabledBorderColor = Color(0xFFE0E0E0)
                                )
                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.fillMaxWidth(0.85f).background(Color.White)
                            ) {
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

                        Spacer(Modifier.height(24.dp))

                        Button(
                            onClick = {
                                val amt = amount.toDoubleOrNull() ?: return@Button
                                val date = selectedDate
                                if (type == "INCOME") {
                                    vm.addIncome(title, amt, selectedCategory?.id, date)
                                } else {
                                    val catId = selectedCategory?.id ?: return@Button
                                    vm.addExpense(title, amt, catId, date)
                                }
                                title = ""
                                amount = ""
                                selectedCategory = null
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(30.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TealColor)
                        ) {
                            Text("Save", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
    if(showDatePicker){
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDate = datePickerState.selectedDateMillis ?: selectedDate
                    showDatePicker = false
                } , colors = ButtonDefaults.buttonColors(TealColor)) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false } , colors = ButtonDefaults.buttonColors(
                    TealColor)) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun FormLabel(text: String) {
    Text(text = text.uppercase(),
        fontSize = 12.sp,
        color = Color.Gray,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.sp)
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun TransactionTypeToggle(selectedType: String, onTypeSelected: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F7FA), RoundedCornerShape(24.dp)).padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier.weight(1f).clip(RoundedCornerShape(20.dp)).background(if (selectedType == "EXPENSE") Color.White else Color.Transparent).clickable { onTypeSelected("EXPENSE") }.padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) { Text("Expense", color = if (selectedType == "EXPENSE") Color.Black else Color.Gray, fontWeight = if (selectedType == "EXPENSE") FontWeight.SemiBold else FontWeight.Medium) }

        Box(
            modifier = Modifier.weight(1f).clip(RoundedCornerShape(20.dp)).background(if (selectedType == "INCOME") Color.White else Color.Transparent).clickable { onTypeSelected("INCOME") }.padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) { Text("Income", color = if (selectedType == "INCOME") Color.Black else Color.Gray, fontWeight = if (selectedType == "INCOME") FontWeight.SemiBold else FontWeight.Medium) }
    }
}