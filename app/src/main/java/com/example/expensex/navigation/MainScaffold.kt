package com.example.expensex.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensex.model.Routes
import com.example.expensex.screens.AddTransactionScreen
import com.example.expensex.screens.HomeScreen
import com.example.expensex.screens.ProfileScreen
import com.example.expensex.screens.StatsScreen
import com.example.expensex.viewmodel.HomeScreenViewModel
import com.example.expensex.viewmodel.WalletViewModel

val TealColor = Color(0xFF3B978F)

@Composable
fun MainScaffold(
    uid: String,
    homeVm: HomeScreenViewModel
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(homeVm)
            }
            composable(Routes.WALLET) {
                val vm: WalletViewModel = hiltViewModel()
                AddTransactionScreen(vm)
            }
            composable(Routes.STATS) { StatsScreen() }
            composable(Routes.PROFILE) { ProfileScreen() }
            composable(Routes.ADD) {
                AddTransactionScreen(hiltViewModel())
            }
        }
    }
}
@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Use a Box to layer the FAB right on top of the BottomAppBar
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {

        // 1. The White Bottom Navigation Background
        BottomAppBar(
            containerColor = Color.White,
            contentColor = Color.Gray,
            tonalElevation = 8.dp,
            contentPadding = PaddingValues(horizontal = 24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigate(Routes.HOME) }) {
                    Icon(
                        imageVector = if (currentRoute == Routes.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Home",
                        tint = if (currentRoute == Routes.HOME) TealColor else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(onClick = { navController.navigate(Routes.STATS) }) {
                    Icon(
                        imageVector = Icons.Default.Assessment,
                        contentDescription = "Stats",
                        tint = if (currentRoute == Routes.STATS) TealColor else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Gap in the middle for the FAB
                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = { navController.navigate(Routes.WALLET) }) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = "Wallet",
                        tint = if (currentRoute == Routes.WALLET) TealColor else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(onClick = { navController.navigate(Routes.PROFILE) }) {
                    Icon(
                        imageVector = if (currentRoute == Routes.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                        contentDescription = "Profile",
                        tint = if (currentRoute == Routes.PROFILE) TealColor else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(Routes.ADD) },
            shape = CircleShape,
            containerColor = TealColor,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-32).dp)
                .size(64.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}