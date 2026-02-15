package com.example.expensex.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.expensex.model.Routes
import com.example.expensex.screens.LoginScreen
import com.example.expensex.screens.SplashScreen
import com.example.expensex.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.expensex.screens.RegisterScreen
import com.example.expensex.viewmodel.HomeScreenViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) { SplashScreen(navController) }

        composable(Routes.LOGIN) {
            val vm: AuthViewModel = hiltViewModel()
            LoginScreen(
                vm = vm,
                navController = navController,
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            val vm: AuthViewModel = hiltViewModel()
            RegisterScreen(
                vm,
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable(
            route = "${Routes.HOME}/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType })
        ) { backStack ->
            val uid = backStack.arguments?.getString("uid")!!
            val homeVm: HomeScreenViewModel =
                hiltViewModel(backStack)

            MainScaffold(uid, homeVm)
        }
    }
}