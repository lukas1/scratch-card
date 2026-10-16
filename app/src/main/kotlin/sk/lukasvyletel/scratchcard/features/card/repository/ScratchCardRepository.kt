package sk.lukasvyletel.scratchcard.features.card.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
    private val scratchMutex = Mutex()
    private val activationMutex = Mutex()

    suspend fun scratchCard() {
        scratchMutex.withLock {
            if (_cardStateFlow.value != ScratchCardState.Unscratched) return@withLock

            _cardStateFlow.emit(ScratchCardState.Scratched(code = cardScratcher.scratch()))
        }
    }

    fun activateCard(scratchedCard: ScratchCardState.Scratched): Job =
        activationCoroutineScope.launch {
            activationMutex.withLock {
                if (_cardStateFlow.value != scratchedCard) return@withLock

                _activationErrorFlow.value = null
                scratchCardApi.activateCard(scratchedCard.code).fold(
                    onSuccess = { isActivated ->
                        if (isActivated) {
                            _cardStateFlow.update {
                                ScratchCardState.Activated(code = scratchedCard.code)
                            }
                        } else {
                            _activationErrorFlow.value = ActivationError.ACTIVATION_FAILED
                        }
                    },
                    onFailure = { _activationErrorFlow.value = ActivationError.UNEXPECTED_ERROR },
                )
            }
        }

    fun dismissActivationError() {
        _activationErrorFlow.value = null
    }
}
