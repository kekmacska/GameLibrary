package org.kekmacska.gamelibrary.viewModels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.kekmacska.gamelibrary.preferences.TokenStorage
import org.kekmacska.gamelibrary.preferences.saveLoggedIn
import org.kekmacska.gamelibrary.services.login

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
}