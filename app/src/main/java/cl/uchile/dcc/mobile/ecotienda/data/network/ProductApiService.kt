package cl.uchile.dcc.mobile.ecotienda.data.network

import cl.uchile.dcc.mobile.ecotienda.data.network.dto.ProductDetailDto
import cl.uchile.dcc.mobile.ecotienda.data.network.dto.ProductListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {

    // La API no tiene un listado general. /productos/nuevos es el conjunto más amplio sin filtro.
    @GET("productos/nuevos")
    suspend fun getProducts(): List<ProductListDto>

    @GET("productos/{id}")
    suspend fun getProductById(@Path("id") id: String): ProductDetailDto

    @GET("productos")
    suspend fun searchProducts(@Query("buscar") query: String): List<ProductListDto>
}
