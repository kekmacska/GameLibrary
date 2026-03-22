package org.kekmacska.gamelibrary.viewModels

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.launch
import org.kekmacska.gamelibrary.models.RegisterFieldErrors
import org.kekmacska.gamelibrary.models.RegisterResponse
import org.kekmacska.gamelibrary.preferences.TokenStorage
import org.kekmacska.gamelibrary.preferences.saveLoggedIn
import org.kekmacska.gamelibrary.services.login
import org.kekmacska.gamelibrary.services.register

class AuthViewModel : ViewModel() {
    var error = mutableStateOf<String?>(null)
        private set

    fun login(context: Context, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = login(email, password)
                val success = !response.token.isNullOrBlank()
                if (success) {
                    //store token in secure storage
                    val storage = TokenStorage(context)
                    storage.saveToken(response.token)

                    context.saveLoggedIn()
                    onSuccess()
                }
            } catch (e: Exception) {
                error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun register(
        context: Context,
        name: String,
        email: String,
        password: String,
        onBackendErrors: (RegisterFieldErrors) -> Unit,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                when (val response = register(name, email, password)) {
                    is RegisterResponse.Success -> {

                        onBackendErrors(RegisterFieldErrors())

                        Toast.makeText(
                            context,
                            "Registration successful, you can now log in",
                            Toast.LENGTH_LONG
                        ).show()

                        onSuccess()
                    }

                    is RegisterResponse.Error -> {
                        onBackendErrors(response.errors)
                    }
                }

            } catch (e: ClientRequestException) {
                val errorResponse = e.response.body<RegisterResponse.Error>()
                onBackendErrors(errorResponse.errors)
            }
        }
    }
}