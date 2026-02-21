package org.kekmacska.gamelibrary.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.kekmacska.gamelibrary.models.Game

@Composable
fun Table(content: @Composable () -> Unit) {
    Column {
        content()
    }
}

@Composable
fun Tr(content: @Composable RowScope.() -> Unit) {
    Row(Modifier.fillMaxWidth()) {
        content()   // now this lambda is a RowScope
    }
}

@Composable
fun RowScope.Cell(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .weight(1f)
            .padding(8.dp)
    ) {
        content()
    }
}

@Composable
fun GameDetails(cardModel: Game) {
    val uriHandler = LocalUriHandler.current

    Column(modifier = Modifier.padding(16.dp)) {

        AsyncImage(
            model = cardModel.cover,
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
                Cell { Text(text = cardModel.name) }
            }
            Tr {
                Cell { Text(text = "Release year", fontWeight = FontWeight.Bold) }
                Cell { Text(text = cardModel.releaseYear.toString()) }
            }
            Tr {
                Cell { Text(text = "Genre", fontWeight = FontWeight.Bold) }
                Cell { Text(text = cardModel.genre) }
            }
            Tr {
                Cell { Text(text = "Publisher", fontWeight = FontWeight.Bold) }
                Cell { Text(text = cardModel.publisherId.toString()) }
            }
            Tr {
                Cell { Text(text = "Platforms", fontWeight = FontWeight.Bold) }
                Cell { Text(text = cardModel.platforms.joinToString(", ")) }
            }
            Spacer(Modifier.height(8.dp))
            Tr {
                Cell {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        if (!cardModel.freetogameUrl.isNullOrEmpty()) {
                            Text(
                                text = "Freetogame link",
                                modifier = Modifier.clickable {
                                    uriHandler.openUri(cardModel.freetogameUrl)
                                }
                            )
                        } else {
                            Text("There is no Freetogame article about this game")
                        }
                    }
                }
            }
        }
    }
}