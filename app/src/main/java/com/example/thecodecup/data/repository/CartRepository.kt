package com.example.thecodecup.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.thecodecup.data.model.CartItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _cartItems = MutableStateFlow(loadCartItems())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private fun loadCartItems(): List<CartItem> {
        val cartJson = prefs.getString("cart_items", null)
        return if (cartJson != null) {
            val type = object : TypeToken<List<CartItem>>() {}.type
            gson.fromJson(cartJson, type)
        } else {
            emptyList()
        }
    }

    private fun saveCartItems(items: List<CartItem>) {
        val cartJson = gson.toJson(items)
        prefs.edit().putString("cart_items", cartJson).apply()
        _cartItems.value = items
    }

    fun addItem(item: CartItem) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.uniqueKey == item.uniqueKey }

        if (existingItemIndex != -1) {
            val existingItem = currentItems[existingItemIndex]
            currentItems[existingItemIndex] = existingItem.copy(
                quantity = existingItem.quantity + item.quantity,
                totalPrice = existingItem.totalPrice + item.totalPrice
            )
        } else {
            currentItems.add(item)
        }
        saveCartItems(currentItems)
    }

    fun removeItem(item: CartItem) {
        val currentItems = _cartItems.value.toMutableList()
        currentItems.remove(item)
        saveCartItems(currentItems)
    }

    fun updateQuantity(item: CartItem, newQuantity: Int) {
        val currentItems = _cartItems.value.toMutableList()
        val index = currentItems.indexOf(item)
        if (index != -1) {
            val basePrice = item.totalPrice / item.quantity
            currentItems[index] = item.copy(
                quantity = newQuantity,
                totalPrice = basePrice * newQuantity
            )
            saveCartItems(currentItems)
        }
    }

    fun clearCart() {
        saveCartItems(emptyList())
    }
}