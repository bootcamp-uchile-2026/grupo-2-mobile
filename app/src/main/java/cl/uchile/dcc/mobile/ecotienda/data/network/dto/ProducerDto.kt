package cl.uchile.dcc.mobile.ecotienda.data.network.dto

import com.google.gson.annotations.SerializedName

data class ProducerDto(
    @SerializedName("id") val id: String,
    @SerializedName("producerName") val producerName: String,
    @SerializedName("producerDescription") val producerDescription: String,
    @SerializedName("producerImageUrl") val producerImageUrl: String,
    @SerializedName("producerLocation") val producerLocation: String,
    @SerializedName("producerHistory") val producerHistory: String
)
