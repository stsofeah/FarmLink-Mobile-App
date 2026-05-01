package com.example.farmlink_project.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

class ProductRepository(context: Context) {
    private val productDao = AppDatabase.getDatabase(context).productDao()

    suspend fun addProduct(
        name: String,
        weight: String,
        price: String,
        location: String,
        stockQuantity: Int,
        sellerId: String
    ) {
        val product = ProductEntity(
            name = name,
            weight = weight,
            price = price,
            location = location,
            stockQuantity = stockQuantity,
            sellerId = sellerId
        )
        productDao.insertProduct(product)
    }

    fun getProductsBySeller(sellerId: String): Flow<List<ProductEntity>> {
        return productDao.getProductsBySeller(sellerId)
    }

    suspend fun deleteProduct(product: ProductEntity) {
        productDao.deleteProduct(product)
    }
} 