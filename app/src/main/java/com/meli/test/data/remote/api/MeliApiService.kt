package com.meli.test.data.remote.api

import com.meli.test.data.remote.model.ProductDetailResponse
import com.meli.test.data.remote.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MeliApiService {

    @GET("products/search")
    suspend fun searchProducts(
        @Query("site_id") siteId: String = "MCO", // to colombia
        @Query("q") query: String,
        @Query("status") status: String = "active"
    ): SearchResponse

    @GET("products/{productId}")
    suspend fun getProductDetails(
        @Path("productId") productId: String
    ): ProductDetailResponse
}