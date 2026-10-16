package sk.lukasvyletel.scratchcard.features.card.dependency_injection

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import sk.lukasvyletel.scratchcard.features.card.ScratchCardCodeGenerator
import sk.lukasvyletel.scratchcard.features.card.ScratchCardRepository

class ScratchCardModule(
    scratchCardCodeGenerator: ScratchCardCodeGenerator,
    cpuBoundCoroutineDispatcher: CoroutineDispatcher,
    activationCoroutineScope: CoroutineScope,
) {
    val scratchCardRepository: ScratchCardRepository by lazy {
        ScratchCardRepository(
            scratchCardCodeGenerator = scratchCardCodeGenerator,
            cpuBoundCoroutineDispatcher = cpuBoundCoroutineDispatcher,
            activationCoroutineScope = activationCoroutineScope,
        )
    }
}
