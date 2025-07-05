package com.example.thecodecup.data.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchRepository(private val context: Context) {

    private val prefs = context.getSharedPreferences("search_prefs", Context.MODE_PRIVATE)
    private val _recentSearches = MutableStateFlow<List<String>>(loadRecentSearches())

    companion object {
        private const val RECENT_SEARCHES_KEY = "recent_searches"
        private const val MAX_RECENT_SEARCHES = 5
    }

    fun getRecentSearches(): Flow<List<String>> = _recentSearches.asStateFlow()

    suspend fun addRecentSearch(query: String) {
        val currentSearches = _recentSearches.value.toMutableList()

        // Remove if already exists
        currentSearches.remove(query)

        // Add to beginning
        currentSearches.add(0, query)

        // Keep only MAX_RECENT_SEARCHES items
        if (currentSearches.size > MAX_RECENT_SEARCHES) {
            currentSearches.removeAt(currentSearches.size - 1)
        }

        // Save to preferences
        saveRecentSearches(currentSearches)
        _recentSearches.value = currentSearches
    }

    suspend fun clearRecentSearches() {
        prefs.edit().remove(RECENT_SEARCHES_KEY).apply()
        _recentSearches.value = emptyList()
    }

    private fun loadRecentSearches(): List<String> {
        val searchesString = prefs.getString(RECENT_SEARCHES_KEY, "") ?: ""
        return if (searchesString.isEmpty()) {
            emptyList()
        } else {
            searchesString.split(",").filter { it.isNotEmpty() }
        }
    }

    private fun saveRecentSearches(searches: List<String>) {
        val searchesString = searches.joinToString(",")
        prefs.edit().putString(RECENT_SEARCHES_KEY, searchesString).apply()
    }
}