package cl.uchile.dcc.mobile.ecotienda.data.network

import cl.uchile.dcc.mobile.ecotienda.data.network.dto.PageDto
import cl.uchile.dcc.mobile.ecotienda.data.network.dto.ProducerDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProducerApiService {
    @GET("producers")
    suspend fun getProducers(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): PageDto<ProducerDto>

    @GET("producers/{id}")
    suspend fun getProducerById(@Path("id") id: String): ProducerDto
}
