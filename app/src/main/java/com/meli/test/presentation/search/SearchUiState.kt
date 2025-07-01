package com.meli.test.presentation.search

import com.meli.test.domain.model.Product

data class SearchUiState(
    val searchQuery: String = "",
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasSearched: Boolean = false
)