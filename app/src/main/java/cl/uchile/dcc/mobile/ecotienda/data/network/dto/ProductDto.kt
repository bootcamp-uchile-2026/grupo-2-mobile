package cl.uchile.dcc.mobile.ecotienda.data.network.dto

import com.google.gson.annotations.SerializedName

data class ProductListDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("producerId") val producerId: Int,
    @SerializedName("producerName") val producerName: String? = null,
    @SerializedName("categoryId") val categoryId: Int,
    @SerializedName("price") val price: Int,
    @SerializedName("offerPrice") val offerPrice: Int? = null,
    @SerializedName("finalPrice") val finalPrice: Int,
    @SerializedName("discountPercentage") val discountPercentage: Int = 0,
    @SerializedName("stock") val stock: Int = 0,
    @SerializedName("available") val available: Boolean = true,
    @SerializedName("co2Avoided") val co2Avoided: Double = 0.0,
    @SerializedName("greenPoints") val greenPoints: Int = 0,
    @SerializedName("rating") val rating: Double? = null,
    @SerializedName("reviewCount") val reviewCount: Int = 0,
    @SerializedName("primaryImageFileId") val primaryImageFileId: String? = null,
)

data class ProductDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("producerId") val producerId: Int,
    @SerializedName("producerName") val producerName: String? = null,
    @SerializedName("categoryId") val categoryId: Int,
    @SerializedName("categoryName") val categoryName: String? = null,
    @SerializedName("price") val price: Int,
    @SerializedName("offerPrice") val offerPrice: Int? = null,
    @SerializedName("finalPrice") val finalPrice: Int,
    @SerializedName("discountPercentage") val discountPercentage: Int = 0,
    @SerializedName("stock") val stock: Int = 0,
    @SerializedName("available") val available: Boolean = true,
    @SerializedName("co2Avoided") val co2Avoided: Double = 0.0,
    @SerializedName("greenPoints") val greenPoints: Int = 0,
    @SerializedName("description") val description: String? = null,
    @SerializedName("primaryImageFileId") val primaryImageFileId: String? = null,
    @SerializedName("imageFileIds") val imageFileIds: List<String>? = null,
)
