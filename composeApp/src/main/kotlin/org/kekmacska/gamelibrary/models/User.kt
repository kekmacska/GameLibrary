package org.kekmacska.gamelibrary.models

@kotlinx.serialization.Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val registered_at: String?=null
)