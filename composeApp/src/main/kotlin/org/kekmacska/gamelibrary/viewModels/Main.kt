package org.kekmacska.gamelibrary.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.kekmacska.gamelibrary.models.Game
import org.kekmacska.gamelibrary.services.getAllGames

class MainViewModel : ViewModel() {

    private val pageSize = 9
    private val _allGames = MutableStateFlow<List<Game>>(emptyList())
    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    val games: StateFlow<List<Game>> = combine(_allGames, _currentPage) { games, page ->
        val start = (page - 1) * pageSize
        games.drop(start).take(pageSize)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val totalPages: StateFlow<Int> = _allGames.map { games ->
        if (games.isEmpty()) 1 else (games.size + pageSize - 1) / pageSize
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)
    private val _isGridLayout = MutableStateFlow(true)
    val isGridLayout = _isGridLayout.asStateFlow()

    private val _error: MutableStateFlow<Throwable?> = MutableStateFlow(null)
    val error: StateFlow<Throwable?> = _error.asStateFlow()
    private val _selectedGame = MutableStateFlow<Game?>(null)

    fun clearError() {
        _error.value = null
    }

    init {
        loadGames()
    }

    fun loadGames() {
        viewModelScope.launch {
            try { //to catch errors, like unreachable backend
                val allGames = getAllGames().shuffled()
                _allGames.value = allGames //fill the all games list
                _currentPage.value = 1
                _error.value = null
            } catch (e: Throwable) {
                _error.value = e //update error state
            }
        }
    }

    fun nextPage() {
        if (_currentPage.value < totalPages.value) {
            _currentPage.value++
        }
    }

    fun previousPage() {
        if (_currentPage.value > 1) {
            _currentPage.value--
        }
    }

    val selectedGame = _selectedGame

    fun selectGame(game: Game) {
        _selectedGame.value = game
    }

    fun ToggleLayout() {
        _isGridLayout.value = !_isGridLayout.value
    }
}