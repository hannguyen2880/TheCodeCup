package com.example.thecodecup.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.thecodecup.ui.theme.LocalThemeManager

@Composable
fun ThemeToggleButton(
    modifier: Modifier = Modifier
) {
    val themeManager = LocalThemeManager.current

    IconButton(
        onClick = { themeManager.toggleTheme() },
        modifier = modifier.size(40.dp)
    ) {
        Icon(
            imageVector = if (themeManager.isDarkMode) {
                Icons.Default.LightMode
            } else {
                Icons.Default.DarkMode
            },
            contentDescription = if (themeManager.isDarkMode) {
                "Switch to Light Mode"
            } else {
                "Switch to Dark Mode"
            },
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )
    }
}