package com.example.thecodecup.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.thecodecup.data.model.CartItem
import com.example.thecodecup.data.model.Coffee
import com.example.thecodecup.data.model.CoffeeCustomization
import com.example.thecodecup.data.model.CoffeeSize
import com.example.thecodecup.data.model.ShotType
import com.example.thecodecup.data.model.MilkType
import com.example.thecodecup.data.model.SweetnessLevel
import com.example.thecodecup.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class DataStoreRepository(private val context: Context) {
    companion object {
        val CART_ITEMS_KEY = stringPreferencesKey("cart_items")
        val LOYALTY_STAMPS_KEY = intPreferencesKey("loyalty_stamps")
        val TOTAL_POINTS_KEY = intPreferencesKey("total_points")
        val POINTS_HISTORY_KEY = stringPreferencesKey("points_history")
    }

    private val json = Json { ignoreUnknownKeys = true }

    // Cart persistence
    suspend fun saveCartItems(items: List<CartItem>) {
        context.dataStore.edit { preferences ->
            val serializedItems = items.map { item ->
                mapOf(
                    "id" to item.id,
                    "coffeeId" to item.coffee.id,
                    "coffeeName" to item.coffee.name,
                    "coffeePrice" to item.coffee.price.toString(),
                    "coffeeImageRes" to item.coffee.imageRes.toString(),
                    "shotType" to item.customization.shotType.name,
                    "size" to item.customization.size.name,
                    "milkType" to item.customization.milkType.name,
                    "sweetness" to item.customization.sweetness.name,
                    "quantity" to item.quantity.toString(),
                    "totalPrice" to item.totalPrice.toString(),
                    "isHot" to item.isHot.toString(),
                    "iceLevel" to item.iceLevel.toString()
                )
            }
            preferences[CART_ITEMS_KEY] = json.encodeToString(serializedItems)
        }
    }

    fun getCartItems(): Flow<List<CartItem>> {
        return context.dataStore.data.map { preferences ->
            val serializedItems = preferences[CART_ITEMS_KEY] ?: return@map emptyList()
            try {
                val itemMaps = json.decodeFromString<List<Map<String, String>>>(serializedItems)
                itemMaps.map { itemMap ->
                    CartItem(
                        id = itemMap["id"] ?: "",
                        coffee = Coffee(
                            id = itemMap["coffeeId"] ?: "",
                            name = itemMap["coffeeName"] ?: "",
                            price = itemMap["coffeePrice"]?.toDoubleOrNull() ?: 0.0,
                            imageRes = itemMap["coffeeImageRes"]?.toIntOrNull() ?: R.drawable.americano
                        ),
                        customization = CoffeeCustomization(
                            shotType = ShotType.valueOf(itemMap["shotType"] ?: "SINGLE"),
                            size = CoffeeSize.valueOf(itemMap["size"] ?: "MEDIUM"),
                            milkType = MilkType.valueOf(itemMap["milkType"] ?: "REGULAR"),
                            sweetness = SweetnessLevel.valueOf(itemMap["sweetness"] ?: "NORMAL")
                        ),
                        quantity = itemMap["quantity"]?.toIntOrNull() ?: 1,
                        totalPrice = itemMap["totalPrice"]?.toDoubleOrNull() ?: 0.0,
                        isHot = itemMap["isHot"]?.toBooleanStrictOrNull() ?: true,
                        iceLevel = itemMap["iceLevel"]?.toIntOrNull() ?: 3
                    )
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    // Loyalty data persistence
    suspend fun saveLoyaltyStamps(stamps: Int) {
        context.dataStore.edit { preferences ->
            preferences[LOYALTY_STAMPS_KEY] = stamps
        }
    }

    fun getLoyaltyStamps(): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[LOYALTY_STAMPS_KEY] ?: 0
        }
    }

    suspend fun saveTotalPoints(points: Int) {
        context.dataStore.edit { preferences ->
            preferences[TOTAL_POINTS_KEY] = points
        }
    }

    fun getTotalPoints(): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[TOTAL_POINTS_KEY] ?: 0
        }
    }

    suspend fun clearCart() {
        context.dataStore.edit { preferences ->
            preferences.remove(CART_ITEMS_KEY)
        }
    }
}