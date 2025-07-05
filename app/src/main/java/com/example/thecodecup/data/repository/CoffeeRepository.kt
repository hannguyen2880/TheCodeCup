// data/repository/CoffeeRepository.kt
package com.example.thecodecup.data.repository

import com.example.thecodecup.R
import com.example.thecodecup.data.model.Coffee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface CoffeeRepository {
    fun getAllCoffees(): Flow<List<Coffee>>
    fun getCoffeeById(id: String): Flow<Coffee?>
    fun getPopularCoffees(): Flow<List<Coffee>>
    fun searchCoffees(query: String): Flow<List<Coffee>>
}

class CoffeeRepositoryImpl : CoffeeRepository {
    private val coffeeList = listOf(
        Coffee(
            id = "americano",
            name = "Americano",
            price = 3.00,
            imageRes = R.drawable.americano,
            description = "Rich and bold espresso with hot water",
            isPopular = true,
            rating = 4.5f
        ),
        Coffee(
            id = "cappuccino",
            name = "Cappuccino",
            price = 3.50,
            imageRes = R.drawable.cappuccino,
            description = "Espresso with steamed milk and foam",
            isPopular = true,
            rating = 4.7f
        ),
        Coffee(
            id = "mocha",
            name = "Mocha",
            price = 4.00,
            imageRes = R.drawable.mocha,
            description = "Chocolate and espresso perfect blend",
            rating = 4.6f
        ),
        Coffee(
            id = "flat_white",
            name = "Flat White",
            price = 3.75,
            imageRes = R.drawable.flat_white,
            description = "Smooth espresso with microfoam milk",
            rating = 4.4f
        )
    )

    override fun getAllCoffees(): Flow<List<Coffee>> = flowOf(coffeeList)

    override fun getCoffeeById(id: String): Flow<Coffee?> =
        flowOf(coffeeList.find { it.id == id })

    override fun getPopularCoffees(): Flow<List<Coffee>> =
        flowOf(coffeeList.filter { it.isPopular })

    //override fun searchCoffees(query: String): Flow<List<Coffee>> =
    //    flowOf(coffeeList.filter { it.name.contains(query, ignoreCase = true) })

    // Add this method to your existing CoffeeRepositoryImpl class
    override fun searchCoffees(query: String): Flow<List<Coffee>> {
        return flowOf(
            coffeeList.filter { coffee ->
                coffee.name.contains(query, ignoreCase = true) ||
                        coffee.description.contains(query, ignoreCase = true) ||
                        coffee.category.contains(query, ignoreCase = true) ||
                        coffee.ingredients.any { ingredient ->
                            ingredient.contains(query, ignoreCase = true)
                        }
            }.sortedWith(
                compareByDescending<Coffee> { it.isPopular }
                    .thenByDescending { it.rating }
                    .thenBy { it.name }
            )
        )
    }
}