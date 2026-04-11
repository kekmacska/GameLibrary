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
import kotlin.collections.emptyList

class MainViewModel : ViewModel() {
    private val gridPageSize=9
    private val listPageSize=5
    private val _isLoading= MutableStateFlow(true)
    val isLoading=_isLoading.asStateFlow()
    private val _allGames = MutableStateFlow<List<Game>>(emptyList())
    val allGames = _allGames.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _isGridLayout = MutableStateFlow(true)
    val isGridLayout = _isGridLayout.asStateFlow()
    private val pageSize: StateFlow<Int> =
        isGridLayout
            .map { isGrid -> if (isGrid) gridPageSize else listPageSize }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), gridPageSize)

    // search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchAttribute = MutableStateFlow("name")
    val searchAttribute = _searchAttribute.asStateFlow()
    val filteredGames: StateFlow<List<Game>> =
        combine(_allGames, _searchQuery, _searchAttribute) { games, query, attribute ->
            if (query.isBlank()) return@combine games

            val regex = try { Regex(query, RegexOption.IGNORE_CASE) } catch (_: Throwable) { null }

            games.filter { game ->
                when (attribute) {
                    "name" -> regex?.containsMatchIn(game.name)
                        ?: game.name.contains(query, true)

                    "releaseYear" -> regex?.containsMatchIn(game.releaseYear.toString())
                        ?: game.releaseYear.toString().contains(query)

                    "genre" -> regex?.containsMatchIn(game.genre)
                        ?: game.genre.contains(query, true)

                    else -> false
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val totalPages: StateFlow<Int> =
        combine(filteredGames, pageSize) { games, size ->
            if (games.isEmpty()) 1 else (games.size + size - 1) / size
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    private val _error = MutableStateFlow<Throwable?>(null)
    val error = _error.asStateFlow()
    private val _selectedGame = MutableStateFlow<Game?>(null)
    val selectedGame = _selectedGame

    init {
        loadGames()
    }

    fun loadGames() {
        viewModelScope.launch {
            _isLoading.value=true
            try {
                val allGames = getAllGames().shuffled()
                _allGames.value = allGames
                _currentPage.value = 1
                _error.value = null
            } catch (e: Throwable) {
                _error.value = e
            }finally {
                _isLoading.value=false
            }
        }
    }

    fun clearError() { _error.value = null }

    fun nextPage() {
        if (_currentPage.value < totalPages.value) _currentPage.value++
    }

    fun previousPage() {
        if (_currentPage.value > 1) _currentPage.value--
    }

    fun selectGame(game: Game) { _selectedGame.value = game }

    fun toggleLayout() { _isGridLayout.value = !_isGridLayout.value }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        _currentPage.value = 1
    }

    fun setSearchAttribute(attribute: String) {
        _searchAttribute.value = attribute
        _currentPage.value = 1
    }

    val paginatedGames: StateFlow<List<Game>> =
        combine(filteredGames, currentPage, pageSize) { games, page, size ->
            val from = (page - 1) * size
            val to = minOf(from + size, games.size)
            if (from >= games.size) emptyList() else games.subList(from, to)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}