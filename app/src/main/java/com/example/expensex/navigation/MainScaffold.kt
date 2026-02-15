package com.example.expensex.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensex.model.Routes
import com.example.expensex.screens.AddTransactionScreen
import com.example.expensex.screens.HomeScreen
import com.example.expensex.screens.ProfileScreen
import com.example.expensex.screens.StatsScreen
import com.example.expensex.viewmodel.HomeScreenViewModel
import com.example.expensex.viewmodel.WalletViewModel

@Composable
fun MainScaffold(
    uid: String,
    homeVm: HomeScreenViewModel
) {
    val navController = rememberNavController()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.ADD) }
            ) { Icon(Icons.Default.Add, null) }
        },
        bottomBar = { BottomBar(navController, uid) }
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
fun BottomBar(nav: NavController, uid: String) {

    val items = listOf(Routes.HOME, Routes.WALLET, Routes.STATS, Routes.PROFILE)
    val currentRoute =
        nav.currentBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { route ->
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = {
                    nav.navigate(route) {
                        popUpTo(Routes.HOME)
                        launchSingleTop = true
                    }
                },
                icon = { Icon(Icons.Default.Home, null) },
                label = { Text(route) }
            )
        }
    }
}