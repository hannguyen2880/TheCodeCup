// ui/screens/CoffeeDetailsScreen.kt
package com.example.thecodecup.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thecodecup.R
import com.example.thecodecup.data.model.Coffee
import com.example.thecodecup.data.model.CoffeeCustomization
import com.example.thecodecup.data.model.CoffeeSize
import com.example.thecodecup.data.model.ShotType
import com.example.thecodecup.data.model.MilkType
import com.example.thecodecup.data.model.SweetnessLevel
import com.example.thecodecup.navigation.Screen
import com.example.thecodecup.ui.theme.CoffeeBrown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeDetailsScreen(
    navController: NavController,
    coffeeId: String
) {
    // Coffee details based on ID with model classes 
    val coffee = remember(coffeeId) {
        when (coffeeId) {
            "americano" -> Coffee(
                id = "americano",
                name = "Americano",
                price = 3.00,
                imageRes = R.drawable.americano,
                description = "Rich and bold espresso with hot water for a smooth, full-bodied taste",
                category = "Hot Coffee",
                rating = 4.5f,
                isPopular = true,
                ingredients = listOf("Espresso", "Hot Water")
            )
            "cappuccino" -> Coffee(
                id = "cappuccino",
                name = "Cappuccino",
                price = 3.50,
                imageRes = R.drawable.cappuccino,
                description = "Perfect balance of espresso, steamed milk, and velvety milk foam",
                category = "Hot Coffee",
                rating = 4.7f,
                isPopular = true,
                ingredients = listOf("Espresso", "Steamed Milk", "Milk Foam")
            )
            "mocha" -> Coffee(
                id = "mocha",
                name = "Mocha",
                price = 4.00,
                imageRes = R.drawable.mocha,
                description = "Indulgent blend of rich chocolate and premium espresso",
                category = "Specialty",
                rating = 4.6f,
                ingredients = listOf("Espresso", "Chocolate", "Steamed Milk", "Whipped Cream")
            )
            "flat_white" -> Coffee(
                id = "flat_white",
                name = "Flat White",
                price = 3.75,
                imageRes = R.drawable.flat_white,
                description = "Smooth espresso with perfectly textured microfoam milk",
                category = "Hot Coffee",
                rating = 4.4f,
                ingredients = listOf("Double Espresso", "Microfoam Milk")
            )
            else -> Coffee(
                id = "americano",
                name = "Americano",
                price = 3.00,
                imageRes = R.drawable.americano,
                description = "Rich and bold espresso with hot water",
                rating = 4.5f
            )
        }
    }

    // Customization states with model enums 
    var quantity by remember { mutableStateOf(1) }
    var customization by remember {
        mutableStateOf(
            CoffeeCustomization(
                shotType = ShotType.SINGLE,
                size = CoffeeSize.MEDIUM,
                hasIce = false,
                milkType = MilkType.REGULAR,
                sweetness = SweetnessLevel.NORMAL
            )
        )
    }
    var isFavorite by remember { mutableStateOf(false) }

    // Dynamic price calculation with customization 
    val totalPrice by remember(quantity, customization) {
        derivedStateOf {
            var price = coffee.price
            price += customization.shotType.priceModifier
            price += customization.size.priceModifier
            price * quantity
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with Navigation 
        TopAppBar(
            title = {
                Text(
                    text = "Details",
                    fontWeight = FontWeight.Medium
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                // Favorite Toggle 
                IconButton(onClick = { isFavorite = !isFavorite }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }

                // Cart Preview Icon 
                IconButton(onClick = { navController.navigate(Screen.MyCart.route) }) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Cart"
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Coffee Image and Basic Info 
            CoffeeImageSection(coffee = coffee)

            Spacer(modifier = Modifier.height(24.dp))

            // Coffee Details Section 
            CoffeeInfoSection(coffee = coffee)

            Spacer(modifier = Modifier.height(24.dp))

            // Quantity Selector 
            QuantitySection(
                quantity = quantity,
                onQuantityChange = { quantity = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Customization Options 
            CustomizationSection(
                customization = customization,
                onCustomizationChange = { customization = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Add to Cart Section 
            AddToCartSection(
                totalPrice = totalPrice,
                onAddToCart = {
                    // Add to Cart Logic - Navigate to Cart
                    navController.navigate(Screen.MyCart.route)
                }
            )
        }
    }
}

@Composable
fun CoffeeImageSection(coffee: Coffee) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Coffee Image với kích thước nhỏ hơn và sắc nét hơn
            Card(
                modifier = Modifier.size(180.dp), // Giảm từ 250dp xuống 180dp
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Image(
                    painter = painterResource(coffee.imageRes),
                    contentDescription = coffee.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Fit // Thay đổi từ Crop sang Fit để tránh mất nét
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Popular Badge
            if (coffee.isPopular) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = CoffeeBrown.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⭐",
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Popular",
                            color = CoffeeBrown,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CoffeeInfoSection(coffee: Coffee) {
    Column {
        // Coffee Name and Rating 
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = coffee.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = coffee.category,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Rating Section 
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = coffee.rating.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description 
        Text(
            text = coffee.description,
            fontSize = 16.sp,
            color = Color.Gray,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Ingredients 
        if (coffee.ingredients.isNotEmpty()) {
            Text(
                text = "Ingredients:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                coffee.ingredients.take(3).forEach { ingredient ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF5F5F5)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = ingredient,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuantitySection(
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Column {
        Text(
            text = "Quantity",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Decrease Button 
            IconButton(
                onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
                enabled = quantity > 1,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (quantity > 1) CoffeeBrown else Color.Gray.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Text(
                    text = "−",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Quantity Display 
            Text(
                text = quantity.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center
            )

// Increase Button
            IconButton(
                onClick = { onQuantityChange(quantity + 1) },
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = CoffeeBrown,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Text(
                    text = "+",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun CustomizationSection(
    customization: CoffeeCustomization,
    onCustomizationChange: (CoffeeCustomization) -> Unit
) {
    Column {
        Text(
            text = "Customization",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Shot Type Selection
        CustomizationGroup(
            title = "Shot",
            options = ShotType.values().map { it.displayName },
            selectedIndex = customization.shotType.ordinal,
            onSelectionChange = { index ->
                onCustomizationChange(
                    customization.copy(shotType = ShotType.values()[index])
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Size Selection
        CustomizationGroup(
            title = "Select",
            options = CoffeeSize.values().map { it.displayName },
            selectedIndex = customization.size.ordinal,
            onSelectionChange = { index ->
                onCustomizationChange(
                    customization.copy(size = CoffeeSize.values()[index])
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Ice Option
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ice",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Switch(
                checked = customization.hasIce,
                onCheckedChange = { hasIce ->
                    onCustomizationChange(customization.copy(hasIce = hasIce))
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = CoffeeBrown,
                    checkedTrackColor = CoffeeBrown.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@Composable
fun CustomizationGroup(
    title: String,
    options: List<String>,
    selectedIndex: Int,
    onSelectionChange: (Int) -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            options.forEachIndexed { index, option ->
                Card(
                    modifier = Modifier
                        .selectable(
                            selected = selectedIndex == index,
                            onClick = { onSelectionChange(index) }
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedIndex == index)
                            CoffeeBrown else Color(0xFFF5F5F5)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = option,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = if (selectedIndex == index) Color.White else Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun AddToCartSection(
    totalPrice: Double,
    onAddToCart: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Price Display
        Column {
            Text(
                text = "Price",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "$${String.format("%.2f", totalPrice)}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = CoffeeBrown
            )
        }

        // Add to Cart Button
        Button(
            onClick = onAddToCart,
            colors = ButtonDefaults.buttonColors(
                containerColor = CoffeeBrown
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(56.dp)
        ) {
            Text(
                text = "Add to Cart",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoffeeDetailsScreenPreview() {
    CoffeeDetailsScreen(
        navController = rememberNavController(),
        coffeeId = "americano"
    )
}