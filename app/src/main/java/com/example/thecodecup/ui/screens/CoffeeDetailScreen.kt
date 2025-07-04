package com.example.thecodecup.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.example.thecodecup.ui.viewmodel.CartViewModel
import com.example.thecodecup.data.model.CartItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeDetailsScreen(
    navController: NavController,
    coffeeId: String,
    cartViewModel: CartViewModel
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

    // Customization states
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
    var isHot by remember { mutableStateOf(true) }
    var iceLevel by remember { mutableStateOf(3) } // 1, 2, 3 ice cubes

    // Dynamic price calculation
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
            // Coffee Image
            CoffeeImageSection(coffee = coffee)

            Spacer(modifier = Modifier.height(24.dp))

            // Coffee Name and Quantity (same row)
            CoffeeInfoSection(
                coffee = coffee,
                quantity = quantity,
                onQuantityChange = { quantity = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Customization Options
            CustomizationSection(
                customization = customization,
                onCustomizationChange = { customization = it },
                isHot = isHot,
                onHotColdChange = { isHot = it },
                iceLevel = iceLevel,
                onIceLevelChange = { iceLevel = it }
            )

            Spacer(modifier = Modifier.height(32.dp))
            AddToCartSection(
                coffee = coffee,
                customization = customization,
                quantity = quantity,
                isHot = isHot,
                iceLevel = iceLevel,
                totalPrice = totalPrice,
                cartViewModel = cartViewModel,
                onAddToCart = {
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
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.size(140.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Image(
                    painter = painterResource(coffee.imageRes),
                    contentDescription = coffee.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Fit
                )
            }

            //Spacer(modifier = Modifier.height(12.dp))

            if (coffee.isPopular) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = CoffeeBrown.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⭐",
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Popular",
                            color = CoffeeBrown,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CoffeeInfoSection(
    coffee: Coffee,
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Coffee Name only
        Text(
            text = coffee.name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        // Quantity Selector
        QuantitySelector(
            quantity = quantity,
            onQuantityChange = onQuantityChange
        )
    }
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconButton(
            onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
            enabled = quantity > 1,
            modifier = Modifier
                .size(36.dp)
                .background(
                    color = if (quantity > 1) CoffeeBrown else Color.Gray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Text(
                text = "−",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Text(
            text = quantity.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.width(32.dp),
            textAlign = TextAlign.Center
        )

        IconButton(
            onClick = { onQuantityChange(quantity + 1) },
            modifier = Modifier
                .size(36.dp)
                .background(
                    color = CoffeeBrown,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Text(
                text = "+",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun CustomizationSection(
    customization: CoffeeCustomization,
    onCustomizationChange: (CoffeeCustomization) -> Unit,
    isHot: Boolean,
    onHotColdChange: (Boolean) -> Unit,
    iceLevel: Int,
    onIceLevelChange: (Int) -> Unit
) {
    Column {
        // Hot/Cold Selection with Icons
        HotColdSelectionGroup(
            title = "Select",
            isHot = isHot,
            onSelectionChange = onHotColdChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Size Selection
        CustomizationGroup(
            title = "Size",
            options = CoffeeSize.values().map { it.displayName },
            selectedIndex = customization.size.ordinal,
            onSelectionChange = { index ->
                onCustomizationChange(
                    customization.copy(size = CoffeeSize.values()[index])
                )
            }
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

        // Ice Level Selection
        IceLevelSelectionGroup(
            title = "Ice",
            iceLevel = iceLevel,
            onIceLevelChange = onIceLevelChange
        )
    }
}

@Composable
fun HotColdSelectionGroup(
    title: String,
    isHot: Boolean,
    onSelectionChange: (Boolean) -> Unit
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
            // Hot Option
            Card(
                modifier = Modifier
                    .selectable(
                        selected = isHot,
                        onClick = { onSelectionChange(true) }
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isHot) CoffeeBrown else Color(0xFFF5F5F5)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_hot),
                        contentDescription = "Hot",
                        tint = if (isHot) Color.White else Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Hot",
                        color = if (isHot) Color.White else Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Cold Option
            Card(
                modifier = Modifier
                    .selectable(
                        selected = !isHot,
                        onClick = { onSelectionChange(false) }
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (!isHot) CoffeeBrown else Color(0xFFF5F5F5)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_cold),
                        contentDescription = "Cold",
                        tint = if (!isHot) Color.White else Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Cold",
                        color = if (!isHot) Color.White else Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun IceLevelSelectionGroup(
    title: String,
    iceLevel: Int,
    onIceLevelChange: (Int) -> Unit
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
            // Light Ice (1 cube)
            Card(
                modifier = Modifier
                    .selectable(
                        selected = iceLevel == 1,
                        onClick = { onIceLevelChange(1) }
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (iceLevel == 1) CoffeeBrown else Color(0xFFF5F5F5)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_ice_cube),
                        contentDescription = "Light Ice",
                        tint = if (iceLevel == 1) Color.White else Color.Black,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            // Medium Ice (2 cubes)
            Card(
                modifier = Modifier
                    .selectable(
                        selected = iceLevel == 2,
                        onClick = { onIceLevelChange(2) }
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (iceLevel == 2) CoffeeBrown else Color(0xFFF5F5F5)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_ice_cube),
                        contentDescription = "Medium Ice",
                        tint = if (iceLevel == 2) Color.White else Color.Black,
                        modifier = Modifier.size(12.dp)
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_ice_cube),
                        contentDescription = "Medium Ice",
                        tint = if (iceLevel == 2) Color.White else Color.Black,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            // Normal Ice (3 cubes)
            Card(
                modifier = Modifier
                    .selectable(
                        selected = iceLevel == 3,
                        onClick = { onIceLevelChange(3) }
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (iceLevel == 3) CoffeeBrown else Color(0xFFF5F5F5)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_ice_cube),
                        contentDescription = "Normal Ice",
                        tint = if (iceLevel == 3) Color.White else Color.Black,
                        modifier = Modifier.size(12.dp)
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_ice_cube),
                        contentDescription = "Normal Ice",
                        tint = if (iceLevel == 3) Color.White else Color.Black,
                        modifier = Modifier.size(12.dp)
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_ice_cube),
                        contentDescription = "Normal Ice",
                        tint = if (iceLevel == 3) Color.White else Color.Black,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
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
    coffee: Coffee,
    customization: CoffeeCustomization,
    quantity: Int,
    isHot: Boolean,
    iceLevel: Int,
    totalPrice: Double,
    cartViewModel: CartViewModel,
    onAddToCart: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
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

        Button(
            onClick = {
                val cartItem = CartItem(
                    id = "${coffee.id}_${System.currentTimeMillis()}",
                    coffee = coffee,
                    customization = customization,
                    quantity = quantity,
                    totalPrice = totalPrice,
                    isHot = isHot,
                    iceLevel = iceLevel
                )
                cartViewModel.addItem(cartItem)
                onAddToCart()
            },
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