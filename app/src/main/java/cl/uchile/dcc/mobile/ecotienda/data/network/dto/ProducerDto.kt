package cl.uchile.dcc.mobile.ecotienda.data.network.dto

import com.google.gson.annotations.SerializedName

data class ProducerDto(
    @SerializedName("id") val id: Int,
    @SerializedName("businessName") val businessName: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("logoFileId") val logoFileId: String? = null,
    @SerializedName("bannerFileId") val bannerFileId: String? = null,
    @SerializedName("location") val location: String? = null,
    @SerializedName("websiteUrl") val websiteUrl: String? = null,
)
