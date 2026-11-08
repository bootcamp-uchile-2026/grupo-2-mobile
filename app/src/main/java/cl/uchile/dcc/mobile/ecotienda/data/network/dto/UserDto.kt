package cl.uchile.dcc.mobile.ecotienda.data.network.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("profileImageUrl") val profileImageUrl: String,
    @SerializedName("password") val password: String
)
