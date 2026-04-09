package org.kekmacska.gamelibrary.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Collectible(
    val id: Int,

    @SerialName("game_id")
    val gameId:Int,

    val type:String,

    val description: String,

    val images:List<String>,

    @SerialName("map_location")
    val coordinates: List<Float>? = null
)