package com.example.thecodecup.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

sealed class Screen(val route: String) {
    // Main Screens - Bottom Navigation
    object Splash : Screen(route = "splash_screen")
    object Home : Screen(route = "home_screen")
    object MyCart : Screen(route = "mycart_screen")
    object Profile : Screen(route = "profile_screen")
    object Rewards : Screen(route = "rewards_screen")
    object MyOrder : Screen(route = "myorder_screen")

    // Detail Screens with Arguments
    object CoffeeDetails : Screen(route = "coffee_details/{coffee_id}") {
        fun createRoute(coffeeId: String) = "coffee_details/$coffeeId"
    }

    object OrderDetails : Screen(route = "order_details/{order_id}") {
        fun createRoute(orderId: String) = "order_details/$orderId"
    }

    // Success & Status Screens
    object OrderSuccess : Screen(route = "order_success_screen")
    object RedeemRewards : Screen(route = "redeem_rewards_screen")

    // Additional Screens
    object Search : Screen(route = "search_screen")
    object Notifications : Screen(route = "notifications_screen")
    object Settings : Screen(route = "settings_screen")
    object EditProfile : Screen(route = "edit_profile_screen")
    object PaymentMethods : Screen(route = "payment_methods_screen")
    object OrderHistory : Screen(route = "order_history_screen")
}

// Bottom Navigation Items
sealed class BottomNavItem(
    var title: String,
    var icon: androidx.compose.ui.graphics.vector.ImageVector,
    var route: String
) {
    object Home : BottomNavItem(
        title = "Home",
        icon = Icons.Filled.Home,
        route = Screen.Home.route
    )

    object MyCart : BottomNavItem(
        title = "My Cart",
        icon = Icons.Filled.ShoppingCart,
        route = Screen.MyCart.route
    )

    object Rewards : BottomNavItem(
        title = "Rewards",
        icon = Icons.Filled.Star,
        route = Screen.Rewards.route
    )

    object MyOrder : BottomNavItem(
        title = "My Order",
        icon = Icons.Filled.List,
        route = Screen.MyOrder.route
    )

    object Profile : BottomNavItem(
        title = "Profile",
        icon = Icons.Filled.Person,
        route = Screen.Profile.route
    )
}

// Navigation Arguments Helper
object NavigationArgs {
    const val COFFEE_ID = "coffee_id"
    const val ORDER_ID = "order_id"
    const val USER_ID = "user_id"
    const val REWARD_ID = "reward_id"
}