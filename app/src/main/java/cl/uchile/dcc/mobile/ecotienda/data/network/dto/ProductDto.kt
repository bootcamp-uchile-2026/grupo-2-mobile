package cl.uchile.dcc.mobile.ecotienda.data.network.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("id") val id: String,
    @SerializedName("productName") val productName: String,
    @SerializedName("price") val price: Int,
    @SerializedName("description") val description: String,
    @SerializedName("productProducer") val productProducer: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("stock") val stock: Int,
    @SerializedName("category") val category: String,
    @SerializedName("producerID") val producerId: String
)