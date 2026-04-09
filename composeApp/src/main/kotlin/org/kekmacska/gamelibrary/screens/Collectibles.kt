package org.kekmacska.gamelibrary.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.kekmacska.gamelibrary.viewModels.CollectiblesViewmodel

@Composable
fun CollectiblesScreen(
    gameId:Int,
    viewmodel: CollectiblesViewmodel= viewModel()
){
    LaunchedEffect(gameId) {
        viewmodel.load(gameId)
    }

    val collectibles=viewmodel.collectibles
    val error=viewmodel.error

    Column(modifier= Modifier.padding(16.dp)){
        Text("Collectibles for ...") //TODO
        collectibles.forEach { item->
            Text("* ${item.type}: ${item.description}")

            Spacer(Modifier.height(8.dp))
        }
    }
}