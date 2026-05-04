package com.example.neupsipromovil.presentation.navegation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.neupsipromovil.presentation.screens.login.LoginScreen
import com.example.neupsipromovil.presentation.screens.login.LoginViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.currentBackStackEntryAsState

// Rutas
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
}


@Composable
fun NeuNavGraph(
    navController: NavHostController = rememberNavController(),
    // Shared w LoginScreen so both observe the sessionState flow.
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()

    // Read once on initial composition
    val startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // token expiry detected anywhere in the app.
    LaunchedEffect(isLoggedIn) {
        when {
            !isLoggedIn && currentRoute != Screen.Login.route -> {
                // Redirect to login screen
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
            isLoggedIn && currentRoute == Screen.Login.route -> {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    NavHost(
        navController    = navController,
        startDestination = startDestination
    ) {

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                viewModel = loginViewModel
            )
        }

        composable(Screen.Home.route) {
            // HomeScreen()

        }
    }
}