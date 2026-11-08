package cl.uchile.dcc.mobile.ecotienda.data.network

import cl.uchile.dcc.mobile.ecotienda.data.network.dto.ProducerDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProducerApiService {
    @GET("producers")
    suspend fun getProducers(): List<ProducerDto>

    @GET("producers/{id}")
    suspend fun getProducerById(@Path("id") id: String): ProducerDto

    // Buscar productores
    @GET("producers/search")
    suspend fun searchProducers(@Query("q") query: String): List<ProducerDto>
}