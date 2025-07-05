package com.example.thecodecup.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.thecodecup.data.model.Order
import com.example.thecodecup.data.model.OrderStatus

class OrdersViewModel : ViewModel() {
    private val _orders = mutableStateListOf(
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
        ),
        Order(
            id = "3",
            coffeeName = "Cappuccino",
            date = "23 June",
            time = "2:15 PM",
            price = 3.25,
            address = "3 Addersion Court Chino Hills, H066824, United State",
            status = OrderStatus.ONGOING
        ),
        Order(
            id = "4",
            coffeeName = "Flat White",
            date = "22 June",
            time = "10:30 AM",
            price = 3.75,
            address = "3 Addersion Court Chino Hills, H058824, United State",
            status = OrderStatus.COMPLETED
        ),
        Order(
            id = "5",
            coffeeName = "Mocha",
            date = "21 June",
            time = "3:20 PM",
            price = 4.00,
            address = "3 Addersion Court Chino Hills, H058824, United State",
            status = OrderStatus.COMPLETED
        )
    )

    val orders: List<Order> = _orders

    val ongoingOrders: List<Order>
        get() = _orders.filter { it.status == OrderStatus.ONGOING }

    val historyOrders: List<Order>
        get() = _orders.filter { it.status == OrderStatus.COMPLETED }

    fun markOrderAsCompleted(orderId: String) {
        val index = _orders.indexOfFirst { it.id == orderId }
        if (index != -1) {
            _orders[index] = _orders[index].copy(status = OrderStatus.COMPLETED)
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        val index = _orders.indexOfFirst { it.id == orderId }
        if (index != -1) {
            _orders[index] = _orders[index].copy(status = newStatus)
        }
    }
    fun addOrder(order: Order) {
        _orders.add(0, order)
    }
}