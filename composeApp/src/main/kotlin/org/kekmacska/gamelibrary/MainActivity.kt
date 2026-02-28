package org.kekmacska.gamelibrary

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.kekmacska.gamelibrary.preferences.NotLoggedInSelectedFlow
import org.kekmacska.gamelibrary.preferences.saveNotLoggedIn
import org.kekmacska.gamelibrary.screens.GameDetails
import org.kekmacska.gamelibrary.screens.LoginScreen
import org.kekmacska.gamelibrary.screens.MainScreen
import org.kekmacska.gamelibrary.screens.RegisterScreen
import org.kekmacska.gamelibrary.themes.Theme
import org.kekmacska.gamelibrary.viewModels.MainViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedGetBackStackEntry")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val notLoggedIn by context.NotLoggedInSelectedFlow.collectAsState(initial = false)
            val startDestination = if (notLoggedIn) "main" else "login"
            val navController = rememberNavController()
            val activity = LocalActivity.current
            // In your Application class or MainActivity

            Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {

                        // LOGIN SCREEN
                        composable(
                            route = "login",
                            enterTransition = {
                                slideIntoContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(300)
                                ) + fadeIn()
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(300)
                                ) + fadeOut()
                            },
                            popEnterTransition = {
                                slideIntoContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Right,
                                    animationSpec = tween(300)
                                ) + fadeIn()
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Right,
                                    animationSpec = tween(300)
                                ) + fadeOut()
                            }
                        ) {
                            Scaffold { padding ->
                                LoginScreen(
                                    paddingValues = padding,
                                    onRegisterClick = { navController.navigate("register") },
                                    onNotNowClick = {
                                        scope.launch { context.saveNotLoggedIn() }
                                        navController.navigate("main")
                                    }
                                )
                            }
                        }

                        // REGISTER SCREEN
                        composable(
                            route = "register",
                            enterTransition = {
                                slideIntoContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(300)
                                ) + fadeIn()
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(300)
                                ) + fadeOut()
                            },
                            popEnterTransition = {
                                slideIntoContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Right,
                                    animationSpec = tween(300)
                                ) + fadeIn()
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Right,
                                    animationSpec = tween(300)
                                ) + fadeOut()
                            }
                        ) {
                            Scaffold {
                                RegisterScreen(
                                    onBackClick = { navController.popBackStack() }
                                )
                            }
                        }

                        //MAIN SCREEN
                        composable(
                            route = "main",
                            enterTransition = {
                                scaleIn(
                                    initialScale = .85f,
                                    animationSpec = tween(300)
                                ) + fadeIn(animationSpec = tween(300))
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(200))
                            }
                        ) {
                            Scaffold {
                                MainScreen(
                                    navController = navController,
                                    onBackClick = { activity?.finish() },
                                )
                            }
                        }

                        //GAME DETAILS SCREEN
                        composable(
                            route = "details",
                            enterTransition = {
                                scaleIn(
                                    initialScale = .85f,
                                    animationSpec = tween(300)
                                ) + fadeIn(animationSpec = tween(200))
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(200))
                            }
                        ) {
                            val mainEntry = navController.getBackStackEntry("main")
                            val viewModel: MainViewModel =
                                androidx.lifecycle.viewmodel.compose.viewModel(mainEntry)
                            val game = viewModel.selectedGame.collectAsState().value
                            if (game != null) {
                                Scaffold {
                                    GameDetails(game)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}