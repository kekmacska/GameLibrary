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
    var isLoading by mutableStateOf(true)
        private set
    var collectibles by mutableStateOf<List<Collectible>>(emptyList())
        private set
    var error by mutableStateOf(false)
        private set
    var selectedImageIdx by mutableStateOf<Int?>(null)
        private set
    var currentCollectible by mutableStateOf<Collectible?>(null)
        private set

    fun load(gameId: Int) {
        viewModelScope.launch {
            isLoading=true
            try {
                val result = getCollectiblesForGame(gameId)
                collectibles = result
                error = false
                currentCollectible=result.firstOrNull()
                selectedImageIdx=null
            } catch (_: Exception) {
                collectibles = emptyList()
                error = true
                currentCollectible=null
                selectedImageIdx=null
            }finally {
                isLoading=false
                selectedImageIdx=null
            }
        }
    }

    fun openImage(collectible: Collectible,idx: Int){
        currentCollectible=collectible
        selectedImageIdx=idx
    }

    fun closeImage(){
        selectedImageIdx=null
    }

    fun selectCollectible(idx:Int){
        currentCollectible=collectibles.getOrNull(idx)
        selectedImageIdx=null
    }
}