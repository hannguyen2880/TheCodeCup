package com.example.thecodecup.data.repository

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoyaltyRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("loyalty_prefs", Context.MODE_PRIVATE)

    private val _loyaltyStamps = MutableStateFlow(loadLoyaltyStamps())
    val loyaltyStamps: StateFlow<Int> = _loyaltyStamps.asStateFlow()

    private fun loadLoyaltyStamps(): Int {
        return prefs.getInt("loyalty_stamps", 0)
    }

    fun updateLoyaltyStamps(stamps: Int) {
        prefs.edit().putInt("loyalty_stamps", stamps).apply()
        _loyaltyStamps.value = stamps
    }

    fun addStamp() {
        val currentStamps = _loyaltyStamps.value
        if (currentStamps < 8) {
            updateLoyaltyStamps(currentStamps + 1)
        }
    }

    fun resetStamps() {
        updateLoyaltyStamps(0)
    }
}