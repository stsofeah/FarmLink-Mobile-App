package com.example.farmlink_project.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val weight: String,
    val price: String,
    val location: String,
    val stockQuantity: Int,
    val sellerId: String // To identify which user listed the product
)