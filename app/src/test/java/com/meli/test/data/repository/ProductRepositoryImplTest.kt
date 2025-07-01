package com.meli.test.data.repository

import com.meli.test.data.remote.api.MeliApiService
import com.meli.test.data.remote.model.Attribute
import com.meli.test.data.remote.model.AttributeDetail
import com.meli.test.data.remote.model.MainFeature
import com.meli.test.data.remote.model.Paging
import com.meli.test.data.remote.model.Picture
import com.meli.test.data.remote.model.PictureDetail
import com.meli.test.data.remote.model.ProductDetailResponse
import com.meli.test.data.remote.model.ProductResult
import com.meli.test.data.remote.model.SearchResponse
import com.meli.test.data.remote.model.ShortDescription
import com.meli.test.util.ErrorMessages
import com.meli.test.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

class ProductRepositoryImplTest {
    private lateinit var repository: ProductRepositoryImpl
    private lateinit var apiService: MeliApiService

    @Before
    fun setup() {
        apiService = mockk()
        repository = ProductRepositoryImpl(apiService)
    }

    @Test
    fun `searchProducts returns success with products`() = runBlocking {
        // Given
        val query = "smartphone"
        val searchResponse = SearchResponse(
            keywords = query,
            paging = Paging(total = 2, offset = 0, limit = 10),
            results = listOf(
                ProductResult(
                    id = "1",
                    name = "Smartphone 1",
                    pictures = listOf(Picture("1", "image1.jpg")),
                    attributes = listOf(
                        Attribute(id = "BRAND", name = "Marca", valueId = "1", valueName = "Samsung"),
                        Attribute(id = "MODEL", name = "Modelo", valueId = "2", valueName = "Galaxy S21")
                    ),
                    priority = "high",
                    dateCreated = null,
                    catalogProductId = null,
                    pdpTypes = null,
                    status = "active",
                    domainId = null,
                    settings = null,
                    mainFeatures = null,
                    childrenIds = null,
                    qualityType = null,
                    type = null,
                    siteId = null,
                    keywords = null,
                    variations = null,
                    description = null
                ),
                ProductResult(
                    id = "2",
                    name = "Smartphone 2",
                    pictures = listOf(Picture("2", "image2.jpg")),
                    attributes = listOf(
                        Attribute(id = "BRAND", name = "Marca", valueId = "3", valueName = "Apple"),
                        Attribute(id = "MODEL", name = "Modelo", valueId = "4", valueName = "iPhone 13")
                    ),
                    priority = "medium",
                    dateCreated = null,
                    catalogProductId = null,
                    pdpTypes = null,
                    status = "active",
                    domainId = null,
                    settings = null,
                    mainFeatures = null,
                    childrenIds = null,
                    qualityType = null,
                    type = null,
                    siteId = null,
                    keywords = null,
                    variations = null,
                    description = null
                )
            ),
            usedAttributes = null,
            queryType = null
        )
        
        coEvery { apiService.searchProducts(query = query) } returns searchResponse

        // When
        val result = repository.searchProducts(query)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(2, (result as Resource.Success).data.size)
        assertEquals("1", result.data[0].id)
        assertEquals("Smartphone 1", result.data[0].name)
        assertEquals("image1.jpg", result.data[0].imageUrl)
        assertEquals("Samsung", result.data[0].brand)
        assertEquals("Galaxy S21", result.data[0].model)
        assertEquals("high", result.data[0].priority)
        
        assertEquals("2", result.data[1].id)
        assertEquals("Smartphone 2", result.data[1].name)
        assertEquals("image2.jpg", result.data[1].imageUrl)
        assertEquals("Apple", result.data[1].brand)
        assertEquals("iPhone 13", result.data[1].model)
        assertEquals("medium", result.data[1].priority)
    }
    
