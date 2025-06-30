package com.example.thecodecup.data.model
import java.util.Date

data class Order(
    val id: String,
    val items: List<OrderItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val status: OrderStatus,
    val orderDate: Date = Date(),
    val estimatedTime: String = "15-20 min",
    val customerName: String = "",
    val loyaltyPointsEarned: Int = 0,

    val coffeeName: String = "",
    val date: String = "",
    val time: String = "",
    val price: Double = 0.0,
    val address: String = ""
)

data class OrderItem(
    val coffee: Coffee,
    val customization: CoffeeCustomization,
    val quantity: Int,
    val itemPrice: Double
)

enum class OrderStatus(val displayName: String) {
    PENDING("Pending"),
    PREPARING("Preparing"),
    READY("Ready"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    ONGOING("On Going")
}