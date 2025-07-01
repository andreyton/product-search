package com.meli.test.data.remote.model

import com.google.gson.annotations.SerializedName

data class ProductDetailResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("catalog_product_id")
    val catalogProductId: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("domain_id")
    val domainId: String?,
    @SerializedName("permalink")
    val permalink: String?,
    @SerializedName("family_name")
    val familyName: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("buy_box_winner")
    val buyBoxWinner: Any?,
    @SerializedName("pdp_types")
    val pdpTypes: List<String>? = emptyList(),
    @SerializedName("pickers")
    val pickers: List<Picker>? = emptyList(),
    @SerializedName("pictures")
    val pictures: List<PictureDetail> = emptyList(),
    @SerializedName("description_pictures")
    val descriptionPictures: List<Any>? = emptyList(),
    @SerializedName("main_features")
    val mainFeatures: List<MainFeature>? = emptyList(),
    @SerializedName("disclaimers")
    val disclaimers: List<Any>? = emptyList(),
    @SerializedName("attributes")
    val attributes: List<AttributeDetail>? = emptyList(),
    @SerializedName("short_description")
    val shortDescription: ShortDescription? = null,
    @SerializedName("parent_id")
    val parentId: String?,
    @SerializedName("user_product")
    val userProduct: Any?,
    @SerializedName("children_ids")
    val childrenIds: List<String>? = emptyList(),
    @SerializedName("settings")
    val settings: SettingsDetail?,
    @SerializedName("quality_type")
    val qualityType: String?,
    @SerializedName("release_info")
    val releaseInfo: Any?,
    @SerializedName("presale_info")
    val presaleInfo: Any?,
    @SerializedName("enhanced_content")
    val enhancedContent: Any?,
    @SerializedName("tags")
    val tags: List<String>? = emptyList(),
    @SerializedName("date_created")
    val dateCreated: String?,
    @SerializedName("authorized_stores")
    val authorizedStores: Any?,
    @SerializedName("last_updated")
    val lastUpdated: String?,
    @SerializedName("grouper_id")
    val grouperId: Any?,
    @SerializedName("experiments")
    val experiments: Map<String, Any>? = emptyMap()
)

data class ShortDescription(
    @SerializedName("type")
    val type: String?,
    @SerializedName("content")
    val content: String?
)

data class PictureDetail(
    @SerializedName("id")
    val id: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("suggested_for_picker")
    val suggestedForPicker: Any?,
    @SerializedName("max_width")
    val maxWidth: Int?,
    @SerializedName("max_height")
    val maxHeight: Int?,
    @SerializedName("source_metadata")
    val sourceMetadata: Any?,
    @SerializedName("tags")
    val tags: List<String>? = emptyList()
)

data class Picker(
    @SerializedName("picker_id")
    val pickerId: String?,
    @SerializedName("picker_name")
    val pickerName: String?,
    @SerializedName("products")
    val products: List<PickerProduct>? = emptyList(),
    @SerializedName("tags")
    val tags: List<String>? = emptyList(),
    @SerializedName("attributes")
    val attributes: List<PickerAttribute>? = emptyList(),
    @SerializedName("value_name_delimiter")
    val valueNameDelimiter: String?
)

data class PickerProduct(
    @SerializedName("product_id")
    val productId: String?,
    @SerializedName("picker_label")
    val pickerLabel: String?,
    @SerializedName("picture_id")
    val pictureId: String?,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("tags")
    val tags: List<String>? = emptyList(),
    @SerializedName("permalink")
    val permalink: String?,
    @SerializedName("product_name")
    val productName: String?,
    @SerializedName("auto_completed")
    val autoCompleted: Boolean?
)

data class PickerAttribute(
    @SerializedName("attribute_id")
    val attributeId: String?,
    @SerializedName("template")
    val template: String?
)

data class MainFeature(
    @SerializedName("text")
    val text: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("metadata")
    val metadata: Map<String, Any>? = emptyMap()
)

data class AttributeDetail(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("value_id")
    val valueId: String?,
    @SerializedName("value_name")
    val valueName: String?,
    @SerializedName("values")
    val values: List<AttributeValue>? = emptyList(),
    @SerializedName("meta")
    val meta: AttributeMeta? = null
)

data class AttributeValue(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("meta")
    val meta: AttributeMeta? = null
)

data class AttributeMeta(
    @SerializedName("value")
    val value: Any?,
    @SerializedName("rgb")
    val rgb: String?
)

data class SettingsDetail(
    @SerializedName("content")
    val content: String?,
    @SerializedName("listing_strategy")
    val listingStrategy: String?,
    @SerializedName("with_enhanced_pictures")
    val withEnhancedPictures: Boolean?,
    @SerializedName("base_site_product_id")
    val baseSiteProductId: Any?,
    @SerializedName("exclusive")
    val exclusive: Boolean?
)