package org.kekmacska.gamelibrary.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Publisher(
    val id:Int,
    val name: String,
    val headquarters: String,

    @SerialName("is_active")
    val isActive: UByte //1=active 0=inactive
)