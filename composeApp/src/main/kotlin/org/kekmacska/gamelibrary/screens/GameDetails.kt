package org.kekmacska.gamelibrary.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import org.kekmacska.gamelibrary.components.Cell
import org.kekmacska.gamelibrary.components.Table
import org.kekmacska.gamelibrary.components.Tr
import org.kekmacska.gamelibrary.components.shimmer.ShimmerCollectiblesButtonPlaceholder
import org.kekmacska.gamelibrary.components.shimmer.ShimmerText
import org.kekmacska.gamelibrary.models.Game
import org.kekmacska.gamelibrary.models.Publisher
import org.kekmacska.gamelibrary.services.getPublisherById
import org.kekmacska.gamelibrary.viewModels.CollectiblesViewmodel
@Composable
fun GameDetails(
    game: Game,
    navController: NavController,
    collectiblesViewmodel: CollectiblesViewmodel= viewModel(key="collectibles-${game.id}")
) {
    val context= LocalContext.current
    val scrollState= rememberScrollState()
    val uriHandler = LocalUriHandler.current
    var publisher by remember { mutableStateOf<Publisher?>(null) }
    LaunchedEffect(game.id) {
        collectiblesViewmodel.load(game.id)
    }
    LaunchedEffect(game.publisherId) {
        publisher= getPublisherById(game.publisherId,context)
    }
    val collectibles=collectiblesViewmodel.collectibles


    Column(modifier = Modifier.padding(16.dp).verticalScroll(scrollState)) {

        AsyncImage(
            model = game.cover,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            contentScale = ContentScale.Fit
        )

        // Space between image and table
        Spacer(modifier = Modifier.padding(top = 16.dp))

        Table {
            Tr {
                Cell { Text(text = "Name", fontWeight = FontWeight.Bold) }
                Cell { Text(text = game.name) }
            }
            Tr {
                Cell { Text(text = "Release year", fontWeight = FontWeight.Bold) }
                Cell { Text(text = game.releaseYear.toString()) }
            }
            Tr {
                Cell { Text(text = "Genre", fontWeight = FontWeight.Bold) }
                Cell { Text(text = game.genre) }
            }
            Tr {
                Cell { Text(text = "Publisher", fontWeight = FontWeight.Bold) }
                Cell { ShimmerText(text = publisher?.name, width = 120.dp, height = 20.dp) }
            }
            Tr {
                Cell { Text(text = "Platforms", fontWeight = FontWeight.Bold) }
                Cell { Text(text = game.platforms.joinToString(", ")) }
            }

            Spacer(Modifier.height(8.dp))

            Tr {
                Cell {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        if (!game.freetogameUrl.isNullOrEmpty()) {
                            Text(
                                text = "Freetogame link",
                                modifier = Modifier.clickable {
                                    uriHandler.openUri(game.freetogameUrl)
                                }
                            )
                        } else {
                            Text("There is no Freetogame article about this game")
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            when {
                collectiblesViewmodel.isLoading -> {
                    ShimmerCollectiblesButtonPlaceholder()
                }

                collectibles.isNotEmpty() -> {
                    Button(
                        onClick = {
                            navController.navigate("collectibles/${game.id}") {
                                popUpTo("register") { inclusive = true }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View collectibles")
                    }
                }
            }

        }
    }
}