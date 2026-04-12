package org.kekmacska.gamelibrary.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.lifecycle.viewmodel.compose.viewModel
import org.kekmacska.gamelibrary.components.GameCardComponent
import org.kekmacska.gamelibrary.components.GameListComponent
import org.kekmacska.gamelibrary.components.PaginationBar
import org.kekmacska.gamelibrary.components.SearchBar
import org.kekmacska.gamelibrary.components.shimmer.ShimmerMainGridPlaceholder
import org.kekmacska.gamelibrary.components.shimmer.ShimmerMainListPlaceholder
import org.kekmacska.gamelibrary.viewModels.MainViewModel

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val items by viewModel.paginatedGames.collectAsState()
    val isGridLayout by viewModel.isGridLayout.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val totalPages by viewModel.totalPages.collectAsState()

    if (error == null) {
        Column(modifier = Modifier.fillMaxSize()) {

            SearchBar(viewModel)

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                //grid layout
                if (isGridLayout) {
                    if (isLoading) {
                        ShimmerMainGridPlaceholder()
                    } else {
                        LazyVerticalGrid(
                            modifier = Modifier.fillMaxSize().padding(bottom = 80.dp),
                            columns = GridCells.Fixed(3),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
                        ) {
                            items(items, key = { it.id }) { card ->
                                GameCardComponent(card) {
                                    viewModel.selectGame(card)
                                    navController.navigate("details")
                                }
                            }
                        }
                    }
                }

                //list layout
                else {
                    if (isLoading) {
                        ShimmerMainListPlaceholder()
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(bottom = 80.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 30.dp)
                        ) {
                            items(items, key = { it.id }) { item ->
                                GameListComponent(cardModel = item) {
                                    viewModel.selectGame(item)
                                    navController.navigate("details")
                                }
                            }
                        }
                    }
                }

                //fab to change between grid and list
                FloatingActionButton(
                    onClick = { viewModel.toggleLayout() },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp, 16.dp, 16.dp, 60.dp)
                ) {
                    Icon(
                        imageVector = if (isGridLayout)
                            Icons.Filled.FormatListNumbered
                        else
                            Icons.Filled.GridOn,
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
        ErrorScreen(error) {customUrl ->
            viewModel.loadGames(customUrl)
            viewModel.clearError()
        }
    }
}