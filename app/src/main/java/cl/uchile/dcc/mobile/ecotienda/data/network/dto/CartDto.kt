package cl.uchile.dcc.mobile.ecotienda.data.network.dto

import com.google.gson.annotations.SerializedName

data class CartDto(
    @SerializedName("id") val id: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("items") val items: List<CartItemDto>,
    @SerializedName("total") val total: Int,
    @SerializedName("itemCount") val itemCount: Int
)

// Petición para agregar producto al carro
data class AddToCartRequest(
    @SerializedName("productId") val productId: String,
    @SerializedName("quantity") val quantity: Int = 1
)

// Petición para modificar la cantidad
data class UpdateQuantityRequest(
    @SerializedName("quantity") val quantity: Int
)