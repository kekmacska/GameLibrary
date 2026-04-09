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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.kekmacska.gamelibrary.components.SideBarLeft
import org.kekmacska.gamelibrary.preferences.isLoggedInFlow
import org.kekmacska.gamelibrary.preferences.saveNotLoggedIn
import org.kekmacska.gamelibrary.screens.CollectiblesScreen
import org.kekmacska.gamelibrary.screens.GameDetails
import org.kekmacska.gamelibrary.screens.LoginScreen
import org.kekmacska.gamelibrary.screens.MainScreen
import org.kekmacska.gamelibrary.screens.RegisterScreen
import org.kekmacska.gamelibrary.themes.Theme
import org.kekmacska.gamelibrary.viewModels.AuthViewModel
import org.kekmacska.gamelibrary.viewModels.MainViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedGetBackStackEntry")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val isLoggedIn by context.isLoggedInFlow.collectAsState(initial = false)
            val startDestination = if (isLoggedIn) "main" else "login"
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val mainViewModel: MainViewModel = viewModel()
            val authViewModel: AuthViewModel = viewModel()
            val error by mainViewModel.error.collectAsState(initial = null)
            val currentRoute = navBackStackEntry?.destination?.route
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val showDrawer = currentRoute !in listOf("login", "register") && error == null
            LocalActivity.current

            Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SideBarLeft(
                        navController = navController,
                        drawerState = drawerState,
                        showDrawer = showDrawer
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
                                        navController = navController,
                                        authViewModel = authViewModel,
                                        context = context,
                                        onRegisterClick = {
                                            navController.navigate("register") {
                                                popUpTo("login") {
                                                    inclusive = true
                                                }
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
                                        onBackClick = {
                                            navController.navigate("login") {
                                                popUpTo("register") {
                                                    inclusive = true
                                                }
                                            }
                                        },
                                        navController = navController,
                                        authViewModel = authViewModel,
                                        context = context
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
                                    viewModel(mainEntry)
                                val game = viewModel.selectedGame.collectAsState().value
                                if (game != null) {
                                    Scaffold {
                                        GameDetails(
                                            game,
                                            navController=navController
                                        )
                                    }
                                }
                            }

                            //COLLECTIBLES SCREEN
                            composable(
                                route="collectibles/{gameId}",
                                enterTransition = {
                                    scaleIn(
                                        initialScale = .85f,
                                        animationSpec = tween(300)
                                    )+fadeIn(animationSpec = tween(200))
                                },
                                exitTransition = {
                                    fadeOut(animationSpec = tween(200))
                                }
                            ){backStackEntry->
                                val gameId=backStackEntry.arguments?.getString("gameId")!!.toInt()
                                Scaffold {
                                    CollectiblesScreen(gameId = gameId)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}