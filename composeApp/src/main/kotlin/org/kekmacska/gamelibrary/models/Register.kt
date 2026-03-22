package org.kekmacska.gamelibrary.models

@kotlinx.serialization.Serializable
data class RegisterRequest (
    val name: String,
    val email:String,
    val password: String
    )

@kotlinx.serialization.Serializable
sealed class RegisterResponse {

    @kotlinx.serialization.Serializable
    data class Success(
        val message: String,
        val user: User,
        val token: String
    ) : RegisterResponse()

    @kotlinx.serialization.Serializable
    data class Error(
        val message: String,
        val errors: RegisterFieldErrors
    ) : RegisterResponse()
}

@kotlinx.serialization.Serializable
data class RegisterFieldErrors(
    val name: List<String>? = null,
    val email: List<String>? = null,
    val password: List<String>? = null
)
