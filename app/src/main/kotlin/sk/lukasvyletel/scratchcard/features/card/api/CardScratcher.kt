package sk.lukasvyletel.scratchcard.features.card.api

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sk.lukasvyletel.scratchcard.core.coroutines.DelayFunction
import kotlin.time.Duration.Companion.seconds

private val SCRATCH_CARD_DELAY = 2.seconds

class CardScratcher(
    private val scratchCardCodeGenerator: ScratchCardCodeGenerator,
    private val delayFunction: DelayFunction,
    private val cpuBoundCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend fun scratch(): String = withContext(cpuBoundCoroutineDispatcher) {
        delayFunction(SCRATCH_CARD_DELAY)
        scratchCardCodeGenerator()
    }
}
