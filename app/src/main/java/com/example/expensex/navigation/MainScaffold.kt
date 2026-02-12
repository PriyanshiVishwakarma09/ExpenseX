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

@Composable
fun MainScaffold(uid: String) {
    val navController = rememberNavController()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("${Routes.ADD}/$uid")
                }
            ) {
                Icon(Icons.Default.Add, null)
            }
        },
        bottomBar = {
            BottomBar(navController, uid)
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "${Routes.HOME}/$uid",
            modifier = Modifier.padding(padding)
        ) {

            composable("${Routes.HOME}/{uid}") { HomeRoot() }
            composable("${Routes.WALLET}/{uid}") { WalletRoot() }
            composable("${Routes.STATS}/{uid}") { StatsRoot() }
            composable("${Routes.PROFILE}/{uid}") { ProfileRoot() }
            composable("${Routes.ADD}/{uid}") { AddRoot() }
        }
    }
}

@Composable fun HomeRoot() { HomeScreen(hiltViewModel()) }
@Composable fun WalletRoot() { AddTransactionScreen(hiltViewModel()) }
@Composable fun StatsRoot() { StatsScreen() }
@Composable fun ProfileRoot() { ProfileScreen() }
@Composable fun AddRoot() { AddTransactionScreen(hiltViewModel()) }


@Composable
fun BottomBar(nav: NavController, uid: String) {
    val items = listOf(Routes.HOME, Routes.WALLET, Routes.STATS, Routes.PROFILE)

    NavigationBar {
        items.forEach {
            NavigationBarItem(
                selected = false,
                onClick = { nav.navigate("$it/$uid") },
                icon = { Icon(Icons.Default.Home, null) },
                label = { Text(it) }
            )
        }
    }
}
