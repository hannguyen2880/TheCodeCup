package com.example.thecodecup.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.thecodecup.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val _userProfile = MutableStateFlow(loadUserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private fun loadUserProfile(): UserProfile {
        return UserProfile(
            fullName = prefs.getString("full_name", "Anderson") ?: "Anderson",
            phoneNumber = prefs.getString("phone_number", "+60134589525") ?: "+60134589525",
            email = prefs.getString("email", "Anderson@email.com") ?: "Anderson@email.com",
            address = prefs.getString("address", "3 Addersion Court\nChino Hills, HO56824, United State")
                ?: "3 Addersion Court\nChino Hills, HO56824, United State",
            profileImageUri = prefs.getString("profile_image_uri", null)
        )
    }

    fun updateUserProfile(profile: UserProfile) {
        prefs.edit().apply {
            putString("full_name", profile.fullName)
            putString("phone_number", profile.phoneNumber)
            putString("email", profile.email)
            putString("address", profile.address)
            putString("profile_image_uri", profile.profileImageUri)
            apply()
        }
        _userProfile.value = profile
    }

    fun updateField(field: String, value: String) {
        val currentProfile = _userProfile.value
        val updatedProfile = when (field) {
            "full_name" -> currentProfile.copy(fullName = value)
            "phone_number" -> currentProfile.copy(phoneNumber = value)
            "email" -> currentProfile.copy(email = value)
            "address" -> currentProfile.copy(address = value)
            else -> currentProfile
        }
        updateUserProfile(updatedProfile)
    }

    fun updateProfileImage(uri: String) {
        val currentProfile = _userProfile.value
        updateUserProfile(currentProfile.copy(profileImageUri = uri))
    }

    fun getLoyaltyStamps(): Int {
        return prefs.getInt("loyalty_stamps", 0)
    }

    fun updateLoyaltyStamps(stamps: Int) {
        prefs.edit().putInt("loyalty_stamps", stamps).apply()
    }

    fun resetLoyaltyStamps() {
        prefs.edit().putInt("loyalty_stamps", 0).apply()
    }

    fun addLoyaltyStamp() {
        val currentStamps = getLoyaltyStamps()
        if (currentStamps < 8) {
            updateLoyaltyStamps(currentStamps + 1)
        }
    }
}