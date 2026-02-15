package org.kekmacska.gamelibrary.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kekmacska.gamelibrary.components.GameCardComponent
import org.kekmacska.gamelibrary.models.MainScreenCard

@Composable
fun MainScreen(cards: List<MainScreenCard>) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(cards) { card -> GameCardComponent(card) }
    }
}