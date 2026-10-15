package sk.lukasvyletel.scratchcard.features.scratch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import sk.lukasvyletel.scratchcard.core.coroutines.WHILE_SUBSCRIBED_STOP_TIMEOUT_FOR_CONFIGURATION_CHANGES_MILLIS
import sk.lukasvyletel.scratchcard.features.card.ScratchCardRepository
import sk.lukasvyletel.scratchcard.features.card.ScratchCardState

class ScratchCardViewModel(
    private val scratchCardRepository: ScratchCardRepository,
) : ViewModel() {
    val code: StateFlow<String?> = scratchCardRepository.cardStateFlow
        .map { cardState ->
            when (cardState) {
                is ScratchCardState.WithCode -> cardState.code
                ScratchCardState.Unscratched -> null
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(
                WHILE_SUBSCRIBED_STOP_TIMEOUT_FOR_CONFIGURATION_CHANGES_MILLIS,
            ),
            initialValue = scratchCardRepository.cardStateFlow.value.codeOrNull(),
        )

    fun scratchCard() {
        scratchCardRepository.scratchCard()
    }
}

private fun ScratchCardState.codeOrNull(): String? = when (this) {
    is ScratchCardState.WithCode -> code
    ScratchCardState.Unscratched -> null
}
