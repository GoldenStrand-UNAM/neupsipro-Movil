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
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.neupsipromovil.presentation.screens.profile.ProfileScreen

// Rutas
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Profile: Screen("profile/{userId}") {
        fun createRoute(userId: String) = "profile/$userId"
    }
}

@Composable
fun NeuNavGraph(
    modifier: Modifier = Modifier,
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
        startDestination = startDestination,
        modifier         = modifier
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

        composable(
            route = Screen.Profile.route,
            arguments = listOf(navArgument("userId") {
                type = NavType.StringType
                defaultValue = "u-016"
            })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: "u-016"
            ProfileScreen(userId = userId, navController = navController)
        }
    }
}