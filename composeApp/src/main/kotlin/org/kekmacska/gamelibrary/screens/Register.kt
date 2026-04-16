package org.kekmacska.gamelibrary.screens

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
fun RegisterScreen(
    onBackClick: () -> Unit = {},
    navController: NavController,
    authViewModel: AuthViewModel,
    context: Context = LocalContext.current
) {
    var name by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var confirmError by remember { mutableStateOf("") }

    val biometricAction by authViewModel.biometricEvent

    BiometricEffect(
        biometricAction=biometricAction,
        onConsumed = {authViewModel.runPending()}
    )

    AuthScreenLayout(title = "Register") {

        AuthTextField(
            value = name,
            onValueChange = { name = it },
            label = "Name",
            leadingIcon = { Icon(Icons.Rounded.AccountCircle, "name") },
            error = nameError
        )

        Spacer(Modifier.height(16.dp))

        AuthTextField(
            value = email,
            onValueChange = {email=it},
            label = "Email",
            leadingIcon = {Icon(Icons.Rounded.Email, contentDescription = "email")},
            error=emailError
        )

        Spacer(Modifier.height(16.dp))

        AuthTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            leadingIcon = { Icon(Icons.Rounded.Lock, "password") },
            isPassword = true,
            error = passwordError
        )

        Spacer(Modifier.height(16.dp))

        AuthTextField(
            value = confirm,
            onValueChange = { confirm = it },
            label = "Confirm Password",
            leadingIcon = { Icon(Icons.Rounded.Lock, null) },
            isPassword = true,
            error = confirmError
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                val nameErr = if (name.isBlank()) "Required" else ""
                val emailErr = when {
                    email.isBlank() -> "Email is required"
                    !email.matches(emailRegex) -> "Invalid email format"
                    else -> ""
                }
                val passwordErr = when {
                    password.isBlank() -> "Password is required"
                    !password.matches(passwordRegex)->"Password format is invalid"
                    else -> ""
                }
                val confirmErr = when {
                    confirm.isBlank() -> "Password confirmation is required"
                    password.trim() != confirm.trim() -> "Passwords don't match"
                    else -> ""
                }

                nameError = nameErr
                emailError = emailErr
                passwordError = passwordErr
                confirmError = confirmErr

                //register
                if(nameErr.isEmpty()&&emailErr.isEmpty()&&passwordErr.isEmpty()&&confirmErr.isEmpty()){
                    authViewModel.register(
                        context = context,
                        name = name,
                        email = email,
                        password = password,
                        onBackendErrors = { backend ->
                            nameError = backend.name?.firstOrNull() ?: ""
                            emailError = backend.email?.firstOrNull() ?: ""
                            passwordError = backend.password?.firstOrNull() ?: ""
                        },
                        onSuccess = {
                            navController.navigate("login"){
                                popUpTo("register"){inclusive=true}
                            }
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Account")
        }

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = onBackClick) {
            Text("Back to Login")
        }
    }
}