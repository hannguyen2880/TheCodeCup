package com.example.thecodecup.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thecodecup.data.model.Order
import com.example.thecodecup.data.model.OrderStatus
import com.example.thecodecup.data.repository.OrdersRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class OrdersViewModel(private val ordersRepository: OrdersRepository) : ViewModel() {

    val orders: StateFlow<List<Order>> = ordersRepository.orders
    val ongoingOrders: StateFlow<List<Order>> = orders.map { orderList ->
        orderList.filter { it.status == OrderStatus.ONGOING }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val historyOrders: StateFlow<List<Order>> = orders.map { orderList ->
        orderList.filter { it.status == OrderStatus.COMPLETED }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun markOrderAsCompleted(orderId: String) {
        viewModelScope.launch {
            ordersRepository.markOrderAsCompleted(orderId)
        }
    }

    fun addOrder(order: Order) {
        viewModelScope.launch {
            ordersRepository.addOrder(order)
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        viewModelScope.launch {
            ordersRepository.updateOrderStatus(orderId, newStatus)
        }
    }
}