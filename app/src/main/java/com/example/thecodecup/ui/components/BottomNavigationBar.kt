package com.example.thecodecup.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.thecodecup.R
import com.example.thecodecup.navigation.Screen
import com.example.thecodecup.ui.theme.CoffeeBrown

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", R.drawable.ic_home, Screen.Home.route),
        BottomNavItem("Rewards", R.drawable.ic_rewards, Screen.Rewards.route),
        BottomNavItem("My Order", R.drawable.ic_orders, Screen.MyOrder.route)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        contentColor = CoffeeBrown
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (currentRoute == item.route) CoffeeBrown else Color.Gray
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CoffeeBrown,
                    unselectedIconColor = Color.Gray,
                    indicatorColor = CoffeeBrown.copy(alpha = 0.1f)
                )
            )
        }
    }
}

data class BottomNavItem(
    val title: String,
    val icon: Int,
    val route: String
)