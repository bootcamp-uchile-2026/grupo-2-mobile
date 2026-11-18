package cl.uchile.dcc.mobile.ecotienda.data.network.dto

import com.google.gson.annotations.SerializedName

data class CartItemDto(
    @SerializedName("productId") val productId: String,
    @SerializedName("productName") val productName: String,
    @SerializedName("price") val price: Int,
    @SerializedName("imageUrl") val imageUrl: String? = null,
    @SerializedName("producerID") val producerId: String? = null,
    @SerializedName("productProducer") val productProducer: String? = null,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("subtotal") val subtotal: Int? = null
)
