package sk.lukasvyletel.scratchcard.features.card.dependency_injection

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import sk.lukasvyletel.scratchcard.core.api.RetrofitClientBuilder
import sk.lukasvyletel.scratchcard.features.card.api.ScratchCardCodeGenerator
import sk.lukasvyletel.scratchcard.features.card.api.VersionService
import kotlin.uuid.Uuid

class ScratchCardBindings(
    private val versionServiceFactory: () -> VersionService,
    val scratchCardCodeGenerator: ScratchCardCodeGenerator = { Uuid.random().toString() },
    val activationCoroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
) {
    fun versionService(): VersionService = versionServiceFactory()
}

fun createVersionService(baseUrl: String): VersionService =
    RetrofitClientBuilder.buildRetrofit(
        baseUrl = baseUrl,
        httpClient = RetrofitClientBuilder.buildHttpClient(),
        json = Json { ignoreUnknownKeys = true },
    ).create(VersionService::class.java)
