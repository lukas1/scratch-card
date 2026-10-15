package sk.lukasvyletel.scratchcard.core.dependency_injection

import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import sk.lukasvyletel.scratchcard.core.annotations.ExcludeFromCoverage
import sk.lukasvyletel.scratchcard.core.api.RetrofitClientBuilder

class RealInjector : Injector by SharedInjector() {
    private val httpClient: OkHttpClient = RetrofitClientBuilder.buildHttpClient()
    private val json: Json = Json { ignoreUnknownKeys = true }

    @Suppress("UnusedPrivateMember")
    @ExcludeFromCoverage
    private fun <T> createApi(baseUrl: String, serviceInterface: Class<T>): T =
        RetrofitClientBuilder.buildRetrofit(baseUrl, httpClient, json).create(serviceInterface)
}
