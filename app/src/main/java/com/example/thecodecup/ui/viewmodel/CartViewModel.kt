package com.example.thecodecup.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.thecodecup.data.model.CartItem
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class CartViewModel : ViewModel() {
    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> = _cartItems

    fun addItem(item: CartItem) {
        val existingIndex = _cartItems.indexOfFirst { it.uniqueKey == item.uniqueKey }
        if (existingIndex != -1) {
            val existingItem = _cartItems[existingIndex]
            _cartItems[existingIndex] = existingItem.copy(
                quantity = existingItem.quantity + item.quantity,
                totalPrice = (existingItem.quantity + item.quantity) *
                        (existingItem.totalPrice / existingItem.quantity)
            )
        } else {
            _cartItems.add(item)
        }
    }

    fun removeItem(item: CartItem) {
        _cartItems.remove(item)
    }

    fun updateQuantity(item: CartItem, newQuantity: Int) {
        val index = _cartItems.indexOfFirst { it.uniqueKey == item.uniqueKey }
        if (index != -1 && newQuantity > 0) {
            val unitPrice = item.totalPrice / item.quantity
            val updatedItem = item.copy(
                quantity = newQuantity,
                totalPrice = unitPrice * newQuantity
            )
            _cartItems[index] = updatedItem
        } else if (newQuantity <= 0) {
            removeItem(item)
        }
    }

    fun getTotalPrice(): Double {
        return _cartItems.sumOf { it.totalPrice }
    }

    fun clearCart() {
        _cartItems.clear()
    }

    fun getItemCount(): Int {
        return _cartItems.sumOf { it.quantity }
    }
}

class CartViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}