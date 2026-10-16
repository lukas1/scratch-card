package sk.lukasvyletel.scratchcard.features.activate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import sk.lukasvyletel.scratchcard.core.coroutines.WHILE_SUBSCRIBED_STOP_TIMEOUT_FOR_CONFIGURATION_CHANGES_MILLIS
import sk.lukasvyletel.scratchcard.features.card.ScratchCardRepository
import sk.lukasvyletel.scratchcard.features.card.ScratchCardState

class ActivateCardViewModel(
    private val scratchCardRepository: ScratchCardRepository,
) : ViewModel() {
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
        val scratchedCard = scratchCardRepository.cardStateFlow.value as? ScratchCardState.Scratched
            ?: return
        scratchCardRepository.activateCard(scratchedCard)
    }
}

private fun ScratchCardState.codeOrNull(): String? = when (this) {
    is ScratchCardState.WithCode -> code
    ScratchCardState.Unscratched -> null
}
