package cl.uchile.dcc.mobile.ecotienda.data.network.dto

import com.google.gson.annotations.SerializedName

// Listado de GET /productos/nuevos y búsqueda GET /productos?buscar=
data class ProductListDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Int,
    @SerializedName("salePrice") val salePrice: Int? = null,
    @SerializedName("imageUrl") val imageUrl: String? = null,
    @SerializedName("brand") val brand: String? = null,
)

// Detalle de GET /productos/{id}
data class ProductDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Int,
    @SerializedName("description") val description: String? = null,
    @SerializedName("images") val images: List<String>? = null,
    @SerializedName("colors") val colors: List<String>? = null,
    @SerializedName("sizes") val sizes: List<String>? = null,
)
