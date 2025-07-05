package com.example.thecodecup.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ThemeManager(context: Context) : ViewModel() {
    private val prefs: SharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    var isDarkMode by mutableStateOf(prefs.getBoolean("dark_mode", false))
        private set

    fun toggleTheme() {
        isDarkMode = !isDarkMode
        prefs.edit().putBoolean("dark_mode", isDarkMode).apply()
    }
}

val LocalThemeManager = compositionLocalOf<ThemeManager> {
    error("ThemeManager not provided")
}