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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.kekmacska.gamelibrary.components.BiometricEffect
import org.kekmacska.gamelibrary.providers.Validators.emailRegex
import org.kekmacska.gamelibrary.providers.Validators.passwordRegex
import org.kekmacska.gamelibrary.components.AuthScreenLayout
import org.kekmacska.gamelibrary.components.AuthTextField
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

    val loginCompleted by authViewModel.loginCompleted
    val biometricAction by authViewModel.biometricEvent

    LaunchedEffect(loginCompleted) {
        if(loginCompleted){
            navController.navigate("main"){
                popUpTo("login"){inclusive=true}
            }
            authViewModel.resetLoginCompleted()
        }
    }

    BiometricEffect(
        biometricAction=biometricAction,
        onConsumed = {
            authViewModel.runPending()
        }
    )

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
                passwordError = when {
                    password.isBlank() -> "Password is required"
                    !password.matches(passwordRegex)->"Password must be at least 8 characters and contain at least 1 letter"
                    else -> ""
                }
                if (emailError.isNotEmpty() || passwordError.isNotEmpty()) return@Button

                //login
                authViewModel.login(context = context, email = email, password = password)
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