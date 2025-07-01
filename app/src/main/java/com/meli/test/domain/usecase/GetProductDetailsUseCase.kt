package com.meli.test.domain.usecase

import com.meli.test.domain.model.ProductDetail
import com.meli.test.domain.repository.ProductRepository
import com.meli.test.util.ErrorMessages
import com.meli.test.util.Resource

class GetProductDetailsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(productId: String): Resource<ProductDetail> {
        if (productId.isBlank()) {
            return Resource.Error(ErrorMessages.EMPTY_ID_PRODUCT_ERROR)
        }
        val result = repository.getProductDetails(productId)

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