package sk.lukasvyletel.scratchcard.core.dependency_injection

import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import sk.lukasvyletel.scratchcard.core.api.RetrofitClientBuilder
import sk.lukasvyletel.scratchcard.core.navigation.Navigation

class GlobalModule {
    private val httpClient: OkHttpClient = RetrofitClientBuilder.buildHttpClient()
    private val json: Json = Json { ignoreUnknownKeys = true }

    val navigation: Navigation = Navigation()

    @Suppress("UnusedPrivateMember")
    private fun <T> createApi(baseUrl: String, serviceInterface: Class<T>): T =
        RetrofitClientBuilder.buildRetrofit(baseUrl, httpClient, json).create(serviceInterface)
}
