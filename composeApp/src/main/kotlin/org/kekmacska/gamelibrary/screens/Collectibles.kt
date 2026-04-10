package org.kekmacska.gamelibrary.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.kekmacska.gamelibrary.components.Cell
import org.kekmacska.gamelibrary.components.FullScreenImage
import org.kekmacska.gamelibrary.components.ImageCarousel
import org.kekmacska.gamelibrary.components.PaginationBar
import org.kekmacska.gamelibrary.components.Table
import org.kekmacska.gamelibrary.components.Tr
import org.kekmacska.gamelibrary.viewModels.CollectiblesViewmodel
import org.kekmacska.gamelibrary.viewModels.MainViewModel

@Composable
fun CollectiblesScreen(
    gameId: Int,
    collectiblesViewmodel: CollectiblesViewmodel = viewModel(),
    mainViewModel: MainViewModel = viewModel()
) {
    LaunchedEffect(gameId) {
        collectiblesViewmodel.load(gameId)
    }

    val currentCollectible=collectiblesViewmodel.currentCollectible
    val collectibles=collectiblesViewmodel.collectibles
    val currentGame = mainViewModel.allGames.collectAsState().value.find { it.id == gameId }
    val selectedImageIdx = collectiblesViewmodel.selectedImageIdx
    val isFullScreen = selectedImageIdx != null
    val currentCollectibleImages=currentCollectible?.images?:emptyList()
    val pagerState = rememberPagerState(pageCount = { currentCollectibleImages.size })
    val currentPage =collectibles.indexOf(currentCollectible)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .blur(if (isFullScreen) 20.dp else 0.dp)
        ) {
            Text(
                text = "${currentGame?.name ?: ""}'s collectibles",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 10.dp)
            )

            if (currentCollectibleImages.isNotEmpty()) {
                ImageCarousel(
                    images = currentCollectibleImages,
                    pagerState = pagerState,
                    onImageClick = { idx ->
                        currentCollectible?.let{
                            collectiblesViewmodel.openImage(it,idx)
                        }
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            if(currentCollectible!=null){
                Table(modifier = Modifier.padding(horizontal = 16.dp)){
                    Tr{
                        Cell{ Text("Type", fontWeight = FontWeight.Bold)}
                        Cell{Text(currentCollectible.type)}
                    }
                    Tr{
                        Cell{Text("Description", fontWeight = FontWeight.Bold)}
                        Cell{Text(currentCollectible.description)}
                    }
                    Tr{
                        Cell{Text("Coordinates", fontWeight = FontWeight.Bold)}
                        Cell{
                            Text(
                                text = currentCollectible.coordinates?.takeIf
                                { it != listOf(.0f, .0f) }?.joinToString(", ")
                                    ?: "Not applicable"
                            )
                        }
                    }
                }
            }
            if (collectibles.size > 1) {
                PaginationBar(
                    currentPage = currentPage + 1,
                    totalPages = collectibles.size,
                    onPrev = {
                        if (currentPage > 0) {
                            collectiblesViewmodel.selectCollectible(currentPage - 1)
                        }
                    },
                    onNext = {
                        if (currentPage < collectibles.size - 1) {
                            collectiblesViewmodel.selectCollectible(currentPage + 1)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (isFullScreen&&currentCollectible!=null) {
            FullScreenImage(
                images = currentCollectibleImages,
                startIdx = selectedImageIdx,
                onClose = { collectiblesViewmodel.closeImage() }
            )
        }
    }
}