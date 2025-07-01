package com.meli.test.domain.repository

import com.meli.test.domain.model.Product
import com.meli.test.domain.model.ProductDetail
import com.meli.test.util.Resource

interface ProductRepository {
    suspend fun searchProducts(query: String): Resource<List<Product>>
    suspend fun getProductDetails(productId: String): Resource<ProductDetail>
}