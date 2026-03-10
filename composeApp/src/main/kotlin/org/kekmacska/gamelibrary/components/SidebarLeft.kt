package org.kekmacska.gamelibrary.components

import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.kekmacska.gamelibrary.preferences.isLoggedInFlow
import org.kekmacska.gamelibrary.preferences.saveNotLoggedIn

@Composable
fun SideBarLeft(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isLoggedIn by context.isLoggedInFlow.collectAsState(initial = false)

    ModalDrawerSheet {
        NavigationDrawerItem(
            label = { Text(if (isLoggedIn) "Log out" else "Login") },
            selected = false,
            onClick = {
                if (isLoggedIn) {
                    coroutineScope.launch { context.saveNotLoggedIn() }
                } else {
                    navController.navigate("login")
                }
            }
        )

        if (!isLoggedIn) {
            NavigationDrawerItem(
                label = { Text("Register") },
                selected = false,
                onClick = { navController.navigate("register") }
            )
        }
    }
}