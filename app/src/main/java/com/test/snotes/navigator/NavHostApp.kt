package com.test.snotes.navigator

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.test.snotes.ui.AddEditNoteScreen
import com.test.snotes.ui.NotesScreen
import com.test.snotes.ui.SignInScreen
import com.test.snotes.ui.SplashScreen


@Composable
fun NavHostApp() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Router.SplashScreen) {
        ->

        composable<Router.SplashScreen> {
            SplashScreen(navController)
        }

        composable<Router.SignInScreen> {
            SignInScreen(navController)
        }

        composable<Router.NotesScreen> {
            NotesScreen(navController)
        }

        composable<Router.AddEditNoteScreen> { backStackEntry ->
            val screen = backStackEntry.toRoute<Router.AddEditNoteScreen>()

            AddEditNoteScreen(
                navController,
                screen.id,
                screen.title,
                screen.description,
                screen.reminderTime,
                screen.userId,
                screen.isAlarmOn,
            )
        }

    }


}