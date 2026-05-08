package com.example.neupsipromovil.presentation.navegation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.neupsipromovil.presentation.screens.profile.ProfileScreen

sealed class Screen(
    val route: String,
) {
    object Profile: Screen("profile/{userId}") {
        fun createRoute(userId: String) = "profile/$userId"
    }
}

@Composable
fun NeupsiproNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        // Id hardcodeaado en lo que se integra con login para dar el id bien con el boton
        startDestination = Screen.Profile.route,
        modifier = modifier
    ) {
        composable(
            route = Screen.Profile.route,
            arguments = listOf(navArgument("userId") {
                type = NavType.StringType
                defaultValue = "u-016" })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: "u-016"
            println("DEBUG_NAV: El user id extraido del argumento es: '$userId'")
            ProfileScreen(userId = userId, navController = navController)
        }
    }
}