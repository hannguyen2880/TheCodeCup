package com.example.thecodecup.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thecodecup.data.model.CartItem
import com.example.thecodecup.navigation.Screen
import com.example.thecodecup.ui.theme.CoffeeBrown
import com.example.thecodecup.ui.viewmodel.CartViewModel
import com.example.thecodecup.ui.viewmodel.RewardsViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCartScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    rewardsViewModel: RewardsViewModel
) {
    val cartItems = cartViewModel.cartItems
    val totalPrice = cartViewModel.getTotalPrice()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = "My Cart",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        if (cartItems.isEmpty()) {
            EmptyCartContent(
                modifier = Modifier.weight(1f),
                onContinueShopping = {
                    navController.navigate(Screen.Home.route)
                }
            )
        } else {
            // Cart Items List with Swipe to Dismiss
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(
                    items = cartItems,
                    key = { it.id }
                ) { item ->
                    SwipeToDismissCartItem(
                        item = item,
                        onDismiss = { cartViewModel.removeItem(item) },
                        onQuantityChange = { newQuantity ->
                            cartViewModel.updateQuantity(item, newQuantity)
                        }
                    )
                }
            }

            // Checkout Section
            CheckoutSection(
                totalPrice = totalPrice,
                onCheckout = {
                    // Clear cart and navigate to success
                    cartViewModel.clearCart()
                    navController.navigate(Screen.OrderSuccess.route)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissCartItem(
    item: CartItem,
    onDismiss: () -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    var isDismissed by remember { mutableStateOf(false) }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.StartToEnd,
                SwipeToDismissBoxValue.EndToStart -> {
                    isDismissed = true
                    onDismiss()
                    true
                }
                SwipeToDismissBoxValue.Settled -> false
            }
        }
    )

    AnimatedVisibility(
        visible = !isDismissed,
        exit = shrinkVertically()
    ) {
        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                DismissBackground(dismissState.dismissDirection)
            }
        ) {
            CartItemCard(
                item = item,
                onQuantityChange = onQuantityChange
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissDirection: SwipeToDismissBoxValue) {
    val color = when (dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Color.Red.copy(alpha = 0.8f)
        SwipeToDismissBoxValue.EndToStart -> Color.Red.copy(alpha = 0.8f)
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color, RoundedCornerShape(12.dp))
            .padding(16.dp),
        contentAlignment = when (dismissDirection) {
            SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
            SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
            else -> Alignment.Center
        }
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Coffee Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.coffee.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${item.customization.size.displayName} | ${item.customization.shotType.displayName}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Text(
                    text = if (item.isHot) "Hot" else "Cold",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$${String.format("%.2f", item.totalPrice)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoffeeBrown
                )
            }

            // Quantity Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = {
                        if (item.quantity > 1) {
                            onQuantityChange(item.quantity - 1)
                        }
                    },
                    enabled = item.quantity > 1,
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = if (item.quantity > 1) CoffeeBrown else Color.Gray.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(6.dp)
                        )
                ) {
                    Text(
                        text = "−",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = item.quantity.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.width(20.dp),
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = { onQuantityChange(item.quantity + 1) },
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = CoffeeBrown,
                            shape = RoundedCornerShape(6.dp)
                        )
                ) {
                    Text(
                        text = "+",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyCartContent(
    modifier: Modifier = Modifier,
    onContinueShopping: () -> Unit
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
                text = "🛒",
                fontSize = 64.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your cart is empty",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Add some delicious coffee to get started",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onContinueShopping,
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

@Composable
fun CheckoutSection(
    totalPrice: Double,
    onCheckout: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Amount",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )

                Text(
                    text = "$${String.format("%.2f", totalPrice)}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onCheckout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CoffeeBrown
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Checkout",
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}