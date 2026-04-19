package org.kekmacska.gamelibrary.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.launch
import org.kekmacska.gamelibrary.models.RegisterFieldErrors
import org.kekmacska.gamelibrary.models.RegisterResponse
import org.kekmacska.gamelibrary.cache.TokenStorage
import org.kekmacska.gamelibrary.preferences.saveLoggedIn
import org.kekmacska.gamelibrary.services.login
import org.kekmacska.gamelibrary.services.register

class AuthViewModel : ViewModel() {

    private val TAG = "BIOMETRIC_DEBUG"

    var error = mutableStateOf<String?>(null)
        private set

    private val _loginCompleted = mutableStateOf(false)
    val loginCompleted: State<Boolean> get() = _loginCompleted

    private val _biometricEvent = mutableStateOf(false)
    val biometricEvent: State<Boolean> get() = _biometricEvent

    private var pendingAction: (() -> Unit)? = null

    fun login(context: Context, email: String, password: String) {
        pendingAction = {
            viewModelScope.launch {
                try {
                    val response = login(email, password)

                    if (!response.token.isNullOrBlank()) {
                        TokenStorage(context).saveToken(response.token)
                        context.saveLoggedIn()

                        _loginCompleted.value = true
                        Log.d(TAG, "LOGIN SUCCESS")
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "LOGIN ERROR: ${e.message}")
                    error.value = e.message ?: "Unknown error"
                }
            }
        }

        triggerBiometric()
    }

    fun register(
        context: Context,
        name: String,
        email: String,
        password: String,
        onBackendErrors: (RegisterFieldErrors) -> Unit,
        onSuccess: () -> Unit
    ) {

        pendingAction = {
            viewModelScope.launch {
                try {
                    when (val response = register(name, email, password)) {

                        is RegisterResponse.Success -> {

                            onBackendErrors(RegisterFieldErrors())

                            Toast.makeText(
                                context,
                                "Registration successful",
                                Toast.LENGTH_LONG
                            ).show()

                            onSuccess()
                        }

                        is RegisterResponse.Error -> {
                            Log.d(TAG, "REGISTER ERROR (field validation)")
                            onBackendErrors(response.errors)
                        }
                    }
                } catch (e: ClientRequestException) {
                    Log.d(TAG, "REGISTER EXCEPTION: ${e.message}")
                    val errorResponse = e.response.body<RegisterResponse.Error>()
                    onBackendErrors(errorResponse.errors)
                }
            }
        }

        triggerBiometric()
    }

    private fun triggerBiometric() {
        _biometricEvent.value = false
        _biometricEvent.value = true
    }

    fun runPending() {
        pendingAction?.invoke()
        pendingAction = null
        _biometricEvent.value = false
    }

    fun resetLoginCompleted() {
        _loginCompleted.value = false
    }
}