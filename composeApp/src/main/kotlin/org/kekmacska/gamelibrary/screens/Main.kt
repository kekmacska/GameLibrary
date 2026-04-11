package org.kekmacska.gamelibrary.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import org.kekmacska.gamelibrary.components.PaginationBar
import org.kekmacska.gamelibrary.components.SearchBar
import org.kekmacska.gamelibrary.viewModels.MainViewModel

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val items by viewModel.paginatedGames.collectAsState() //support filtering and paginating
    val isGridLayout by viewModel.isGridLayout.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val totalPages by viewModel.totalPages.collectAsState()

    if (error == null) {
        Column(modifier = Modifier.fillMaxSize()) {
            //show search anywhere if there is no error
            SearchBar(viewModel)

            Box(modifier = Modifier.fillMaxSize().weight(1f)) {
                // LazyVerticalGrid for Grid Layout
                if (isGridLayout) {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 80.dp),
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
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
                        modifier = Modifier.padding(bottom = 80.dp),
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
                    onClick = { viewModel.toggleLayout() },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp, 16.dp, 16.dp, 60.dp)
                ) {
                    Icon(
                        imageVector = if (isGridLayout) Icons.Filled.FormatListNumbered else Icons.Filled.GridOn,
                        contentDescription = "Toggle Layout"
                    )
                }

                //pagination
                PaginationBar(
                    currentPage = currentPage,
                    totalPages = totalPages,
                    onPrev = { viewModel.previousPage() },
                    onNext = { viewModel.nextPage() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    } else {
        ErrorScreen(error) {
            viewModel.loadGames()
            viewModel.clearError()
        }
    }
}