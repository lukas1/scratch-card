package sk.lukasvyletel.scratchcard.core.dependency_injection

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import sk.lukasvyletel.scratchcard.AppRoute
import sk.lukasvyletel.scratchcard.HomeRoute
import sk.lukasvyletel.scratchcard.core.api.RetrofitClientBuilder

class GlobalModule {
    private val httpClient: OkHttpClient = RetrofitClientBuilder.buildHttpClient()
    private val json: Json = Json { ignoreUnknownKeys = true }
    val backStack: SnapshotStateList<AppRoute> = mutableStateListOf(HomeRoute)

    @Suppress("UnusedPrivateMember")
    private fun <T> createApi(baseUrl: String, serviceInterface: Class<T>): T =
        RetrofitClientBuilder.buildRetrofit(baseUrl, httpClient, json).create(serviceInterface)
}
