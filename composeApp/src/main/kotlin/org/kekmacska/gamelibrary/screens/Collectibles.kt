package org.kekmacska.gamelibrary.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.kekmacska.gamelibrary.components.FullScreenImage
import org.kekmacska.gamelibrary.components.ImageCarousel
import org.kekmacska.gamelibrary.viewModels.CollectiblesViewmodel

@Composable
fun CollectiblesScreen(
    gameId: Int,
    viewmodel: CollectiblesViewmodel = viewModel()
) {
    LaunchedEffect(gameId) {
        viewmodel.load(gameId)
    }

    val collectibles = viewmodel.collectibles
    val selectedImageIdx = viewmodel.selectedImageIdx
    val isFullScreen = selectedImageIdx != null

    val allImages = remember(collectibles) {
        collectibles.flatMap { it.images }
    }

    val pagerState = rememberPagerState(pageCount = { allImages.size })

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .blur(if (isFullScreen) 20.dp else 0.dp)
        ) {
            if (allImages.isNotEmpty()) {
                ImageCarousel(
                    images = allImages,
                    pagerState = pagerState,
                    onImageClick = { idx -> viewmodel.openImage(idx) }
                )
            }
        }

        if (isFullScreen) {
            FullScreenImage(
                images = allImages,
                startIdx = selectedImageIdx,
                onClose = { viewmodel.closeImage() }
            )
        }
    }
}