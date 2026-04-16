package org.kekmacska.gamelibrary.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Game(
    val id: Int,
    val name: String,

    @SerialName("release_year")
    val releaseYear: Int,
    val genre: String,

    @SerialName("publisher_id")
    val publisherId: Int,
    val platforms: List<String>,
    val cover: String?,

    @SerialName("freetogame_url")
    val freetogameUrl: String?,
)
//unfortunately I need to use primitive types to achieve a successful deserialization. I can only cast it when displaying it