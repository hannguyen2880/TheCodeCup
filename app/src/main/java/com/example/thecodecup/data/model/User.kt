package com.example.thecodecup.data.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val loyaltyCard: LoyaltyCard,
    val favoriteOrders: List<OrderItem> = emptyList(),
    val orderHistory: List<Order> = emptyList()
)

data class LoyaltyCard(
    val currentStamps: Int = 0,
    val maxStamps: Int = 8,
    val totalPoints: Int = 0,
    val membershipLevel: MembershipLevel = MembershipLevel.BRONZE,
    val isComplete: Boolean = false
)

enum class MembershipLevel(val displayName: String, val benefits: String) {
    BRONZE("Bronze", "5% discount"),
    SILVER("Silver", "10% discount + free size upgrade"),
    GOLD("Gold", "15% discount + free extra shot"),
    PLATINUM("Platinum", "20% discount + priority service")
}