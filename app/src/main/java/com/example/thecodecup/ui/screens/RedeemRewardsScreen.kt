package com.example.thecodecup.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.thecodecup.data.model.RewardItem
import com.example.thecodecup.ui.theme.CoffeeBrown
import com.example.thecodecup.ui.viewmodel.CartViewModel
import com.example.thecodecup.ui.viewmodel.OrdersViewModel
import com.example.thecodecup.ui.viewmodel.RewardsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedeemRewardsScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    rewardsViewModel: RewardsViewModel,
    ordersViewModel: OrdersViewModel
) {
    val availableRewards = remember {
        listOf(
            RewardItem(
                id = "1",
                coffeeName = "Americano",
                coffeeImage = R.drawable.americano,
                pointsRequired = 30,
                validUntil = "Valid until 04.07.23"
            ),
            RewardItem(
                id = "2",
                coffeeName = "Cappuccino",
                coffeeImage = R.drawable.cappuccino,
                pointsRequired = 35,
                validUntil = "Valid until 04.07.23"
            ),
            RewardItem(
                id = "3",
                coffeeName = "Mocha",
                coffeeImage = R.drawable.mocha,
                pointsRequired = 40,
                validUntil = "Valid until 04.07.23"
            ),
            RewardItem(
                id = "4",
                coffeeName = "Flat White",
                coffeeImage = R.drawable.flat_white,
                pointsRequired = 35,
                validUntil = "Valid until 04.07.23"
            )
        )
    }

    val userPoints = rewardsViewModel.userPoints.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = "Redeem",
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
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Points Balance
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = CoffeeBrown
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Your Points",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = "${userPoints.value}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Available Rewards
        Text(
            text = "Available Rewards",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(availableRewards) { reward ->
                RewardItemCard(
                    reward = reward,
                    userPoints = userPoints.value,
                    onRedeemClick = { rewardItem ->
                        if (userPoints.value >= rewardItem.pointsRequired) {
                            rewardsViewModel.redeemReward(rewardItem)
                            navController.navigateUp()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun RewardItemCard(
    reward: RewardItem,
    userPoints: Int,
    onRedeemClick: (RewardItem) -> Unit
) {
    val canRedeem = userPoints >= reward.pointsRequired

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Coffee Image and Info
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(reward.coffeeImage),
                    contentDescription = reward.coffeeName,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = reward.coffeeName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = reward.validUntil,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // Points and Redeem Button
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${reward.pointsRequired} pts",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoffeeBrown
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onRedeemClick(reward) },
                    enabled = canRedeem,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (canRedeem) CoffeeBrown else Color.Gray,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (canRedeem) "Redeem" else "Not enough",
                        color = if (canRedeem) Color.White else Color.LightGray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}