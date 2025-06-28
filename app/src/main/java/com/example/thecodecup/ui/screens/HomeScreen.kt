// ui/screens/HomeScreen.kt
package com.example.thecodecup.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.example.thecodecup.data.model.LoyaltyCard
import com.example.thecodecup.navigation.Screen
import com.example.thecodecup.ui.theme.CoffeeBrown
import com.example.thecodecup.ui.theme.CoffeeWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    // Coffee data with model classes
    val coffeeList = remember {
        listOf(
            Coffee(
                id = "americano",
                name = "Americano",
                price = 3.00,
                imageRes = R.drawable.americano,
                description = "Rich and bold espresso with hot water",
                category = "Hot Coffee",
                rating = 4.5f,
                isPopular = true,
                ingredients = listOf("Espresso", "Hot Water")
            ),
            Coffee(
                id = "cappuccino",
                name = "Cappuccino",
                price = 3.50,
                imageRes = R.drawable.cappuccino,
                description = "Espresso with steamed milk and foam",
                category = "Hot Coffee",
                rating = 4.7f,
                isPopular = true,
                ingredients = listOf("Espresso", "Steamed Milk", "Milk Foam")
            ),
            Coffee(
                id = "mocha",
                name = "Mocha",
                price = 4.00,
                imageRes = R.drawable.mocha,
                description = "Chocolate and espresso perfect blend",
                category = "Specialty",
                rating = 4.6f,
                ingredients = listOf("Espresso", "Chocolate", "Steamed Milk")
            ),
            Coffee(
                id = "flat_white",
                name = "Flat White",
                price = 3.75,
                imageRes = R.drawable.flat_white,
                description = "Smooth espresso with microfoam milk",
                category = "Hot Coffee",
                rating = 4.4f,
                ingredients = listOf("Espresso", "Microfoam Milk")
            )
        )
    }

    // Loyalty card with model
    val loyaltyCard = remember {
        LoyaltyCard(currentStamps = 4, maxStamps = 8, totalPoints = 120)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Section
        item {
            HeaderSection(
                userName = "Anderson",
                onCartClick = { navController.navigate(Screen.MyCart.route) },
                onProfileClick = { navController.navigate(Screen.Profile.route) }
            )
        }

        // Loyalty Card Section
        item {
            LoyaltyCardSection(
                loyaltyCard = loyaltyCard,
                onCardClick = { navController.navigate(Screen.Rewards.route) }
            )
        }

        // Coffee Selection Section
        item {
            CoffeeSelectionSection(
                coffeeList = coffeeList,
                onCoffeeClick = { coffee ->
                    navController.navigate(Screen.CoffeeDetails.createRoute(coffee.id))
                }
            )
        }
    }
}

@Composable
fun HeaderSection(
    userName: String,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Greeting Text
        Column {
            Text(
                text = "Good morning",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = userName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // Action Icons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onCartClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "My Cart",
                    tint = Color.Black
                )
            }

            IconButton(
                onClick = onProfileClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun LoyaltyCardSection(
    loyaltyCard: LoyaltyCard,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4A5568) // Dark blue-gray from design
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Card Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Loyalty card",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${loyaltyCard.currentStamps} / ${loyaltyCard.maxStamps}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Coffee Cup Stamps Progress
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(loyaltyCard.maxStamps) { index ->
                        CoffeeStampIcon(
                            isActive = index < loyaltyCard.currentStamps,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CoffeeStampIcon(
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = if (isActive) Color.White else Color.White.copy(alpha = 0.3f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        // Using PNG coffee cup icon
        Icon(
            painter = painterResource(R.drawable.ic_coffee_cup), // Your PNG file
            contentDescription = "Coffee Cup",
            tint = if (isActive) CoffeeBrown else Color.Gray.copy(alpha = 0.5f),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun CoffeeSelectionSection(
    coffeeList: List<Coffee>,
    onCoffeeClick: (Coffee) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4A5568) // Dark blue-gray matching loyalty card
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Section Title
            Text(
                text = "Choose your coffee",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Coffee Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(320.dp) // Fixed height to prevent nested scrolling
            ) {
                items(coffeeList) { coffee ->
                    CoffeeCard(
                        coffee = coffee,
                        onClick = { onCoffeeClick(coffee) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CoffeeCard(
    coffee: Coffee,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // Square cards
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Coffee Image
            Image(
                painter = painterResource(coffee.imageRes),
                contentDescription = coffee.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Coffee Name
            Text(
                text = coffee.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = Color.Black,
                maxLines = 1
            )

            // Coffee Price
            Text(
                text = "$${String.format("%.2f", coffee.price)}",
                fontSize = 12.sp,
                color = CoffeeBrown,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}