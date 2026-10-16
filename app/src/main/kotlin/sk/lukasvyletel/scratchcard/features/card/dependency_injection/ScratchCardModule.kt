package sk.lukasvyletel.scratchcard.features.card.dependency_injection

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import sk.lukasvyletel.scratchcard.features.card.api.VersionService
import sk.lukasvyletel.scratchcard.features.card.api.ScratchCardApi
import sk.lukasvyletel.scratchcard.features.card.ScratchCardCodeGenerator
import sk.lukasvyletel.scratchcard.features.card.DelayFunction
import sk.lukasvyletel.scratchcard.features.card.ScratchCardRepository
import sk.lukasvyletel.scratchcard.features.card.coroutineDelay

class ScratchCardModule(
    scratchCardCodeGenerator: ScratchCardCodeGenerator,
    delayFunction: DelayFunction = coroutineDelay,
    cpuBoundCoroutineDispatcher: CoroutineDispatcher,
    activationCoroutineScope: CoroutineScope,
    versionService: VersionService,
) {
    private val scratchCardApi: ScratchCardApi by lazy {
        ScratchCardApi(versionService)
    }
    val scratchCardRepository: ScratchCardRepository by lazy {
        ScratchCardRepository(
            scratchCardCodeGenerator = scratchCardCodeGenerator,
            delayFunction = delayFunction,
            cpuBoundCoroutineDispatcher = cpuBoundCoroutineDispatcher,
            activationCoroutineScope = activationCoroutineScope,
            scratchCardApi = scratchCardApi,
        )
    }
}
