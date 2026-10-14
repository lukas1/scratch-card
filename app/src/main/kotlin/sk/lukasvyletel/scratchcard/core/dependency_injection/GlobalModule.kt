package sk.lukasvyletel.scratchcard.core.dependency_injection

import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import sk.lukasvyletel.scratchcard.core.api.RetrofitClientBuilder

class GlobalModule(
    val httpClient: OkHttpClient = RetrofitClientBuilder.buildHttpClient(),
    val json: Json = Json { ignoreUnknownKeys = true },
) {
    fun <T> createApi(baseUrl: String, serviceInterface: Class<T>): T =
        RetrofitClientBuilder.buildRetrofit(baseUrl, httpClient, json).create(serviceInterface)
}
