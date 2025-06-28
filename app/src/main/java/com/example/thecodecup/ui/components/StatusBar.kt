package com.example.thecodecup.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StatusBar(
    backgroundColor: Color = Color.White,
    contentColor: Color = Color.Black
) {
    var currentTime by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            currentTime = formatter.format(Date())
            kotlinx.coroutines.delay(60000) // Update every minute
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side - Time
        Text(
            text = currentTime,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = contentColor
        )

        // Right side - Network indicators
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 4G
            Text(
                text = "4G",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = contentColor
            )

            // WiFi icon
            Icon(
                imageVector = Icons.Default.Wifi,
                contentDescription = "WiFi",
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )

            // Battery icon
            BatteryIcon(contentColor)
        }
    }
}

@Composable
private fun BatteryIcon(tint: Color) {
    // Simple battery representation using text
    Text(
        text = "🔋",
        fontSize = 14.sp,
        color = tint
    )
}