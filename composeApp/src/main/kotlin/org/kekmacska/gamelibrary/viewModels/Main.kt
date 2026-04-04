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

    //search
    private val _searchQuery= MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    private val _searchAttribute= MutableStateFlow("name")
    val searchAttribute: StateFlow<String> = _searchAttribute.asStateFlow()

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

    fun clearError() {
        _error.value = null
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

    fun toggleLayout() {
        _isGridLayout.value = !_isGridLayout.value
    }

    //search
    fun setSearchQuery(query: String){
        _searchQuery.value=query
        _currentPage.value=1 //reset to first page of the search results
    }

    fun setSearchAttribute(attribute: String){
        _searchAttribute.value=attribute
        _currentPage.value=1
    }

    val filteredGames: StateFlow<List<Game>> = combine(_allGames,_searchQuery,_searchAttribute){games,query,attribute->
        if(query.isBlank())return@combine games

        val regex=try{
            Regex(query, RegexOption.IGNORE_CASE)
        }catch (_: Throwable){
            null
        }

        games.filter { game ->
            when(attribute){
                "name"->regex?.containsMatchIn(game.name)?:
                    game.name.contains(query,true)
                "releaseYear"->regex?.containsMatchIn(game.releaseYear.toString())?:
                    game.releaseYear.toString().contains(query)
                "genre"->regex?.containsMatchIn(game.genre)?:
                    game.genre.contains(query,true)
                else->false
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),emptyList())
}