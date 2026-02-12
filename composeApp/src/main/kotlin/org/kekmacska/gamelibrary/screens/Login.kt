package org.kekmacska.gamelibrary.screens

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
import androidx.compose.ui.unit.dp
import org.kekmacska.gamelibrary.themes.AuthScreenLayout
import org.kekmacska.gamelibrary.themes.AuthTextField

@Composable
fun LoginScreen(
    paddingValues: PaddingValues,
    onRegisterClick: () -> Unit = {}
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    AuthScreenLayout(title = "Login", paddingValues = paddingValues) {

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

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                usernameError = if (username.isBlank()) "Required" else ""
                passwordError = if (password.isBlank()) "Required" else ""
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
    }
}