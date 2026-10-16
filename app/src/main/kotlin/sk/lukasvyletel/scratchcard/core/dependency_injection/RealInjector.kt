package sk.lukasvyletel.scratchcard.core.dependency_injection

import kotlinx.serialization.json.Json
import sk.lukasvyletel.scratchcard.core.api.RetrofitClientBuilder
import sk.lukasvyletel.scratchcard.features.card.api.VersionService

class RealInjector(baseUrl: String) : Injector by SharedInjector(
    versionService = createVersionService(baseUrl),
)

fun createVersionService(baseUrl: String): VersionService =
    RetrofitClientBuilder.buildRetrofit(
        baseUrl = baseUrl,
        httpClient = RetrofitClientBuilder.buildHttpClient(),
        json = Json { ignoreUnknownKeys = true },
    ).create(VersionService::class.java)
