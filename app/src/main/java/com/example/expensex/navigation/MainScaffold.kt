package com.example.expensex.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.*

import com.example.expensex.model.Routes
import com.example.expensex.screens.*
import com.example.expensex.ui.viewmodel.ExpenseChartViewModel
import com.example.expensex.viewmodel.HomeScreenViewModel
import com.example.expensex.viewmodel.WalletViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

val TealColor = Color(0xFF3B978F)

@Composable
fun MainScaffold(
    uid: String,
    homeVm: HomeScreenViewModel,
    navController: NavController
){
    val innerNavController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false
        )
    }

    Scaffold(
        bottomBar = { BottomBar(innerNavController) },
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

        NavHost(
            navController = innerNavController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding)
        ) {

            composable(Routes.HOME) {
                HomeScreen(homeVm)
            }

            composable(Routes.WALLET) {
                val vm: WalletViewModel = hiltViewModel()
                AddTransactionScreen(
                    vm = vm,
                    snackbarHostState = snackbarHostState
                )
            }

            composable(Routes.STATS) {
                val viewModel: ExpenseChartViewModel = hiltViewModel()
                ExpenseTrackerScreen(viewModel)
            }

            composable(Routes.PROFILE) {
                    val vm: WalletViewModel = hiltViewModel()
                    ProfileScreen(homeVm , vm , navController = navController)

            }

            composable(Routes.ADD) {
                AddTransactionScreen(
                    vm = hiltViewModel(),
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {

        BottomAppBar(
            containerColor = Color.White,
            tonalElevation = 0.dp,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .clip(RoundedCornerShape(24.dp))
                .shadow(12.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME)
                        launchSingleTop = true
                    }
                }) {
                    Icon(
                        imageVector = if (currentRoute == Routes.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Home",
                        tint = if (currentRoute == Routes.HOME) TealColor else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(onClick = {
                    navController.navigate(Routes.STATS) {
                        popUpTo(Routes.STATS)
                        launchSingleTop = true
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Assessment,
                        contentDescription = "Stats",
                        tint = if (currentRoute == Routes.STATS) TealColor else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = {
                    navController.navigate(Routes.WALLET) {
                        popUpTo(Routes.WALLET)
                        launchSingleTop = true
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = "Wallet",
                        tint = if (currentRoute == Routes.WALLET) TealColor else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(onClick = {
                    navController.navigate(Routes.PROFILE) {
                        popUpTo(Routes.PROFILE)
                        launchSingleTop = true
                    }
                }) {
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
            onClick = {
                navController.navigate(Routes.ADD)
            },
            shape = CircleShape,
            containerColor = TealColor,
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(12.dp),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-36).dp)
                .size(64.dp)
        ){
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
