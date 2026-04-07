package org.kekmacska.gamelibrary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.kekmacska.gamelibrary.preferences.TokenStorage
import org.kekmacska.gamelibrary.preferences.isLoggedInFlow
import org.kekmacska.gamelibrary.preferences.saveNotLoggedIn

@Composable
fun SideBarLeft(
    navController: NavController,
    drawerState: DrawerState,
    showDrawer: Boolean,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isLoggedIn by context.isLoggedInFlow.collectAsState(initial = false)

    Box {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = showDrawer,
            drawerContent = {
                ModalDrawerSheet {
                    if (showDrawer) {
                        NavigationDrawerItem(
                            label = { Text(if (isLoggedIn) "Log out" else "Login") },
                            selected = false,
                            onClick = {
                                if (isLoggedIn) {
                                    coroutineScope.launch {
                                        TokenStorage(context).clearToken()
                                        context.saveNotLoggedIn()
                                        drawerState.close()
                                    }
                                } else {
                                    coroutineScope.launch { drawerState.close() }
                                    navController.navigate("login") {
                                        popUpTo("login") {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        )

                        if (!isLoggedIn) {
                            NavigationDrawerItem(
                                label = { Text("Register") },
                                selected = false,
                                onClick = {
                                    coroutineScope.launch { drawerState.close() }
                                    navController.navigate("register") {
                                        popUpTo("register") {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) {
            content()
        }

        if (showDrawer && drawerState.isClosed) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight(.2f)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(50)
                    )
                    .align(Alignment.CenterStart)
                    .offset(y = (-65).dp)
                    .zIndex(50f)
            )
        }
    }
}