package org.kekmacska.gamelibrary

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.kekmacska.gamelibrary.components.SideBarLeft
import org.kekmacska.gamelibrary.preferences.isLoggedInFlow
import org.kekmacska.gamelibrary.themes.Theme
import org.kekmacska.gamelibrary.viewModels.AuthViewModel
import org.kekmacska.gamelibrary.viewModels.MainViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedGetBackStackEntry")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val context=LocalContext.current
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val authViewModel: AuthViewModel=viewModel()
            val mainViewModel: MainViewModel = viewModel()
            val isLoggedIn by context.isLoggedInFlow.collectAsState(initial = false)
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
                        ApplicationNavHost(
                            navController=navController,
                            startDestination=if (isLoggedIn) "main" else "login",
                            authViewModel=authViewModel,
                            context=this
                        )
                    }
                }
            }
        }
    }
}