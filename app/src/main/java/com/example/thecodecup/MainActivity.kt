package com.example.thecodecup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.thecodecup.ui.theme.ThemeManager
import com.example.thecodecup.ui.viewmodel.CartViewModel
import com.example.thecodecup.ui.viewmodel.RewardsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val themeManager = remember { ThemeManager(context) }

            TheCodeCupTheme(themeManager = themeManager) {
                CoffeeApp(themeManager)
            }
        }
    }
}

@Composable
fun CoffeeApp(themeManager: ThemeManager) {
    val navController = rememberNavController()
    val cartViewModel: CartViewModel = viewModel()
    val rewardsViewModel: RewardsViewModel = viewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

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
            composable(Screen.Splash.route) {
                SplashScreen(navController)
            }

            composable(Screen.Home.route) {
                HomeScreen(navController)
            }

            composable(Screen.Rewards.route) {
                RewardsScreen(navController, rewardsViewModel)
            }

            composable(Screen.MyOrder.route) {
                MyOrderScreen(navController)
            }

            composable(Screen.MyCart.route) {
                MyCartScreen(navController, cartViewModel, rewardsViewModel)
            }

            composable(Screen.OrderSuccess.route) {
                OrderSuccessScreen(navController)
            }

            composable(Screen.Profile.route) {
                ProfileScreen(navController)
            }
            composable(Screen.Search.route) {
                SearchScreen(navController)
            }
            composable(
                route = Screen.CoffeeDetails.route,
                arguments = listOf(navArgument(Screen.CoffeeDetails.COFFEE_ID_ARG) {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val coffeeId = backStackEntry.arguments?.getString(Screen.CoffeeDetails.COFFEE_ID_ARG) ?: ""
                CoffeeDetailsScreen(navController, coffeeId, cartViewModel)
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

            composable(Screen.RedeemRewards.route) {
                RedeemRewardsScreen(navController, rewardsViewModel)
            }
        }
    }
}