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
//sajnos muszáj primitíveket használni a helyes szerializáció érdekében. Csak kíráskor lehet kasztolni
//unfortunately i need to use primitive types to achieve a successful serialization. I can only cast it when displaying it