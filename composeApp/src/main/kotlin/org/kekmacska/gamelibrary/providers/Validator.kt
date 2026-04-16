package org.kekmacska.gamelibrary.providers

import androidx.core.net.toUri

object Validators{
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$".toRegex()

    fun isValidCustomApiUrl(url: String): Boolean{
        return try {
            val uri= url.toUri()
            val validScheme=uri.scheme=="http"||uri.scheme=="https"
            val hasHost=!uri.host.isNullOrEmpty()
            validScheme&&hasHost
        }catch (_: Exception){
            false
        }
    }
}