package com.meli.test.domain.model

data class ProductDetail(
    val id: String,
    val name: String,
    val imageUrls: List<String>,
    val status: String?,
    val permalink: String?,
    val attributes: List<ProductAttribute>,
    val mainFeatures: List<String>,
    val shortDescription: String?,
    val condition: String?
)

data class ProductAttribute(
    val name: String,
    val value: String
)
