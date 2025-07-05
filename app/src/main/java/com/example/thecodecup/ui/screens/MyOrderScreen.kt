package com.example.thecodecup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.thecodecup.ui.viewmodel.LoyaltyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrderScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    rewardsViewModel: RewardsViewModel,
    ordersViewModel: OrdersViewModel,
    loyaltyViewModel: LoyaltyViewModel
) {
    val ongoingOrders by ordersViewModel.ongoingOrders.collectAsState()
    val historyOrders by ordersViewModel.historyOrders.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Ongoing", "History")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = "My Orders",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
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

        // Tab Row
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = index },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedTab == index)
                                CoffeeBrown else Color.Transparent
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
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
                modifier = Modifier.weight(1f),
                onStartShopping = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(
                    items = currentOrders,
                    key = { it.id }
                ) { order ->
                    OrderCard(
                        order = order,
                        isOngoing = selectedTab == 0,
                        onOrderClick = { clickedOrder ->
                            navController.navigate(Screen.OrderDetails.createRoute(clickedOrder.id))
                        },
                        onMarkCompleted = { orderToComplete ->
                            ordersViewModel.markOrderAsCompleted(orderToComplete.id)

                            // Add loyalty rewards
                            val bonusPoints = (orderToComplete.price * 0.1).toInt()
                            rewardsViewModel.addPointsFromCompletedOrder(orderToComplete.id, bonusPoints)
                            loyaltyViewModel.addStampFromCompletedOrder()
                        },
                        onReorder = { orderToReorder ->
                            // Navigate back to home or show reorder confirmation
                            navController.navigate(Screen.Home.route)
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
    isOngoing: Boolean,
    onOrderClick: (Order) -> Unit,
    onMarkCompleted: (Order) -> Unit,
    onReorder: (Order) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOrderClick(order) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Order #${order.id.takeLast(6)}",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${order.date} | ${order.time}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                // Status Badge
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when (order.status) {
                            OrderStatus.ONGOING -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                            OrderStatus.COMPLETED -> Color(0xFF2196F3).copy(alpha = 0.1f)
                            else -> CoffeeBrown.copy(alpha = 0.1f)
                        }
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = order.status.displayName,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = when (order.status) {
                            OrderStatus.ONGOING -> Color(0xFF4CAF50)
                            OrderStatus.COMPLETED -> Color(0xFF2196F3)
                            else -> CoffeeBrown
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Coffee Name and Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.coffeeName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "$${String.format("%.2f", order.price)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoffeeBrown
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Address
            Text(
                text = order.address,
                fontSize = 13.sp,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Action Buttons
            if (isOngoing && order.status == OrderStatus.ONGOING) {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onMarkCompleted(order) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Mark as Completed",
                        fontSize = 14.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else if (!isOngoing && order.status == OrderStatus.COMPLETED) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { onReorder(order) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = CoffeeBrown
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Reorder",
                        fontSize = 14.sp,
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
    modifier: Modifier = Modifier,
    onStartShopping: () -> Unit
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
                text = if (isOngoing) "📋" else "📝",
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
                    "Your active orders will appear here"
                else
                    "Your completed orders will appear here",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            if (isOngoing) {
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onStartShopping,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CoffeeBrown
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Start Shopping",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyOrderScreenPreview() {
    MyOrderScreen(
        navController = rememberNavController(),
        cartViewModel = CartViewModel(cartRepository = TODO()),
        rewardsViewModel = RewardsViewModel(),
        ordersViewModel = OrdersViewModel(ordersRepository = TODO()),
        loyaltyViewModel = LoyaltyViewModel()
    )
}