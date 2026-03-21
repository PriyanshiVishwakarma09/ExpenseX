package com.example.expensex.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensex.db.TransactionEntity
import com.example.expensex.viewmodel.HomeScreenViewModel
import java.text.SimpleDateFormat
import androidx.compose.ui.text.TextStyle
import java.util.Date
import java.util.Locale

val TopBgColor = Color(0xFF3B978F)
val CardBgColor = Color(0xFF2A7C76)
val TextGreen = Color(0xFF24A869)
val TextRed = Color(0xFFF95B51)
val BgLight = Color(0xFFF8F9FA)

@Composable
fun HomeScreen(vm: HomeScreenViewModel) {
    val balance by vm.balance.collectAsState()
    val income by vm.income.collectAsState()
    val expense by vm.expense.collectAsState()
    val recent by vm.recent.collectAsState()
    val username by vm.username.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(TopBgColor)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.verticalScroll(rememberScrollState())
                .padding(top = 40.dp)
        ) {
            TopHeaderRow(username)
            Spacer(modifier = Modifier.height(24.dp))
            BalanceCard(balance, income, expense)
            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                item {
                    SectionHeader("Transactions History", "See all")
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(recent) { tx ->
                    TransactionRow(tx)
                }
            }
        }
    }
}

@Composable
fun TopHeaderRow(username : String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = username,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color.White
            )
            // Orange dot indicator could go here in a tiny Box
        }
    }
}

@Composable
fun BalanceCard(balance: Double, income: Double, expense: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBgColor),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Top Row (Total Balance Label)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Total Balance",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = null, tint = Color.White)
                }
                Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Balance Amount (Now Auto-Resizes)
            Column(modifier = Modifier.fillMaxWidth()){
                AutoResizedText(
                    text = "$ ${"%,.2f".format(balance)}",
                    style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Bottom Row (Income & Expenses)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Income Section
                Row(
                    modifier = Modifier.weight(1f), // Locks to 50% width
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) { // Prevents text from pushing past its bounds
                        Text("Income", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                        AutoResizedText(
                            text = "$ ${"%,.2f".format(income)}",
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp)) // Buffer space in the middle

                // Expenses Section
                Row(
                    modifier = Modifier.weight(1f), // Locks to 50% width
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) { // Prevents text from pushing past its bounds
                        Text("Expenses", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                        AutoResizedText(
                            text = "$ ${"%,.2f".format(expense)}",
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun SectionHeader(title: String, actionText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Color.Black
        )
        Text(
            text = actionText,
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}



@Composable
fun TransactionRow(tx: TransactionEntity){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = TopBgColor
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = tx.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(tx.date)),
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }

        Text(
            text = (if (tx.type == "INCOME") "+ $ " else "- $ ") + "${"%,.2f".format(tx.amount)}",
            color = if (tx.type == "INCOME") TextGreen else TextRed,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}


@Composable
fun AutoResizedText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified
) {
    var resizedTextStyle by remember { mutableStateOf(style) }
    var shouldDraw by remember { mutableStateOf(false) }

    Text(
        text = text,
        color = if (color != Color.Unspecified) color else style.color, // And this line
        modifier = modifier.drawWithContent {
            if (shouldDraw) {
                drawContent()
            }
        },
        softWrap = false,
        style = resizedTextStyle,
        onTextLayout = { result ->
            if (result.didOverflowWidth) {
                resizedTextStyle = resizedTextStyle.copy(
                    fontSize = resizedTextStyle.fontSize * 0.95f
                )
            } else {
                shouldDraw = true
            }
        }
    )
}
