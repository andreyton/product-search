package com.meli.test.presentation.productdetail

import com.meli.test.domain.model.ProductDetail
import com.meli.test.domain.usecase.GetProductDetailsUseCase
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
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductDetailViewModelTest {
    private lateinit var viewModel: ProductDetailViewModel
    private lateinit var getProductDetailsUseCase: GetProductDetailsUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val productId = "123"
    private val productDetail = ProductDetail(
        id = productId,
        name = "Test Product",
        imageUrls = listOf("image.jpg"),
        status = "active",
        permalink = "",
        attributes = listOf(),
        mainFeatures = listOf(),
        shortDescription = "description",
        condition = ""
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getProductDetailsUseCase = mockk()
        viewModel = ProductDetailViewModel(getProductDetailsUseCase, productId)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads product details`() = runTest {
        // Given
        coEvery { getProductDetailsUseCase(productId) } returns Resource.Success(productDetail)

        // When
        viewModel.loadProductDetails()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(productDetail, viewModel.uiState.value.productDetail)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `init with error sets error state`() = runTest {
        // Given
        val errorMessage = ErrorMessages.NETWORK_ERROR
        coEvery { getProductDetailsUseCase(productId) } returns Resource.Error(errorMessage)

        // When
        viewModel.loadProductDetails()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.productDetail)
        assertEquals(errorMessage, viewModel.uiState.value.error)
    }

    @Test
    fun `retry loads product details again`() = runTest {
        // Given
        val errorMessage = ErrorMessages.NETWORK_ERROR
        
        // First call fails
        coEvery { getProductDetailsUseCase(productId) } returns Resource.Error(errorMessage)
        viewModel.loadProductDetails()
        
        // Initialize with error
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(errorMessage, viewModel.uiState.value.error)
        
        // Second call succeeds
        coEvery { getProductDetailsUseCase(productId) } returns Resource.Success(productDetail)
        
        // When
        viewModel.retry()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(productDetail, viewModel.uiState.value.productDetail)
        assertNull(viewModel.uiState.value.error)
    }
}