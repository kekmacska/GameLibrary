package org.kekmacska.gamelibrary.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
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
import androidx.compose.ui.unit.dp
import org.kekmacska.gamelibrary.themes.AuthScreenLayout
import org.kekmacska.gamelibrary.themes.AuthTextField

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit = {}
) {
    var username by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var confirmError by remember { mutableStateOf("") }

    AuthScreenLayout(title = "Register") {

        AuthTextField(
            value = username,
            onValueChange = { username = it },
            label = "Username",
            leadingIcon = { Icon(Icons.Rounded.AccountCircle, null) },
            error = usernameError
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
                usernameError = if (username.isBlank()) "Required" else ""
                passwordError = when {
                    password.isBlank() -> "Required"
                    else -> ""
                }
                confirmError = when {
                    confirm.isBlank() -> "Required"
                    password != confirm -> "Passwords don't match"
                    else -> ""
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