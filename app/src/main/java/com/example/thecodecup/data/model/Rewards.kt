package com.example.thecodecup.data.model

data class UserRewards(
    val totalPoints: Int,
    val loyaltyCard: LoyaltyCard,
    val pointsHistory: List<PointsHistory>
)

data class PointsHistory(
    val id: String,
    val coffeeName: String,
    val date: String,
    val time: String,
    val pointsEarned: Int
)

data class RewardItem(
    val id: String,
    val coffeeName: String,
    val coffeeImage: Int,
    val pointsRequired: Int,
    val validUntil: String
)