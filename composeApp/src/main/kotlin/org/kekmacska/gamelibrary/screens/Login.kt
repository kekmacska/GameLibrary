package org.kekmacska.gamelibrary.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.kekmacska.gamelibrary.themes.AuthScreenLayout
import org.kekmacska.gamelibrary.themes.AuthTextField
import org.kekmacska.gamelibrary.viewModels.AuthViewModel

@Composable
fun LoginScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    authViewModel: AuthViewModel,
    context: Context = LocalContext.current,
    onRegisterClick: () -> Unit = {},
    onNotNowClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

    AuthScreenLayout(title = "Login", paddingValues = paddingValues) {

        AuthTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            leadingIcon = { Icon(Icons.Rounded.AccountCircle, null) },
            error = emailError
        )

        Spacer(Modifier.height(16.dp))

        AuthTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            leadingIcon = { Icon(Icons.Rounded.Lock, null) },
            isPassword = true,
            error = passwordError
        )

        Spacer(Modifier.height(24.dp))

        Button(
            //validation
            onClick = {
                emailError = when {
                    email.isBlank() -> "Email is required"
                    !email.matches(emailRegex) -> "Invalid email format"
                    else -> ""
                }
                passwordError = if (password.isBlank()) "Required" else ""
                if (emailError.isNotEmpty() || passwordError.isNotEmpty()) return@Button

                //login
                authViewModel.login(
                    context = context, email = email, password = password
                ) {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(Modifier.height(32.dp))

        Row {
            Text("Don't have an account?")
            Text(
                " Register",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onRegisterClick() }
            )
        }

        Spacer(Modifier.height(40.dp))

        Row {
            Text(
                "Not now",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onNotNowClick() }
            )
        }
    }
}