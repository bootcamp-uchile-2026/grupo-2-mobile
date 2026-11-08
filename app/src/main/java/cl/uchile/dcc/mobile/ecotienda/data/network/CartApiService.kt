package cl.uchile.dcc.mobile.ecotienda.data.network

import cl.uchile.dcc.mobile.ecotienda.data.network.dto.AddToCartRequest
import cl.uchile.dcc.mobile.ecotienda.data.network.dto.CartDto
import cl.uchile.dcc.mobile.ecotienda.data.network.dto.UpdateQuantityRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CartApiService {

    // Obtener el carrito actual del usuario
    @GET("cart")
    suspend fun getCart(): CartDto

    // Agregar un producto al carrito
    @POST("cart/items")
    suspend fun addToCart(@Body request: AddToCartRequest): CartDto

    // Actualizar la cantidad de un ítem (ej. incrementar o decrementar)
    @PATCH("cart/items/{productId}")
    suspend fun updateQuantity(
        @Path("productId") productId: String,
        @Body request: UpdateQuantityRequest
    ): CartDto

    // Eliminar un producto del carrito
    @DELETE("cart/items/{productId}")
    suspend fun removeFromCart(@Path("productId") productId: String): CartDto

    // Vaciar el carrito completo
    @DELETE("cart")
    suspend fun clearCart()
}