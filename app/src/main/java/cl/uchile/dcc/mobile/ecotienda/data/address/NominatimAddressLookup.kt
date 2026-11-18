package cl.uchile.dcc.mobile.ecotienda.data.address

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

// Cliente de la búsqueda gratuita de OpenStreetMap
// Genera 3 posibles resultados de ls búsqueda: Found, notfound y unavailable
class NominatimAddressLookup(
    private val client: OkHttpClient = OkHttpClient.Builder()
        .callTimeout(8, TimeUnit.SECONDS)
        .build(),
) {
    // Genera una consulta con calle, número y comuna, limitada a Chile (countrycodes=cl).
    suspend fun lookup(
        street: String,
        number: String,
        noNumber: Boolean,
        commune: String,
    ): AddressLookupResult = withContext(Dispatchers.IO) {
        runCatching { search(street, number, noNumber, commune) }
            .getOrElse { AddressLookupResult.Unavailable }
    }


    private fun search(
        street: String,
        number: String,
        noNumber: Boolean,
        commune: String,
    ): AddressLookupResult {
        val streetQuery = if (noNumber || number.isBlank()) {
            street.trim()
        } else {
            "${number.trim()} ${street.trim()}"
        }
        val url = "https://nominatim.openstreetmap.org/search".toHttpUrl().newBuilder()
            .addQueryParameter("format", "jsonv2")
            .addQueryParameter("addressdetails", "1")
            .addQueryParameter("limit", "5")
            .addQueryParameter("countrycodes", "cl")
            .addQueryParameter("street", streetQuery)
            .addQueryParameter("city", commune.trim())
            .build()
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", USER_AGENT)
            .header("Accept", "application/json")
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return AddressLookupResult.Unavailable
            val body = response.body.string()
            val hits = JsonParser.parseString(body).asJsonArray
            val wanted = ChileCommunes.normalize(commune)
            val match = hits.firstOrNull { element ->
                matchesCommune(element.asJsonObject, wanted)
            }?.asJsonObject ?: return AddressLookupResult.NotFound
            val label = match.get("display_name")?.asString
                ?.substringBefore(", Chile")
                .orEmpty()
            return AddressLookupResult.Found(label)
        }
    }

    private fun matchesCommune(hit: JsonObject, wanted: String): Boolean {
        val display = hit.get("display_name")?.asString.orEmpty()
        return display.split(",").any { ChileCommunes.normalize(it) == wanted }
    }

    companion object {
        const val USER_AGENT =
            "EcoTienda/1.0 (cl.uchile.dcc.mobile.ecotienda; educational checkout)"
    }
}

sealed interface AddressLookupResult {
    data class Found(val label: String) : AddressLookupResult
    data object NotFound : AddressLookupResult
    data object Unavailable : AddressLookupResult
}
