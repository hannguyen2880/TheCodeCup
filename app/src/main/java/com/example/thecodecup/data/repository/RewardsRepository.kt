package com.example.thecodecup.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.thecodecup.data.model.PointsHistory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RewardsRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("rewards_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _totalPoints = MutableStateFlow(loadTotalPoints())
    val totalPoints: StateFlow<Int> = _totalPoints.asStateFlow()

    private val _pointsHistory = MutableStateFlow(loadPointsHistory())
    val pointsHistory: StateFlow<List<PointsHistory>> = _pointsHistory.asStateFlow()

    private fun loadTotalPoints(): Int {
        return prefs.getInt("total_points", 2040)
    }

    private fun loadPointsHistory(): List<PointsHistory> {
        val historyJson = prefs.getString("points_history", null)
        return if (historyJson != null) {
            val type = object : TypeToken<List<PointsHistory>>() {}.type
            gson.fromJson(historyJson, type)
        } else {
            getDefaultHistory()
        }
    }

    fun updateTotalPoints(points: Int) {
        prefs.edit().putInt("total_points", points).apply()
        _totalPoints.value = points
    }

    fun addPoints(points: Int) {
        val currentPoints = _totalPoints.value
        updateTotalPoints(currentPoints + points)
    }

    fun addPointsHistory(history: PointsHistory) {
        val currentHistory = _pointsHistory.value.toMutableList()
        currentHistory.add(0, history)
        savePointsHistory(currentHistory)
    }

    private fun savePointsHistory(history: List<PointsHistory>) {
        val historyJson = gson.toJson(history)
        prefs.edit().putString("points_history", historyJson).apply()
        _pointsHistory.value = history
    }

    private fun getDefaultHistory(): List<PointsHistory> {
        return listOf(
            PointsHistory("1", "Americano", "24 June", "12:30 PM", 12),
            PointsHistory("2", "Cafe Latte", "24 June", "11:45 AM", 10)
        )
    }
}