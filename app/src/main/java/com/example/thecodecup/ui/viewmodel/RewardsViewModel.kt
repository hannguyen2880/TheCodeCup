package com.example.thecodecup.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.thecodecup.data.model.RewardItem
import com.example.thecodecup.data.model.PointsHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RewardsViewModel : ViewModel() {
    private val _userPoints = MutableStateFlow(2750)
    val userPoints: StateFlow<Int> = _userPoints.asStateFlow()

    private val _pointsHistory = MutableStateFlow(
        listOf(
            PointsHistory(
                id = "1",
                coffeeName = "Americano",
                date = "24 June",
                time = "12:30 PM",
                pointsEarned = 12
            ),
            PointsHistory(
                id = "2",
                coffeeName = "Cappuccino",
                date = "23 June",
                time = "2:15 PM",
                pointsEarned = 15
            ),
            PointsHistory(
                id = "3",
                coffeeName = "Mocha",
                date = "22 June",
                time = "10:30 AM",
                pointsEarned = 18
            ),
            PointsHistory(
                id = "4",
                coffeeName = "Flat White",
                date = "21 June",
                time = "3:20 PM",
                pointsEarned = 16
            )
        )
    )
    val pointsHistory: StateFlow<List<PointsHistory>> = _pointsHistory.asStateFlow()

    fun redeemReward(reward: RewardItem) {
        val currentPoints = _userPoints.value
        if (currentPoints >= reward.pointsRequired) {
            _userPoints.value = currentPoints - reward.pointsRequired
        }
    }

    fun addPoints(points: Int) {
        _userPoints.value += points
    }
}