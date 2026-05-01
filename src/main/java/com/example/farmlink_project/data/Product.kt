package com.example.farmlink_project.data

import androidx.annotation.DrawableRes
import com.example.farmlink_project.R

/**
 * A data class to represent the product information presented in the product card
 */
data class Product(
    val name: String,
    val price: String,
    val origin: String,
    @DrawableRes val imageResourceId: Int
)

// List of products with corresponding images
val products = listOf(
    Product("Apples", "$2.99", "Farm A", R.drawable.apples),
    Product("Cabbage", "$1.49", "Farm B", R.drawable.cabbage),
    Product("Tomatoes", "$3.99", "Farm C", R.drawable.tomatoes)
)
