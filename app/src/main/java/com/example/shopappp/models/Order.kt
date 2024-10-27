package com.example.shopappp.models

data class Order(
    val id: Long = 0,
    val name: String,
    val address: String,
    val contactNumber: String,
    val email: String,
    val totalAmount: Double,
    val items: List<CartItem>
)
