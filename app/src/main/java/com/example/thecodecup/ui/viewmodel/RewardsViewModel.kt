package com.example.thecodecup.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class RewardItem(
    val id: String,
    val title: String,
    val description: String,
    val pointsCost: Int,
    val imageRes: Int,
    val isAvailable: Boolean = true
)

class RewardsViewModel : ViewModel() {
    private val _currentPoints = MutableStateFlow(150)
    val currentPoints: StateFlow<Int> = _currentPoints.asStateFlow()

    private val _stampsCollected = MutableStateFlow(4)
    val stampsCollected: StateFlow<Int> = _stampsCollected.asStateFlow()

    private val _rewardHistory = MutableStateFlow<List<String>>(emptyList())
    val rewardHistory: StateFlow<List<String>> = _rewardHistory.asStateFlow()

    fun addPoints(points: Int) {
        _currentPoints.value += points
    }

    fun addStamp() {
        if (_stampsCollected.value < 8) {
            _stampsCollected.value += 1
            if (_stampsCollected.value == 8) {
                // Free coffee when 8 stamps collected
                _stampsCollected.value = 0
                addRewardToHistory("Free Coffee Redeemed!")
            }
        }
    }

    fun redeemReward(reward: RewardItem): Boolean {
        return if (_currentPoints.value >= reward.pointsCost) {
            _currentPoints.value -= reward.pointsCost
            addRewardToHistory("Redeemed: ${reward.title}")
            true
        } else {
            false
        }
    }

    private fun addRewardToHistory(reward: String) {
        _rewardHistory.value = _rewardHistory.value + reward
    }

    fun onOrderCompleted(orderAmount: Double) {
        val pointsEarned = (orderAmount * 10).toInt() // 10 points per dollar
        addPoints(pointsEarned)
        addStamp()
    }
}