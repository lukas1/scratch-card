package sk.lukasvyletel.scratchcard.features.card

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import sk.lukasvyletel.scratchcard.features.card.api.ScratchCardApi
import kotlin.time.Duration.Companion.milliseconds

private const val SCRATCH_CARD_DELAY_MILLIS = 2_000L

class ScratchCardRepository(
    private val scratchCardCodeGenerator: ScratchCardCodeGenerator,
    private val delayFunction: DelayFunction,
    private val cpuBoundCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val activationCoroutineScope: CoroutineScope,
    private val scratchCardApi: ScratchCardApi,
) {
    private val _cardStateFlow: MutableStateFlow<ScratchCardState> = MutableStateFlow(
        ScratchCardState.Unscratched
    )
    val cardStateFlow: StateFlow<ScratchCardState> = _cardStateFlow.asStateFlow()
    private val _activationErrorFlow = MutableStateFlow(false)
    val activationErrorFlow: StateFlow<Boolean> = _activationErrorFlow.asStateFlow()
    private val scratchMutex = Mutex()
    private val activationMutex = Mutex()

    // Simulates a CPU-bound operation that takes time to complete.
    // The assignment mentions this should be a heavy operation
    // This code interprets the assignment that it means the code is CPU heavy
    suspend fun scratchCard() = withContext(cpuBoundCoroutineDispatcher) {
        scratchMutex.withLock {
            if (_cardStateFlow.value != ScratchCardState.Unscratched) return@withLock

            delayFunction(SCRATCH_CARD_DELAY_MILLIS.milliseconds)
            _cardStateFlow.emit(ScratchCardState.Scratched(code = scratchCardCodeGenerator()))
        }
    }

    fun activateCard(scratchedCard: ScratchCardState.Scratched): Job =
        activationCoroutineScope.launch {
            activationMutex.withLock {
                if (_cardStateFlow.value != scratchedCard) return@withLock

                _activationErrorFlow.value = false
                scratchCardApi.activateCard(scratchedCard.code).fold(
                    onSuccess = { isActivated ->
                        if (isActivated) {
                            _cardStateFlow.update {
                                ScratchCardState.Activated(code = scratchedCard.code)
                            }
                        } else {
                            _activationErrorFlow.value = true
                        }
                    },
                    onFailure = { _activationErrorFlow.value = true },
                )
            }
        }

    fun dismissActivationError() {
        _activationErrorFlow.value = false
    }
}
