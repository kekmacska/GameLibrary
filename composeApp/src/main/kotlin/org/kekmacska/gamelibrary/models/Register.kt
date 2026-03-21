package org.kekmacska.gamelibrary.models

@kotlinx.serialization.Serializable
data class RegisterRequest (
    val name: String,
    val email:String,
    val password: String
    )