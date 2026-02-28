package org.kekmacska.gamelibrary.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.kekmacska.gamelibrary.models.Game
import org.kekmacska.gamelibrary.services.getAllGames

class MainViewModel : ViewModel() {

    private val _games = MutableStateFlow<List<Game>>(emptyList())
    private val _isGridLayout = MutableStateFlow(true)
    val isGridLayout = _isGridLayout.asStateFlow()
    val games: StateFlow<List<Game>> = _games

    init {
        loadRandomGames()
    }

    fun loadRandomGames() {
        viewModelScope.launch {
            val allGames = getAllGames()
            _games.value = allGames.shuffled().take(9)
        }
    }

    private val _selectedGame = MutableStateFlow<Game?>(null)
    val selectedGame = _selectedGame

    fun selectGame(game: Game) {
        _selectedGame.value = game
    }

    fun ToggleLayout() {
        _isGridLayout.value = !_isGridLayout.value
    }
}