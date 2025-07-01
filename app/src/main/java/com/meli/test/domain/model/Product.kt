package com.meli.test.domain.model

data class Product(
    val id: String,
    val name: String,
    val imageUrl: String,
    val brand: String = "",
    val model: String = "",
    val priority: String
)