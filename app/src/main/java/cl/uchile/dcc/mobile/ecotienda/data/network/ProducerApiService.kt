package cl.uchile.dcc.mobile.ecotienda.data.network

import cl.uchile.dcc.mobile.ecotienda.data.network.dto.ProducerDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ProducerApiService {
    @GET("emprendedores")
    suspend fun getProducers(): List<ProducerDto>

    @GET("emprendedores/{id}")
    suspend fun getProducerById(@Path("id") id: String): ProducerDto
}
