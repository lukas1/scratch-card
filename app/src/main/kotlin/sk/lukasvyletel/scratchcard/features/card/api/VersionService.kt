package sk.lukasvyletel.scratchcard.features.card.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

fun interface VersionService {
    @GET("version")
    suspend fun getVersion(@Query("code") code: String): ActivationVersion
}

@Serializable
data class ActivationVersion(
    @SerialName("android")
    val android: String,
)
