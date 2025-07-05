package com.example.thecodecup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thecodecup.data.model.Order
import com.example.thecodecup.data.model.OrderStatus
import com.example.thecodecup.navigation.Screen
import com.example.thecodecup.ui.theme.CoffeeBrown
import com.example.thecodecup.ui.viewmodel.CartViewModel
import com.example.thecodecup.ui.viewmodel.RewardsViewModel
import com.example.thecodecup.ui.viewmodel.OrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrderScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    rewardsViewModel: RewardsViewModel,
    ordersViewModel: OrdersViewModel
) {
    // Filter orders based on status
    val ongoingOrders = ordersViewModel.ongoingOrders
    val historyOrders = ordersViewModel.historyOrders

    // Tab state management
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("On going", "History")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Custom Header with Navigation
        TopAppBar(
            title = {
                Box(Modifier.fillMaxWidth()) {
                    Card(
                        modifier = Modifier.align(Alignment.Center),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF324A59).copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "My Order",
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF324A59)
                            )
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Custom Tab Row
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = index },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedTab == index)
                                Color(0xFF324A59) else Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == index) Color.White else Color.Gray
                        )
                    }
                }
            }
        }

        // Orders Content
        val currentOrders = if (selectedTab == 0) ongoingOrders else historyOrders

        if (currentOrders.isEmpty()) {
            EmptyOrdersState(
                isOngoing = selectedTab == 0,
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(
                    items = currentOrders,
                    key = { it.id }
                ) { order ->
                    OrderCard(
                        order = order,
                        onOrderClick = { clickedOrder ->
                            navController.navigate(Screen.OrderDetails.createRoute(clickedOrder.id))
                        },
                        onMarkCompleted = { orderToComplete ->
                            ordersViewModel.markOrderAsCompleted(orderToComplete.id)

                            // Add bonus points when completed
                            val bonusPoints = (orderToComplete.price * 0.1).toInt()
                            rewardsViewModel.addPointsFromCompletedOrder(orderToComplete.id, bonusPoints)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    order: Order,
    onOrderClick: (Order) -> Unit,
    onMarkCompleted: (Order) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOrderClick(order) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header Row: Date/Time and Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${order.date} | ${order.time}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "$${String.format("%.2f", order.price)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF324A59)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Coffee Name with Status Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Status Dot
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = when (order.status) {
                                    OrderStatus.ONGOING -> Color(0xFF4CAF50)
                                    OrderStatus.COMPLETED -> Color.Gray
                                    else -> CoffeeBrown
                                },
                                shape = RoundedCornerShape(6.dp)
                            )
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = order.coffeeName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                // Status Badge
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when (order.status) {
                            OrderStatus.ONGOING -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                            OrderStatus.COMPLETED -> Color.Gray.copy(alpha = 0.1f)
                            else -> CoffeeBrown.copy(alpha = 0.1f)
                        }
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = order.status.displayName,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = when (order.status) {
                            OrderStatus.ONGOING -> Color(0xFF4CAF50)
                            OrderStatus.COMPLETED -> Color.Gray
                            else -> CoffeeBrown
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Address
            Text(
                text = order.address,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 24.dp)
            )

            // Action Button (only for ongoing orders)
            if (order.status == OrderStatus.ONGOING) {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onMarkCompleted(order) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF324A59)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Mark as Completed",
                        fontSize = 14.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyOrdersState(
    isOngoing: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "📋",
                fontSize = 64.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isOngoing) "No ongoing orders" else "No order history",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isOngoing)
                    "Your current orders will appear here"
                else
                    "Your completed orders will appear here",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}