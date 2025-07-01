package com.meli.test.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meli.test.domain.usecase.SearchProductsUseCase
import com.meli.test.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    /**
     * Call a search service to get products
     */
    fun searchProducts() {
        val query = _uiState.value.searchQuery

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            when (val result = searchProductsUseCase(query)) {
                is Resource.Success -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            products = result.data,
                            hasSearched = true
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = result.message,
                            hasSearched = true
                        )
                    }
                }
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    /**
     *  load a previous searched product list
     */
    fun loadProductsForQuery() {
        // Validate if list has products to show
        if (_uiState.value.products.isNotEmpty()) {
            return
        }
        searchProducts()
    }

    /**
     * Clear result from a search
     */
    fun clearResults() {
        _uiState.update {
            it.copy(
                products = emptyList(),
                hasSearched = false,
                error = null
            )
        }
    }
}