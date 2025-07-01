package com.meli.test.data.repository

import com.meli.test.data.remote.api.MeliApiService
import com.meli.test.data.remote.model.Attribute
import com.meli.test.data.remote.model.AttributeDetail
import com.meli.test.domain.model.Product
import com.meli.test.domain.model.ProductAttribute
import com.meli.test.domain.model.ProductDetail
import com.meli.test.domain.repository.ProductRepository
import com.meli.test.util.ErrorMessages
import com.meli.test.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val apiService: MeliApiService,
) : ProductRepository {

    /**
     * Search products with main api service
     */
    override suspend fun searchProducts(query: String): Resource<List<Product>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.searchProducts(query = query)
            val products = response.results.map { result ->

                Product(
                    id = result.id,
                    name = result.name,
                    imageUrl = result.pictures?.firstOrNull()?.url ?: "",
                    brand = findBrandAttribute(result.attributes),
                    model = findModelAttribute(result.attributes),
                    priority = result.priority?: ""
                )
            }
            Resource.Success(products)
        } catch (e: Exception) {
            Resource.Error(e.message ?: ErrorMessages.GENERIC_ERROR)
        }
    }

    /**
     * Get product detail for specific product id
     */
    override suspend fun getProductDetails(productId: String): Resource<ProductDetail> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getProductDetails(productId)

            val imageUrls = response.pictures.mapNotNull { it.url }

            val attributes = response.attributes?.mapNotNull { attribute ->
                if (attribute.name != null && attribute.valueName != null) {
                    ProductAttribute(
                        name = attribute.name,
                        value = attribute.valueName
                    )
                } else null
            } ?: emptyList()

            val mainFeatures = response.mainFeatures?.mapNotNull { it.text } ?: emptyList()

            val productDetail = ProductDetail(
                id = response.id,
                name = response.name,
                imageUrls = imageUrls,
                status = response.status,
                permalink = response.permalink,
                attributes = attributes,
                mainFeatures = mainFeatures,
                shortDescription = response.shortDescription?.content,
                condition = findConditionAttribute(response.attributes)
            )
            Resource.Success(productDetail)
        } catch (e: Exception) {
            Resource.Error(e.message ?: ErrorMessages.GENERIC_ERROR)
        }
    }

    private fun findBrandAttribute(attributes: List<Attribute>?): String {
        return attributes?.find { it.id == "BRAND" }?.valueName ?: ""
    }

    private fun findModelAttribute(attributes: List<Attribute>?): String {
        return attributes?.find { it.id == "MODEL" }?.valueName ?: ""
    }

    private fun findConditionAttribute(attributes: List<AttributeDetail>?): String {
        return attributes?.find { it.id == "ITEM_CONDITION" }?.valueName ?: ""
    }
}
