package cl.uchile.dcc.mobile.ecotienda.data.network

import cl.uchile.dcc.mobile.ecotienda.data.network.dto.ProductDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {

    // Obtener productos
    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    // Obtener producto por su ID
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): ProductDto

    // Buscar productos
    @GET("products/search")
    suspend fun searchProducts(@Query("q") query: String): List<ProductDto>

    // Eliminar producto
    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: String)
}


