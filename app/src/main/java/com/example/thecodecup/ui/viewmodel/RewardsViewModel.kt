package com.example.thecodecup.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thecodecup.data.model.PointsHistory
import com.example.thecodecup.data.model.RewardItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RewardsViewModel : ViewModel() {
    private val _userPoints = MutableStateFlow(100) // Initial points
    val userPoints: StateFlow<Int> = _userPoints.asStateFlow()

    private val _pointsHistoryList = mutableStateListOf(
        PointsHistory(
            id = "1",
            coffeeName = "Cappuccino",
            date = "24 June",
            time = "12:30 PM",
            pointsEarned = 12
        ),
        PointsHistory(
            id = "2",
            coffeeName = "Americano",
            date = "24 June",
            time = "11:45 AM",
            pointsEarned = 10
        ),
        PointsHistory(
            id = "3",
            coffeeName = "Cafe Latte",
            date = "23 June",
            time = "2:15 PM",
            pointsEarned = 15
        ),
        PointsHistory(
            id = "4",
            coffeeName = "Flat White",
            date = "22 June",
            time = "10:30 AM",
            pointsEarned = 18
        ),
        PointsHistory(
            id = "5",
            coffeeName = "Mocha",
            date = "21 June",
            time = "3:20 PM",
            pointsEarned = 20
        )
    )

    // In RewardsViewModel
    private val _pointsHistory = MutableStateFlow<List<PointsHistory>>(emptyList())
    val pointsHistory: StateFlow<List<PointsHistory>> = _pointsHistory.asStateFlow()

    private val dateFormatter = SimpleDateFormat("dd MMMM", Locale.getDefault())
    private val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())

    fun addPoints(points: Int) {
        viewModelScope.launch {
            _userPoints.value += points
        }
    }

    fun deductPoints(points: Int) {
        viewModelScope.launch {
            if (_userPoints.value >= points) {
                _userPoints.value -= points
            }
        }
    }

    fun addPointsFromOrder(coffeeName: String, orderAmount: Double) {
        val pointsEarned = (orderAmount * 5).toInt() // 5 points per dollar spent
        val currentDate = Date()

        val newHistory = PointsHistory(
            id = UUID.randomUUID().toString(),
            coffeeName = coffeeName,
            date = dateFormatter.format(currentDate),
            time = timeFormatter.format(currentDate),
            pointsEarned = pointsEarned
        )

        viewModelScope.launch {
            _userPoints.value += pointsEarned
            _pointsHistoryList.add(0, newHistory)
        }
    }

    fun addPointsFromCompletedOrder(orderId: String, bonusPoints: Int) {
        val currentDate = Date()

        val newHistory = PointsHistory(
            id = UUID.randomUUID().toString(),
            coffeeName = "Order Completion Bonus",
            date = dateFormatter.format(currentDate),
            time = timeFormatter.format(currentDate),
            pointsEarned = bonusPoints
        )

        viewModelScope.launch {
            _userPoints.value += bonusPoints
            _pointsHistoryList.add(0, newHistory)
        }
    }

    fun redeemReward(reward: RewardItem) {
        if (_userPoints.value >= reward.pointsRequired) {
            _userPoints.value -= reward.pointsRequired

            // Add redemption to history
            val redemptionHistory = PointsHistory(
                id = reward.id,
                coffeeName = "${reward.coffeeName} (Redeemed)",
                date = java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault())
                    .format(java.util.Date()),
                time = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
                    .format(java.util.Date()),
                pointsEarned = -reward.pointsRequired
            )

            _pointsHistory.value = listOf(redemptionHistory) + _pointsHistory.value
        }
    }

    fun getLoyaltyProgress(): Pair<Int, Int> {
        // Calculate loyalty card progress (cups collected out of 8)
        val cupsCollected = (_userPoints.value / 335).coerceAtMost(8) // 335 points per cup
        return Pair(cupsCollected, 8)
    }

    fun getPointsToNextReward(): Int {
        val nextRewardThreshold = 1340 // Standard reward threshold
        return (nextRewardThreshold - (_userPoints.value % nextRewardThreshold)).coerceAtLeast(0)
    }

    fun canRedeemReward(pointsRequired: Int): Boolean {
        return _userPoints.value >= pointsRequired
    }

    fun resetPoints() {
        viewModelScope.launch {
            _userPoints.value = 0
            _pointsHistoryList.clear()
        }
    }

    fun getAvailableRewards(): List<RewardItem> {
        // This could be moved to a repository in a real app
        return listOf(
            RewardItem(
                id = "reward_1",
                coffeeName = "Americano",
                coffeeImage = com.example.thecodecup.R.drawable.americano,
                pointsRequired = 1340,
                validUntil = "Valid until 04.07.23"
            ),
            RewardItem(
                id = "reward_2",
                coffeeName = "Cappuccino",
                coffeeImage = com.example.thecodecup.R.drawable.cappuccino,
                pointsRequired = 1340,
                validUntil = "Valid until 04.07.23"
            ),
            RewardItem(
                id = "reward_3",
                coffeeName = "Mocha",
                coffeeImage = com.example.thecodecup.R.drawable.mocha,
                pointsRequired = 1340,
                validUntil = "Valid until 04.07.23"
            ),
            RewardItem(
                id = "reward_4",
                coffeeName = "Flat White",
                coffeeImage = com.example.thecodecup.R.drawable.flat_white,
                pointsRequired = 1340,
                validUntil = "Valid until 04.07.23"
            )
        )
    }
}