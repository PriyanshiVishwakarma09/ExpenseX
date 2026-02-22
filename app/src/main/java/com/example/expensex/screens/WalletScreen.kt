package com.example.expensex.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensex.db.CategoryEntity
import com.example.expensex.viewmodel.WalletViewModel

val TealColor = Color(0xFF3B978F)
val BgLightColor = Color(0xFFF8F9FA)
val KeypadBgColor = Color(0xFFE5E7EB)

@Composable
fun AddTransactionScreen(vm: WalletViewModel) {
    var type by remember { mutableStateOf("EXPENSE") }
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<CategoryEntity?>(null) }

    var showKeypad by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        vm.ensureDefaultCategories()
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

    BackHandler(enabled = showKeypad) {
        showKeypad = false
    }

    Scaffold(
        containerColor = BgLightColor,
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = TealColor,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Box(
                modifier = Modifier.weight(1f)
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
                            TransactionTypeToggle(selectedType = type, onTypeSelected = { type = it })
                            Spacer(modifier = Modifier.height(24.dp))

                            FormLabel(text = "Name")
                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    // Hide our custom keypad if they click the Name field
                                    .onFocusChanged { if (it.isFocused) showKeypad = false },
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = TealColor,
                                    unfocusedBorderColor = Color(0xFFE0E0E0)
                                )
                            )

                            Spacer(Modifier.height(20.dp))

                            // CUSTOM AMOUNT FIELD
                            FormLabel(text = "Amount")
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, if (amount.isEmpty()) Color(0xFFE0E0E0) else TealColor, RoundedCornerShape(12.dp))
                                    .clickable {
                                        // 4. Show custom keypad & hide system keyboard
                                        showKeypad = true
                                        focusManager.clearFocus()
                                    }
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("$ ", color = TealColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text(
                                        text = amount.ifEmpty { "0.00" },
                                        color = if (amount.isEmpty()) Color.Gray else Color.Black,
                                        fontSize = 16.sp,
                                        fontWeight = if (amount.isNotEmpty()) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                }

                                if (amount.isNotEmpty()) {
                                    Text(
                                        text = "Clear",
                                        color = TealColor,
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .clickable { amount = "" }
                                            .padding(8.dp)
                                    )
                                }
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
                                            showKeypad = false // Hide keypad when picking a category
                                            focusManager.clearFocus()
                                        },
                                    readOnly = true,
                                    enabled = false,
                                    shape = RoundedCornerShape(12.dp),
                                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray) },
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

                            // Save Button
                            Button(
                                onClick = {
                                    showKeypad = false
                                    focusManager.clearFocus()

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


            AnimatedVisibility(visible = showKeypad) {
                CustomNumPad(
                    onNumberClick = { num ->
                        amount += num
                    },
                    onDeleteClick = {
                        if (amount.isNotEmpty()) {
                            amount = amount.dropLast(1)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CustomNumPad(
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit
) {
    val padPadding = 6.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(KeypadBgColor)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(padPadding)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(padPadding)) {
            NumPadKey("1", "", { onNumberClick("1") }, Modifier.weight(1f))
            NumPadKey("2", "ABC", { onNumberClick("2") }, Modifier.weight(1f))
            NumPadKey("3", "DEF", { onNumberClick("3") }, Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(padPadding)) {
            NumPadKey("4", "GHI", { onNumberClick("4") }, Modifier.weight(1f))
            NumPadKey("5", "JKL", { onNumberClick("5") }, Modifier.weight(1f))
            NumPadKey("6", "MNO", { onNumberClick("6") }, Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(padPadding)) {
            NumPadKey("7", "PQRS", { onNumberClick("7") }, Modifier.weight(1f))
            NumPadKey("8", "TUV", { onNumberClick("8") }, Modifier.weight(1f))
            NumPadKey("9", "WXYZ", { onNumberClick("9") }, Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(padPadding), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1f).height(50.dp), contentAlignment = Alignment.Center) {
                Text("+ * #", fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.SemiBold)
            }
            NumPadKey("0", "", { onNumberClick("0") }, Modifier.weight(1f))
            Box(
                modifier = Modifier.weight(1f).height(50.dp).clip(RoundedCornerShape(8.dp)).clickable { onDeleteClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Backspace, contentDescription = "Delete", tint = Color.Black, modifier = Modifier.size(22.dp))
            }
        }
    }
}

@Composable
fun NumPadKey(number: String, letters: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(48.dp).clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = number, fontSize = 22.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            if (letters.isNotEmpty()) {
                Text(text = letters, fontSize = 8.sp, color = Color.Black, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }
        }
    }
}

@Composable
fun FormLabel(text: String) {
    Text(text = text.uppercase(), fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium, letterSpacing = 1.sp)
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