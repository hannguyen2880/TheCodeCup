package com.example.thecodecup.data.model

import androidx.annotation.DrawableRes

data class Coffee(
    val id: String,
    val name: String,
    val price: Double,
    @DrawableRes val imageRes: Int,
    val description: String = "",
    val category: String = "Hot Coffee",
    val rating: Float = 4.5f,
    val isPopular: Boolean = false,
    val ingredients: List<String> = emptyList(),
    val nutritionInfo: NutritionInfo? = null
)

data class NutritionInfo(
    val calories: Int,
    val caffeine: String,
    val sugar: String
)

// Coffee categories enum [[0]](#__0)
enum class CoffeeCategory(val displayName: String) {
    HOT_COFFEE("Hot Coffee"),
    ICED_COFFEE("Iced Coffee"),
    ESPRESSO("Espresso"),
    SPECIALTY("Specialty")
}

// Coffee customization options [[2]](#__2)
data class CoffeeCustomization(
    val shotType: ShotType = ShotType.SINGLE,
    val size: CoffeeSize = CoffeeSize.MEDIUM,
    val hasIce: Boolean = false,
    val milkType: MilkType = MilkType.REGULAR,
    val sweetness: SweetnessLevel = SweetnessLevel.NORMAL
)

enum class ShotType(val displayName: String, val priceModifier: Double) {
    SINGLE("Single", 0.0),
    DOUBLE("Double", 0.5)
}

enum class CoffeeSize(val displayName: String, val priceModifier: Double) {
    SMALL("Small", -0.25),
    MEDIUM("Medium", 0.0),
    LARGE("Large", 0.5)
}

enum class MilkType(val displayName: String) {
    REGULAR("Regular"),
    ALMOND("Almond"),
    SOY("Soy"),
    OAT("Oat")
}

enum class SweetnessLevel(val displayName: String) {
    NO_SUGAR("No Sugar"),
    LIGHT("Light"),
    NORMAL("Normal"),
    EXTRA("Extra Sweet")
}