package sk.lukasvyletel.scratchcard.features.scratch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import sk.lukasvyletel.scratchcard.core.coroutines.WHILE_SUBSCRIBED_STOP_TIMEOUT_FOR_CONFIGURATION_CHANGES_MILLIS
import sk.lukasvyletel.scratchcard.features.card.repository.ScratchCardRepository
import sk.lukasvyletel.scratchcard.features.card.model.ScratchCardState

class ScratchCardViewModel(
    private val scratchCardRepository: ScratchCardRepository,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
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
        if (_isLoading.value || code.value != null) return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                scratchCardRepository.scratchCard()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

private fun ScratchCardState.codeOrNull(): String? = when (this) {
    is ScratchCardState.WithCode -> code
    ScratchCardState.Unscratched -> null
}
