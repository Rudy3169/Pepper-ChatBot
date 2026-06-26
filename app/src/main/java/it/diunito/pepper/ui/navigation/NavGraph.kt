package it.diunito.pepper.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import it.diunito.pepper.ui.screens.EngageScreen
import it.diunito.pepper.ui.screens.MainScreen
import it.diunito.pepper.ui.screens.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Menu   : Screen("menu")
    object Chat   : Screen("chat")
    object Gate   : Screen("gate")
}

@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Menu.route) {
            MainScreen(
                onGoEngage = {
                    navController.navigate(Screen.Chat.route)
                }
            )
        }

        composable(Screen.Chat.route) {
            GuardedBack(navController) {
                EngageScreen()
            }
        }

        composable(
            route = Screen.Gate.route + "/{target}",
            arguments = listOf(navArgument("target") { type = NavType.StringType })
        ) { backStackEntry ->
            val target = backStackEntry.arguments?.getString("target") ?: Screen.Menu.route
            PasswordGate(
                onSuccess = {
                    navController.navigate(target) {
                        popUpTo(Screen.Gate.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}
