package sk.lukasvyletel.scratchcard.features.card.dependency_injection

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import sk.lukasvyletel.scratchcard.features.card.api.CardScratcher
import sk.lukasvyletel.scratchcard.features.card.api.VersionService
import sk.lukasvyletel.scratchcard.features.card.api.ScratchCardApi
import sk.lukasvyletel.scratchcard.features.card.api.ScratchCardCodeGenerator
import sk.lukasvyletel.scratchcard.core.coroutines.DelayFunction
import sk.lukasvyletel.scratchcard.features.card.repository.ScratchCardRepository
import sk.lukasvyletel.scratchcard.core.coroutines.coroutineDelay

class ScratchCardModule(
    scratchCardCodeGenerator: ScratchCardCodeGenerator,
    delayFunction: DelayFunction = coroutineDelay,
    cpuBoundCoroutineDispatcher: CoroutineDispatcher,
    activationCoroutineScope: CoroutineScope,
    versionService: VersionService,
) {
    private val cardScratcher: CardScratcher by lazy {
        CardScratcher(
            scratchCardCodeGenerator = scratchCardCodeGenerator,
            delayFunction = delayFunction,
            cpuBoundCoroutineDispatcher = cpuBoundCoroutineDispatcher,
        )
    }
    private val scratchCardApi: ScratchCardApi by lazy {
        ScratchCardApi(versionService)
    }
    val scratchCardRepository: ScratchCardRepository by lazy {
        ScratchCardRepository(
            activationCoroutineScope = activationCoroutineScope,
            scratchCardApi = scratchCardApi,
            cardScratcher = cardScratcher,
        )
    }
}
