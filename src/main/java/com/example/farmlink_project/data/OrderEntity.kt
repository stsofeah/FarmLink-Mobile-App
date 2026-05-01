package com.example.farmlink_project.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val totalAmount: Double,
    val orderDate: Date,
    val shippingMethod: String,
    val paymentMethod: String,
    val items: String, // JSON string of List<OrderItem>
    val status: String = "Pending" // Can be "Pending", "Completed", etc.
) 