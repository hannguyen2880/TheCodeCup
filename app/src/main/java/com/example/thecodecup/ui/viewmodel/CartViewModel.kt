package com.example.thecodecup.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.thecodecup.data.model.CartItem

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
}