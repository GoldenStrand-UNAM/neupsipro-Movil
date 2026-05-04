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

// ── Rutas de navegación ───────────────────────────────────────────────────────
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home  : Screen("home")
}

@Composable
fun NeuNavGraph(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()
    val startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route

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
            // TODO: reemplazar con HomeScreen real
        }
    }
}