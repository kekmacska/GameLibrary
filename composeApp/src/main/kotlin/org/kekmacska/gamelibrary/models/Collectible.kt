package org.kekmacska.gamelibrary.models

import kotlinx.serialization.Serializable

@Serializable
data class Collectible(
    val id: Int,
    val gameId:Int,
    val type:String,
    val description: String,
    val images:List<String>,
    val coordinates: List<Float>? = null
)