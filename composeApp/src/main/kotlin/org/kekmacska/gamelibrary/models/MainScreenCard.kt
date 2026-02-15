package org.kekmacska.gamelibrary.models

import androidx.annotation.DrawableRes

data class MainScreenCard(
    @DrawableRes val image: Int,
    val name: String,
    val genre: String
)