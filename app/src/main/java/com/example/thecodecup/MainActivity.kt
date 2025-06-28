package com.example.thecodecup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.thecodecup.navigation.Screen
import com.example.thecodecup.ui.screens.*
import com.example.thecodecup.ui.components.BottomNavigationBar
import com.example.thecodecup.ui.theme.TheCodeCupTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheCodeCupTheme {
                CoffeeApp()
            }
        }
    }
}

@Composable
fun CoffeeApp() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            // only show tool bar if we are on one of the main screens
            if (currentDestination?.route in listOf(
                    Screen.Home.route,
                    Screen.Rewards.route,
                    Screen.MyOrder.route
                )) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Splash Screen
            composable(Screen.Splash.route) {
                SplashScreen(navController)
            }

            // Main Bottom Navigation Screens (3 screens)
            composable(Screen.Home.route) {
                HomeScreen(navController)
            }

            composable(Screen.Rewards.route) {
                RewardsScreen(navController)
            }

            composable(Screen.MyOrder.route) {
                MyOrderScreen(navController)
            }

            // Additional Screens (accessed from header)
            composable(Screen.MyCart.route) {
                MyCartScreen(navController)
            }

            composable(Screen.Profile.route) {
                ProfileScreen(navController)
            }

            // Coffee Details Screen with Arguments
            composable(
                route = Screen.CoffeeDetails.route,
                arguments = listOf(navArgument(Screen.CoffeeDetails.COFFEE_ID_ARG) {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val coffeeId = backStackEntry.arguments?.getString(Screen.CoffeeDetails.COFFEE_ID_ARG) ?: ""
                CoffeeDetailsScreen(navController, coffeeId)
            }

            composable(
                route = Screen.OrderDetails.route,
                arguments = listOf(navArgument(Screen.OrderDetails.ORDER_ID_ARG) {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString(Screen.OrderDetails.ORDER_ID_ARG) ?: ""
                OrderDetailsScreen(navController, orderId)
            }

            // Success Screen
            composable(Screen.OrderSuccess.route) {
                OrderSuccessScreen(navController)
            }

            composable(Screen.RedeemRewards.route) {
                RedeemRewardsScreen(navController)
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(3000) // 3 seconds
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.splash_background),
            contentDescription = "Splash Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Content over background
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.coffee_logo),
                contentDescription = "Coffee Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Ordinary Coffee Shop",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}