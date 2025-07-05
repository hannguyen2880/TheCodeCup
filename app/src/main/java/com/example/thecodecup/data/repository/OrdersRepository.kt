package com.example.thecodecup.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.thecodecup.data.model.Order
import com.example.thecodecup.data.model.OrderStatus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrdersRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("orders_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _orders = MutableStateFlow(loadOrders())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private fun loadOrders(): List<Order> {
        val ordersJson = prefs.getString("orders_list", null)
        return if (ordersJson != null) {
            val type = object : TypeToken<List<Order>>() {}.type
            gson.fromJson(ordersJson, type)
        } else {
            getDefaultOrders()
        }
    }

    private fun saveOrders(orders: List<Order>) {
        val ordersJson = gson.toJson(orders)
        prefs.edit().putString("orders_list", ordersJson).apply()
        _orders.value = orders
    }

    fun addOrder(order: Order) {
        val currentOrders = _orders.value.toMutableList()
        currentOrders.add(0, order)
        saveOrders(currentOrders)
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        val currentOrders = _orders.value.toMutableList()
        val index = currentOrders.indexOfFirst { it.id == orderId }
        if (index != -1) {
            currentOrders[index] = currentOrders[index].copy(status = newStatus)
            saveOrders(currentOrders)
        }
    }

    fun markOrderAsCompleted(orderId: String) {
        updateOrderStatus(orderId, OrderStatus.COMPLETED)
    }

    private fun getDefaultOrders(): List<Order> {
        return listOf(
            Order(
                id = "1",
                coffeeName = "Americano",
                date = "24 June",
                time = "12:30 PM",
                price = 3.00,
                address = "3 Addersion Court Chino Hills, H064824, United State",
                status = OrderStatus.ONGOING
            ),
            Order(
                id = "2",
                coffeeName = "Cafe Latte",
                date = "24 June",
                time = "11:45 AM",
                price = 3.50,
                address = "3 Addersion Court Chino Hills, H066824, United State",
                status = OrderStatus.ONGOING
            )
        )
    }
}