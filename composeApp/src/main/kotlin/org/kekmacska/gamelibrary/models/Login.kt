package org.kekmacska.gamelibrary.models

@kotlinx.serialization.Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@kotlinx.serialization.Serializable
data class LoginResponse(
    val user: User?,
    val token: String? = null
)