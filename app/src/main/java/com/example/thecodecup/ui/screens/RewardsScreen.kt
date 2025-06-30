package com.example.thecodecup.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thecodecup.R
import com.example.thecodecup.navigation.Screen
import com.example.thecodecup.ui.theme.CoffeeBrown
import com.example.thecodecup.ui.viewmodel.RewardItem
import com.example.thecodecup.ui.viewmodel.RewardsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsScreen(
    navController: NavController,
    viewModel: RewardsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val currentPoints by viewModel.currentPoints.collectAsStateWithLifecycle()
    val stampsCollected by viewModel.stampsCollected.collectAsStateWithLifecycle()

    val availableRewards = remember {
        listOf(
            RewardItem(
                id = "free_coffee",
                title = "Free Coffee",
                description = "Any size, any drink",
                pointsCost = 100,
                imageRes = R.drawable.americano
            ),
            RewardItem(
                id = "size_upgrade",
                title = "Free Size Upgrade",
                description = "Upgrade to next size",
                pointsCost = 50,
                imageRes = R.drawable.cappuccino
            ),
            RewardItem(
                id = "extra_shot",
                title = "Free Extra Shot",
                description = "Add extra espresso shot",
                pointsCost = 25,
                imageRes = R.drawable.mocha
            ),
            RewardItem(
                id = "pastry",
                title = "Free Pastry",
                description = "Any pastry item",
                pointsCost = 75,
                imageRes = R.drawable.flat_white
            )
        )
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Rewards",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Loyalty Card Section
            item {
                LoyaltyProgressCard(
                    currentPoints = currentPoints,
                    stampsCollected = stampsCollected
                )
            }

            // Available Rewards Header
            item {
                Text(
                    text = "Available Rewards",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Rewards List
            items(availableRewards) { reward ->
                RewardItemCard(
                    reward = reward,
                    currentPoints = currentPoints,
                    onRedeemClick = {
                        navController.navigate(Screen.RedeemRewards.route)
                    }
                )
            }
        }
    }
}

@Composable
fun LoyaltyProgressCard(
    currentPoints: Int,
    stampsCollected: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4A5568)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Points Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Your Points",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = currentPoints.toString(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Icon(
                    painter = painterResource(R.drawable.ic_coffee_cup),
                    contentDescription = "Points",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Stamps Section
            Text(
                text = "Loyalty Card Progress",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )

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
                    repeat(8) { index ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    color = if (index < stampsCollected)
                                        CoffeeBrown else Color.Gray.copy(alpha = 0.3f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_coffee_cup),
                                contentDescription = "Stamp",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Text(
                text = "${stampsCollected}/8 stamps collected",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun RewardItemCard(
    reward: RewardItem,
    currentPoints: Int,
    onRedeemClick: () -> Unit
) {
    val canRedeem = currentPoints >= reward.pointsCost

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = canRedeem) { onRedeemClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (canRedeem) Color.White else Color.Gray.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Reward Image
            Image(
                painter = painterResource(reward.imageRes),
                contentDescription = reward.title,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // Reward Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = reward.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (canRedeem) Color.Black else Color.Gray
                )
                Text(
                    text = reward.description,
                    fontSize = 12.sp,
                    color = if (canRedeem) Color.Gray else Color.Gray.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${reward.pointsCost} pts",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoffeeBrown
                )
            }

            // Redeem Button
            Button(
                onClick = onRedeemClick,
                enabled = canRedeem,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (canRedeem) CoffeeBrown else Color.Gray,
                    disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (canRedeem) "Redeem" else "Need ${reward.pointsCost - currentPoints} pts",
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RewardsScreenPreview() {
    RewardsScreen(navController = rememberNavController())
}