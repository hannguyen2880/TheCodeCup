package com.example.thecodecup.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thecodecup.data.model.CartItem
import com.example.thecodecup.data.repository.CartRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {

    val cartItems: StateFlow<List<CartItem>> = cartRepository.cartItems

    fun addItem(item: CartItem) {
        viewModelScope.launch {
            cartRepository.addItem(item)
        }
    }

    fun removeItem(item: CartItem) {
        viewModelScope.launch {
            cartRepository.removeItem(item)
        }
    }

    fun updateQuantity(item: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(item, newQuantity)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }

    fun getTotalPrice(): Double {
        return cartItems.value.sumOf { it.totalPrice }
    }

    fun getItemCount(): Int {
        return cartItems.value.sumOf { it.quantity }
    }
}