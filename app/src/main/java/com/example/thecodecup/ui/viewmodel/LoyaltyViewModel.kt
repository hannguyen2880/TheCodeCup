package com.example.thecodecup.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.example.thecodecup.data.repository.UserPreferencesRepository
import kotlin.compareTo

class LoyaltyViewModel: ViewModel() {

    private val _loyaltyStamps = mutableStateOf(0)
    val loyaltyStamps: State<Int> = _loyaltyStamps

    val maxStamps = 8
    val isCardComplete: Boolean get() = _loyaltyStamps.value >= maxStamps

    fun addStampFromCompletedOrder() {
        if (_loyaltyStamps.value < maxStamps) {
            val newStamps = _loyaltyStamps.value + 1
            _loyaltyStamps.value = newStamps
        }
    }

    fun redeemFreeCard(): Boolean {
        return if (_loyaltyStamps.value >= maxStamps) {
            _loyaltyStamps.value = 0
            true
        } else {
            false
        }
    }

    fun refreshStamps() {
        //_loyaltyStamps.value = userPreferencesRepository.getLoyaltyStamps()
    }
}