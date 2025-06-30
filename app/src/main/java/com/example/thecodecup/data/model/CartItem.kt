package com.example.thecodecup.data.model

data class CartItem(
    val id: String,
    val coffee: Coffee,
    val customization: CoffeeCustomization,
    val quantity: Int,
    val totalPrice: Double,
    val isHot: Boolean = true,
    val iceLevel: Int = 3
) {
    val uniqueKey: String
        get() = "${coffee.id}_${customization.hashCode()}_$isHot"
}