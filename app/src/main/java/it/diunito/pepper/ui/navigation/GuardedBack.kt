package it.diunito.pepper.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun GuardedBack(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    BackHandler {
        navController.navigate(Screen.Gate.route + "/" + Screen.Menu.route) {
            launchSingleTop = true
        }
    }
    content()
}
