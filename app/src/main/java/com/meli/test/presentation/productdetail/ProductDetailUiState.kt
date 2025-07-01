package com.meli.test.presentation.productdetail

import com.meli.test.domain.model.ProductDetail

data class ProductDetailUiState(
    val productDetail: ProductDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)