package org.kekmacska.gamelibrary

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.launch
import org.kekmacska.gamelibrary.preferences.saveNotLoggedIn
import org.kekmacska.gamelibrary.screens.CollectiblesScreen
import org.kekmacska.gamelibrary.screens.GameDetails
import org.kekmacska.gamelibrary.screens.LoginScreen
import org.kekmacska.gamelibrary.screens.MainScreen
import org.kekmacska.gamelibrary.screens.RegisterScreen
import org.kekmacska.gamelibrary.viewModels.AuthViewModel
import org.kekmacska.gamelibrary.viewModels.MainViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedGetBackStackEntry")
@Composable
fun ApplicationNavHost(
    navController: NavHostController,
    startDestination: String,
    authViewModel: AuthViewModel,
    context: Context
){
    val scope = rememberCoroutineScope()


    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // LOGIN SCREEN
        composable(route = "login") {
            Scaffold { padding ->
                LoginScreen(
                    paddingValues = padding,
                    navController = navController,
                    authViewModel = authViewModel,
                    context = context,
                    onRegisterClick = {
                        navController.navigate("register") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onNotNowClick = {
                        scope.launch { context.saveNotLoggedIn() }
                        navController.navigate("main")
                    }
                )
            }
        }

        // REGISTER SCREEN
        composable(route = "register") {
            Scaffold {
                RegisterScreen(
                    onBackClick = {
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    },
                    navController = navController,
                    authViewModel = authViewModel,
                    context = context
                )
            }
        }

        // MAIN SCREEN
        composable(route = "main") {
            Scaffold {
                MainScreen(navController = navController)
            }
        }

        // GAME DETAILS SCREEN
        composable(route = "details") {
            val mainEntry = remember(navController, "main") {
                navController.getBackStackEntry("main")
            }
            val viewModel: MainViewModel = viewModel(mainEntry)
            val game = viewModel.selectedGame.collectAsState().value
            if (game != null) {
                Scaffold {
                    GameDetails(game, navController = navController)
                }
            }
        }

        // COLLECTIBLES SCREEN
        composable(route="collectibles/{gameId}") { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId")!!.toInt()
            Scaffold {
                CollectiblesScreen(gameId = gameId)
            }
        }
    }
}