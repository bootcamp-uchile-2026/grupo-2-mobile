package cl.uchile.dcc.mobile.ecotienda.data.network

import cl.uchile.dcc.mobile.ecotienda.data.network.dto.ProducerDto
import cl.uchile.dcc.mobile.ecotienda.data.network.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApiService {
    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): UserDto

    @POST("users")
    suspend fun createUser(@Body user: UserDto): UserDto

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String)

}