    @Test
    fun `searchProducts handles empty pictures correctly`() = runBlocking {
        // Given
        val query = "smartphone"
        val searchResponse = SearchResponse(
            keywords = query,
            paging = Paging(total = 1, offset = 0, limit = 10),
            results = listOf(
                ProductResult(
                    id = "1",
                    name = "Smartphone sin imagen",
                    pictures = null,
                    attributes = listOf(
                        Attribute(id = "BRAND", name = "Marca", valueId = "1", valueName = "Samsung"),
                        Attribute(id = "MODEL", name = "Modelo", valueId = "2", valueName = "Galaxy S21")
                    ),
                    priority = "high",
                    dateCreated = null,
                    catalogProductId = null,
                    pdpTypes = null,
                    status = "active",
                    domainId = null,
                    settings = null,
                    mainFeatures = null,
                    childrenIds = null,
                    qualityType = null,
                    type = null,
                    siteId = null,
                    keywords = null,
                    variations = null,
                    description = null
                )
            ),
            usedAttributes = null,
            queryType = null
        )
        
        coEvery { apiService.searchProducts(query = query) } returns searchResponse

        // When
        val result = repository.searchProducts(query)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data.size)
        assertEquals("1", result.data[0].id)
        assertEquals("Smartphone sin imagen", result.data[0].name)
        assertEquals("", result.data[0].imageUrl)
    }

    @Test
    fun `searchProducts returns error when API call fails`() = runBlocking {
        // Given
        val errorMessage = ErrorMessages.NETWORK_ERROR
        val query = "smartphone"
        coEvery { apiService.searchProducts(query = query) } throws IOException(errorMessage)

        // When
        val result = repository.searchProducts(query)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }

    @Test
    fun `getProductDetails returns success with product details`() = runBlocking {
        // Given
        val productId = "123"
        val productDetailResponse = ProductDetailResponse(
            id = productId,
            name = "Test Product",
            catalogProductId = null,
            status = "active",
            domainId = "domain",
            permalink = "permalink",
            pictures = listOf(
                PictureDetail(
                    id = "1", 
                    url = "image1.jpg",
                    suggestedForPicker = null,
                    maxWidth = 100,
                    maxHeight = 100,
                    sourceMetadata = null,
                    tags = null
                ),
                PictureDetail(
                    id = "2", 
                    url = "image2.jpg",
                    suggestedForPicker = null,
                    maxWidth = 100,
                    maxHeight = 100,
                    sourceMetadata = null,
                    tags = null
                )
            ),
            attributes = listOf(
                AttributeDetail(
                    id = "BRAND",
                    name = "Marca",
                    valueId = "1",
                    valueName = "Samsung",
                    values = null,
                    meta = null
                ),
                AttributeDetail(
                    id = "MODEL",
                    name = "Modelo",
                    valueId = "2",
                    valueName = "Galaxy S21",
                    values = null,
                    meta = null
                ),
                AttributeDetail(
                    id = "ITEM_CONDITION",
                    name = "Condición",
                    valueId = "3",
                    valueName = "Nuevo",
                    values = null,
                    meta = null
                )
            ),
            shortDescription = ShortDescription("text", "Test description"),
            mainFeatures = listOf(
                MainFeature(
                    text = "Característica 1",
                    type = "text",
                    metadata = null
                ),
                MainFeature(
                    text = "Característica 2",
                    type = "text",
                    metadata = null
                )
            ),
            familyName = null,
            type = null,
            buyBoxWinner = null,
            pdpTypes = null,
            pickers = null,
            descriptionPictures = null,
            disclaimers = null,
            parentId = null,
            userProduct = null,
            childrenIds = null,
            settings = null,
            qualityType = null,
            releaseInfo = null,
            presaleInfo = null,
            enhancedContent = null,
            tags = null,
            dateCreated = null,
            authorizedStores = null,
            lastUpdated = null,
            grouperId = null,
            experiments = null
        )
        coEvery { apiService.getProductDetails(productId) } returns productDetailResponse

        // When
        val result = repository.getProductDetails(productId)

        // Then
        assertTrue(result is Resource.Success)
        val productDetail = (result as Resource.Success).data
        assertEquals(productId, productDetail.id)
        assertEquals("Test Product", productDetail.name)
        assertEquals(listOf("image1.jpg", "image2.jpg"), productDetail.imageUrls)
        assertEquals("active", productDetail.status)
        assertEquals("permalink", productDetail.permalink)
        assertEquals("Test description", productDetail.shortDescription)
        assertEquals("Nuevo", productDetail.condition)

        assertEquals(3, productDetail.attributes.size)
        assertEquals("Marca", productDetail.attributes[0].name)
        assertEquals("Samsung", productDetail.attributes[0].value)
        assertEquals("Modelo", productDetail.attributes[1].name)
        assertEquals("Galaxy S21", productDetail.attributes[1].value)

        assertEquals(2, productDetail.mainFeatures.size)
        assertEquals("Característica 1", productDetail.mainFeatures[0])
        assertEquals("Característica 2", productDetail.mainFeatures[1])
    }

    @Test
    fun `getProductDetails returns error when API call fails`() = runBlocking {
        // Given
        val errorMessage = ErrorMessages.NETWORK_ERROR
        val productId = "123"
        coEvery { apiService.getProductDetails(productId) } throws IOException(errorMessage)

        // When
        val result = repository.getProductDetails(productId)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }
}