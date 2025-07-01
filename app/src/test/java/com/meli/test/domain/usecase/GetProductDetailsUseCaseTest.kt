package com.meli.test.domain.usecase

import com.meli.test.domain.model.ProductDetail
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

class GetProductDetailsUseCaseTest {

    private lateinit var useCase: GetProductDetailsUseCase
    private lateinit var repository: ProductRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetProductDetailsUseCase(repository)
    }

    @Test
    fun `invoke returns success with product details`() = runBlocking {
        // Given
        val productId = "123"
        val productDetail = ProductDetail(
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

        coEvery { repository.getProductDetails(productId) } returns Resource.Success(productDetail)

        // When
        val result = useCase(productId)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(productDetail, (result as Resource.Success).data)
    }

    @Test
    fun `invoke returns error when repository fails`() = runBlocking {
        // Given
        val productId = "123"
        val errorMessage = ErrorMessages.GENERIC_ERROR
        coEvery { repository.getProductDetails(productId) } returns Resource.Error(errorMessage)

        // When
        val result = useCase(productId)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }
}