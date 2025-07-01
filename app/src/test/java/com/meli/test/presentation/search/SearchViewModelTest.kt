package com.meli.test.presentation.search

import com.meli.test.domain.model.Product
import com.meli.test.domain.usecase.SearchProductsUseCase
import com.meli.test.util.ErrorMessages
import com.meli.test.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {
    private lateinit var viewModel: ProductViewModel
    private lateinit var searchProductsUseCase: SearchProductsUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val products = listOf(
        Product("1", "Smartphone 1", "image1.jpg", "BRAND", "MODEL", "MEDIUM"),
        Product("2", "Smartphone 2", "image2.jpg", "BRAND", "MODEL", "MEDIUM")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        searchProductsUseCase = mockk()
        viewModel = ProductViewModel(searchProductsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSearchQueryChange updates searchQuery in state`() {
        // Given
        val query = "smartphone"

        // When
        viewModel.onSearchQueryChange(query)

        // Then
        assertEquals(query, viewModel.uiState.value.searchQuery)
    }

    @Test
    fun `searchProducts with empty query does nothing`() = runTest {
        // Given
        val errorMessage = ErrorMessages.EMPTY_SEARCH_ERROR
        coEvery { searchProductsUseCase("") } returns Resource.Error(errorMessage)

        // When
        viewModel.onSearchQueryChange("")
        viewModel.searchProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(errorMessage, viewModel.uiState.value.error)
        assertTrue(viewModel.uiState.value.hasSearched)
        assertTrue(viewModel.uiState.value.products.isEmpty())
    }

    @Test
    fun `searchProducts with valid query returns success`() = runTest {
        // Given
        val query = "smartphone"

        coEvery { searchProductsUseCase(query) } returns Resource.Success(products)

        // When
        viewModel.onSearchQueryChange(query)
        viewModel.searchProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.hasSearched)
        assertEquals(products, viewModel.uiState.value.products)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `searchProducts with error returns error state`() = runTest {
        // Given
        val query = "smartphone"
        val errorMessage = "Error de red"
        coEvery { searchProductsUseCase(query) } returns Resource.Error(errorMessage)

        // When
        viewModel.onSearchQueryChange(query)
        viewModel.searchProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.hasSearched)
        assertTrue(viewModel.uiState.value.products.isEmpty())
        assertEquals(errorMessage, viewModel.uiState.value.error)
    }

    @Test
    fun `clearResults resets state`() {
        // Given
        val query = "smartphone"
        viewModel.onSearchQueryChange(query)
        coEvery { searchProductsUseCase(query) } returns Resource.Success(products)
        viewModel.searchProducts()

        // When
        viewModel.clearResults()

        // Then
        assertEquals(query, viewModel.uiState.value.searchQuery)
        assertTrue(viewModel.uiState.value.products.isEmpty())
        assertFalse(viewModel.uiState.value.hasSearched)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `loadProductsForQuery loads products for new query`() = runTest {
        // Given
        val query = "smartphone"
        viewModel.onSearchQueryChange(query)
        coEvery { searchProductsUseCase(query) } returns Resource.Success(products)

        // When
        viewModel.loadProductsForQuery()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(query, viewModel.uiState.value.searchQuery)
        assertEquals(products, viewModel.uiState.value.products)
        assertTrue(viewModel.uiState.value.hasSearched)
    }
}