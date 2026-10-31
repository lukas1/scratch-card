package sk.lukasvyletel.scratchcard.features.card.dependency_injection

import sk.lukasvyletel.scratchcard.core.dependency_injection.GlobalBindings
import sk.lukasvyletel.scratchcard.features.card.api.CardScratcher
import sk.lukasvyletel.scratchcard.features.card.api.ScratchCardApi
import sk.lukasvyletel.scratchcard.features.card.repository.ScratchCardRepository

class ScratchCardModule(
    private val globalBindings: GlobalBindings,
    private val scratchCardBindings: ScratchCardBindings,
) {
    private val cardScratcher: CardScratcher by lazy {
        CardScratcher(
            scratchCardCodeGenerator = scratchCardBindings.scratchCardCodeGenerator,
            delayFunction = globalBindings.delayFunction,
            cpuBoundCoroutineDispatcher = globalBindings.cpuBoundCoroutineDispatcher,
        )
    }
    private val scratchCardApi: ScratchCardApi by lazy {
        ScratchCardApi(scratchCardBindings.versionService())
    }
    val scratchCardRepository: ScratchCardRepository by lazy {
        ScratchCardRepository(
            activationCoroutineScope = scratchCardBindings.activationCoroutineScope,
            scratchCardApi = scratchCardApi,
            cardScratcher = cardScratcher,
        )
    }
}
