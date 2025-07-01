package com.meli.test.domain.usecase

import com.meli.test.domain.model.Product
import com.meli.test.domain.repository.ProductRepository
import com.meli.test.util.ErrorMessages
import com.meli.test.util.Resource

class SearchProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(query: String): Resource<List<Product>> {
        if (query.isBlank()) {
            return Resource.Error(ErrorMessages.EMPTY_SEARCH_ERROR)
        }
        val result = repository.searchProducts(query)

        if (result is Resource.Error) {
            val errorMessage = result.message.lowercase()
            return when {
                errorMessage.contains("connection") ||
                errorMessage.contains("timeout") ||
                errorMessage.contains("unable to resolve host") -> {
                    Resource.Error(ErrorMessages.NETWORK_ERROR)
                }
                else -> {
                    Resource.Error(ErrorMessages.GENERIC_ERROR)
                }
            }
        }
        
        return result
    }
}