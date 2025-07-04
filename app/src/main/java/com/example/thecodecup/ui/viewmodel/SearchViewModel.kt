package com.example.thecodecup.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.thecodecup.data.model.Coffee
import com.example.thecodecup.data.repository.CoffeeRepositoryImpl
import com.example.thecodecup.data.repository.SearchRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val coffeeRepository = CoffeeRepositoryImpl()
    private val searchRepository = SearchRepository(application)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Coffee>>(emptyList())
    val searchResults: StateFlow<List<Coffee>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    private val _popularCoffees = MutableStateFlow<List<Coffee>>(emptyList())
    val popularCoffees: StateFlow<List<Coffee>> = _popularCoffees.asStateFlow()

    init {
        loadInitialData()

        // Auto-search when query changes (with debounce)
        searchQuery
            .debounce(300)
            .filter { it.isNotBlank() }
            .onEach { query ->
                searchCoffees(query)
            }
            .launchIn(viewModelScope)
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            // Load recent searches
            searchRepository.getRecentSearches().collect {
                _recentSearches.value = it
            }
        }

        viewModelScope.launch {
            // Load popular coffees
            coffeeRepository.getPopularCoffees().collect {
                _popularCoffees.value = it
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isEmpty()) {
            _searchResults.value = emptyList()
        }
    }

    fun performSearch() {
        val query = _searchQuery.value.trim()
        if (query.isNotEmpty()) {
            viewModelScope.launch {
                // Save to recent searches
                searchRepository.addRecentSearch(query)
                searchCoffees(query)
            }
        }
    }

    private fun searchCoffees(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                coffeeRepository.searchCoffees(query).collect { results ->
                    _searchResults.value = results
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _searchResults.value = emptyList()
                _isLoading.value = false
            }
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    fun clearRecentSearches() {
        viewModelScope.launch {
            searchRepository.clearRecentSearches()
        }
    }
}