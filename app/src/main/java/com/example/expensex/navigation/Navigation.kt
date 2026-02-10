package com.example.expensex.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensex.screens.HomeScreen
import com.example.expensex.screens.LoginScreen
import com.example.expensex.screens.RegisterScreen
import com.example.expensex.viewmodel.AuthViewModel


@Composable
fun Navigation() {
    val navController = rememberNavController()
     val vm : AuthViewModel = hiltViewModel()
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                vm,
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = { navController.navigate("home") }
            )
        }
        composable("register") {
            RegisterScreen(
                vm,
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable("home") {
            HomeScreen()
        }
    }
}