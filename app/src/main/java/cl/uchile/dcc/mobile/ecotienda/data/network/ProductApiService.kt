package cl.uchile.dcc.mobile.ecotienda.data.network

import cl.uchile.dcc.mobile.ecotienda.data.network.dto.PageDto
import cl.uchile.dcc.mobile.ecotienda.data.network.dto.ProductDetailDto
import cl.uchile.dcc.mobile.ecotienda.data.network.dto.ProductListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {

    @GET("products/new")
    suspend fun getProducts(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 64,
    ): PageDto<ProductListDto>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): ProductDetailDto

    @GET("products")
    suspend fun searchProducts(
        @Query("search") query: String,
        @Query("size") size: Int = 20,
    ): PageDto<ProductListDto>
}
