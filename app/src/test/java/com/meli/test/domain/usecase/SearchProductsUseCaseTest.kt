package com.meli.test.domain.usecase

import com.meli.test.domain.model.Product
import com.meli.test.domain.repository.ProductRepository
import com.meli.test.util.ErrorMessages
import com.meli.test.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SearchProductsUseCaseTest {

    private lateinit var useCase: SearchProductsUseCase
    private lateinit var repository: ProductRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = SearchProductsUseCase(repository)
    }

    @Test
    fun `invoke returns success with products`() = runBlocking {
        // Given
        val query = "smartphone"
        val products = listOf(
            Product("1", "Smartphone 1", "image1.jpg", "BRAND", "MODEL", "MEDIUM"),
            Product("2", "Smartphone 2", "image2.jpg", "BRAND", "MODEL", "MEDIUM")
        )
        coEvery { repository.searchProducts(query) } returns Resource.Success(products)

        // When
        val result = useCase(query)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(products, (result as Resource.Success).data)
    }

    @Test
    fun `invoke returns error when repository fails`() = runBlocking {
        // Given
        val query = "smartphone"
        val errorMessage = ErrorMessages.GENERIC_ERROR
        coEvery { repository.searchProducts(query) } returns Resource.Error(errorMessage)

        // When
        val result = useCase(query)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }
}