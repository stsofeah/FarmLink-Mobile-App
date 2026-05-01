package com.example.farmlink_project.data

data class OrderItem(
    val name: String,
    val price: String,
    val imageRes: Int,
    val quantity: String = "1KG"
) 