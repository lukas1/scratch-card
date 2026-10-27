package sk.lukasvyletel.scratchcard.features.card.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import sk.lukasvyletel.scratchcard.features.card.api.CardScratcher
import sk.lukasvyletel.scratchcard.features.card.api.ScratchCardApi
import sk.lukasvyletel.scratchcard.features.card.model.ActivationError
import sk.lukasvyletel.scratchcard.features.card.model.ScratchCardState

class ScratchCardRepository(
    private val activationCoroutineScope: CoroutineScope,
    private val scratchCardApi: ScratchCardApi,
    private val cardScratcher: CardScratcher,
) {
    private val _cardStateFlow: MutableStateFlow<ScratchCardState> = MutableStateFlow(
        ScratchCardState.Unscratched
    )
    val cardStateFlow: StateFlow<ScratchCardState> = _cardStateFlow.asStateFlow()
    private val _activationErrorFlow = MutableStateFlow<ActivationError?>(null)
    val activationErrorFlow: StateFlow<ActivationError?> = _activationErrorFlow.asStateFlow()
    private val _activationInProgressFlow = MutableStateFlow(false)
    val activationInProgressFlow: StateFlow<Boolean> = _activationInProgressFlow.asStateFlow()
    private val scratchMutex = Mutex()
    private val activationMutex = Mutex()

    suspend fun scratchCard() {
        scratchMutex.withLock {
            if (_cardStateFlow.value != ScratchCardState.Unscratched) return@withLock

            _cardStateFlow.emit(ScratchCardState.Scratched(code = cardScratcher.scratch()))
        }
    }

    fun activateCard(scratchedCard: ScratchCardState.Scratched) {
        if (!_activationInProgressFlow.compareAndSet(expect = false, update = true)) return

        activationCoroutineScope.launch {
            try {
                activationMutex.withLock {
                    if (_cardStateFlow.value != scratchedCard) return@withLock

                    _activationErrorFlow.value = null
                    val result = scratchCardApi.activateCard(scratchedCard.code)
                    if (_cardStateFlow.value != scratchedCard) return@withLock

                    result.fold(
                        onSuccess = { isActivated ->
                            if (isActivated) {
                                _cardStateFlow.compareAndSet(
                                    expect = scratchedCard,
                                    update = ScratchCardState.Activated(code = scratchedCard.code),
                                )
                            } else {
                                _activationErrorFlow.value = ActivationError.ACTIVATION_FAILED
                            }
                        },
                        onFailure = { _activationErrorFlow.value = ActivationError.UNEXPECTED_ERROR },
                    )
                }
            } finally {
                _activationInProgressFlow.value = false
            }
        }
    }

    fun resetCard() {
        _cardStateFlow.value = ScratchCardState.Unscratched
        _activationErrorFlow.value = null
    }

    fun dismissActivationError() {
        _activationErrorFlow.value = null
    }
}
