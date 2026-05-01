package com.example.farmlink_project.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class CartItem(
    val id: String,
    val name: String,
    val price: Double,
    var quantity: Int
)

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    fun addToCart(item: CartItem) {
        val currentCart = _cartItems.value.toMutableList()
        val existingItem = currentCart.find { it.id == item.id }

        if (existingItem != null) {
            existingItem.quantity += item.quantity
        } else {
            currentCart.add(item)
        }

        _cartItems.value = currentCart
    }
}
