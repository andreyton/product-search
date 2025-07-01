package com.meli.test.data.remote.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("keywords")
    val keywords: String,
    @SerializedName("paging")
    val paging: Paging,
    @SerializedName("results")
    val results: List<ProductResult>,
    @SerializedName("used_attributes")
    val usedAttributes: List<Attribute>?,
    @SerializedName("query_type")
    val queryType: String?
)

data class Paging(
    @SerializedName("total")
    val total: Int,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("limit")
    val limit: Int
)

data class ProductResult(
    @SerializedName("id")
    val id: String,
    @SerializedName("date_created")
    val dateCreated: String?,
    @SerializedName("catalog_product_id")
    val catalogProductId: String?,
    @SerializedName("pdp_types")
    val pdpTypes: List<String>?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("domain_id")
    val domainId: String?,
    @SerializedName("settings")
    val settings: Settings?,
    @SerializedName("name")
    val name: String,
    @SerializedName("main_features")
    val mainFeatures: List<Any>?,
    @SerializedName("attributes")
    val attributes: List<Attribute>?,
    @SerializedName("pictures")
    val pictures: List<Picture>?,
    @SerializedName("children_ids")
    val childrenIds: List<String>?,
    @SerializedName("quality_type")
    val qualityType: String?,
    @SerializedName("priority")
    val priority: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("site_id")
    val siteId: String?,
    @SerializedName("keywords")
    val keywords: String?,
    @SerializedName("variations")
    val variations: List<Any>?,
    @SerializedName("description")
    val description: String?
)

data class Settings(
    @SerializedName("listing_strategy")
    val listingStrategy: String?,
    @SerializedName("exclusive")
    val exclusive: Boolean?
)

data class Attribute(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("value_id")
    val valueId: String?,
    @SerializedName("value_name")
    val valueName: String?
)

data class Picture(
    @SerializedName("id")
    val id: String?,
    @SerializedName("url")
    val url: String
)