package sk.lukasvyletel.scratchcard.features.activate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import sk.lukasvyletel.scratchcard.core.coroutines.WHILE_SUBSCRIBED_STOP_TIMEOUT_FOR_CONFIGURATION_CHANGES_MILLIS
import sk.lukasvyletel.scratchcard.features.card.repository.ScratchCardRepository
import sk.lukasvyletel.scratchcard.features.card.model.ActivationError
import sk.lukasvyletel.scratchcard.features.card.model.ScratchCardState

class ActivateCardViewModel(
    private val scratchCardRepository: ScratchCardRepository,
) : ViewModel() {
    private val _activationButtonState = MutableStateFlow(
        activationButtonStateFor(
            scratchCardRepository.cardStateFlow.value,
            scratchCardRepository.activationInProgressFlow.value,
        ),
    )
    val activationButtonState: StateFlow<ActivationButtonState> = _activationButtonState.asStateFlow()
    val activationError: StateFlow<ActivationError?> = scratchCardRepository.activationErrorFlow

    init {
        viewModelScope.launch {
            combine(
                scratchCardRepository.cardStateFlow,
                scratchCardRepository.activationInProgressFlow,
                ::activationButtonStateFor,
            ).collect { state -> _activationButtonState.value = state }
        }
    }

    val code: StateFlow<String?> = scratchCardRepository.cardStateFlow
        .map { cardState -> cardState.codeOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(
                WHILE_SUBSCRIBED_STOP_TIMEOUT_FOR_CONFIGURATION_CHANGES_MILLIS,
            ),
            initialValue = scratchCardRepository.cardStateFlow.value.codeOrNull(),
        )

    fun activateCard() {
        if (_activationButtonState.value != ActivationButtonState.READY) return

        val scratchedCard = scratchCardRepository.cardStateFlow.value as? ScratchCardState.Scratched
            ?: return

        _activationButtonState.value = ActivationButtonState.ACTIVATING
        scratchCardRepository.activateCard(scratchedCard)
    }

    fun dismissActivationError() {
        scratchCardRepository.dismissActivationError()
    }
}

private fun buttonStateFor(cardState: ScratchCardState): ActivationButtonState = when (cardState) {
    is ScratchCardState.Activated -> ActivationButtonState.ACTIVATED
    is ScratchCardState.Unscratched,
    is ScratchCardState.Scratched -> ActivationButtonState.READY
}

private fun activationButtonStateFor(
    cardState: ScratchCardState,
    isActivationInProgress: Boolean,
): ActivationButtonState = when {
    isActivationInProgress -> ActivationButtonState.ACTIVATING
    else -> buttonStateFor(cardState)
}

private fun ScratchCardState.codeOrNull(): String? = when (this) {
    is ScratchCardState.WithCode -> code
    ScratchCardState.Unscratched -> null
}
