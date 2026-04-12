package org.kekmacska.gamelibrary.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String,

    @SerialName("registered_at")
    val registeredAt: String?=null
)