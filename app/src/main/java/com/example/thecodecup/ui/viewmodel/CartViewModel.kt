package com.example.thecodecup.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.thecodecup.data.model.CartItem
import com.example.thecodecup.data.model.Order
import com.example.thecodecup.data.model.OrderStatus
import com.example.thecodecup.ui.viewmodel.RewardsViewModel
import com.example.thecodecup.ui.viewmodel.OrdersViewModel
import com.example.thecodecup.data.model.OrderItem
import java.text.SimpleDateFormat
import java.util.*

class CartViewModel : ViewModel() {
    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> = _cartItems

    fun addItem(item: CartItem) {
        val existingItemIndex = _cartItems.indexOfFirst {
            it.uniqueKey == item.uniqueKey
        }

        if (existingItemIndex != -1) {
            val existingItem = _cartItems[existingItemIndex]
            _cartItems[existingItemIndex] = existingItem.copy(
                quantity = existingItem.quantity + item.quantity,
                totalPrice = existingItem.totalPrice + item.totalPrice
            )
        } else {
            _cartItems.add(item)
        }
    }

    fun removeItem(item: CartItem) {
        _cartItems.remove(item)
    }

    fun updateQuantity(item: CartItem, newQuantity: Int) {
        val index = _cartItems.indexOf(item)
        if (index != -1) {
            val basePrice = item.totalPrice / item.quantity
            _cartItems[index] = item.copy(
                quantity = newQuantity,
                totalPrice = basePrice * newQuantity
            )
        }
    }

    fun clearCart() {
        _cartItems.clear()
    }

    fun getTotalPrice(): Double {
        return _cartItems.sumOf { it.totalPrice }
    }

    fun getItemCount(): Int {
        return _cartItems.sumOf { it.quantity }
    }
    // Add this method to your CartViewModel class
    fun createOrderFromCart(ordersViewModel: OrdersViewModel, rewardsViewModel: RewardsViewModel) {
        if (cartItems.isNotEmpty()) {
            val totalAmount = getTotalPrice()
            val orderItems = cartItems.map { cartItem ->
                OrderItem(
                    coffee = cartItem.coffee,
                    customization = cartItem.customization,
                    quantity = cartItem.quantity,
                    itemPrice = cartItem.totalPrice
                )
            }

            val order = Order(
                id = "order_${System.currentTimeMillis()}",
                items = orderItems,
                totalAmount = totalAmount,
                status = OrderStatus.ONGOING,
                coffeeName = if (cartItems.size == 1) cartItems.first().coffee.name else "${cartItems.size} items",
                date = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date()),
                time = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date()),
                price = totalAmount,
                address = "Your delivery address"
            )

            ordersViewModel.addOrder(order)

            // Add points for the order (10 points per dollar spent)
            val pointsEarned = (totalAmount * 10).toInt()
            rewardsViewModel.addPoints(pointsEarned)
        }
    }
}