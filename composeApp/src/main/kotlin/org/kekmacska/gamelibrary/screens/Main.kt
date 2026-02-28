package org.kekmacska.gamelibrary.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.kekmacska.gamelibrary.components.GameCardComponent
import org.kekmacska.gamelibrary.components.GameListComponent
import org.kekmacska.gamelibrary.viewModels.MainViewModel

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBackClick: () -> Unit = {}
) {
    val items by viewModel.games.collectAsState()
    val isGridLayout by viewModel.isGridLayout.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // LazyVerticalGrid for Grid Layout
        if (isGridLayout) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp), // Padding to avoid floating button covering
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 30.dp)
            ) {
                items(items) { card ->
                    GameCardComponent(card) {
                        viewModel.selectGame(card)
                        navController.navigate("details")
                    }
                }
            }
        } else {
            // LazyColumn for List Layout
            LazyColumn(
                modifier = Modifier.padding(bottom = 80.dp), // Padding for floating action button
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 30.dp)
            ) {
                items(items) { item ->
                    GameListComponent(cardModel = item) {
                        viewModel.selectGame(item)
                        navController.navigate("details")
                    }
                }
            }
        }

        // Floating Action Button to toggle between Grid and List Layout
        FloatingActionButton(
            onClick = { viewModel.ToggleLayout() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp, 16.dp, 16.dp, 50.dp)
        ) {
            Icon(
                imageVector = if (isGridLayout) Icons.Filled.FormatListNumbered else Icons.Filled.GridOn,
                contentDescription = "Toggle Layout"
            )
        }
    }
}