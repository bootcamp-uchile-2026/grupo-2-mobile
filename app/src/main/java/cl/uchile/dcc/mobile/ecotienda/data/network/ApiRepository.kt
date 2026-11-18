package cl.uchile.dcc.mobile.ecotienda.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiRepository {
    // La base termina en '/'. /api/ solo sirve Swagger; los datos están en la raíz.
    private const val BASE_URL = "http://159.112.150.143:3003/"

    // Instancia compartida de Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Para convertir los JSON a DTOs
            .build()
    }

    // Instancia para Productos
    val productApiService: ProductApiService by lazy {
        retrofit.create(ProductApiService::class.java)
    }

    // Instancia para Productores
    val producerApiService: ProducerApiService by lazy {
        retrofit.create(ProducerApiService::class.java)
    }

    // Instancia para Usuarios
    val userApiService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }

    // Instancia para el Carrito
    val cartApiService: CartApiService by lazy {
        retrofit.create(CartApiService::class.java)
    }

    val articleApiService: ArticleApiService by lazy {
        retrofit.create(ArticleApiService::class.java)
    }
}