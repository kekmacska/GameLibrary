package org.kekmacska.gamelibrary.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.kekmacska.gamelibrary.models.Collectible
import org.kekmacska.gamelibrary.services.getCollectiblesForGame

class CollectiblesViewmodel: ViewModel(){
    var collectibles by mutableStateOf<List<Collectible>>(emptyList())
        private set
    var error by mutableStateOf(false)
        private set

    fun load(gameId: Int) {
        viewModelScope.launch {
            try {
                val result = getCollectiblesForGame(gameId)
                collectibles = result
                error = false
            } catch (e: Exception) {
                collectibles = emptyList()
                error = true
            }
        }
    }
}