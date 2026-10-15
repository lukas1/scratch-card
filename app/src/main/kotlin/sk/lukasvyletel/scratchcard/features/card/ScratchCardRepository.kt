package sk.lukasvyletel.scratchcard.features.card

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScratchCardRepository(
    private val scratchCardCodeGenerator: ScratchCardCodeGenerator
) {
    private val _cardStateFlow: MutableStateFlow<ScratchCardState> = MutableStateFlow(
        ScratchCardState.Unscratched
    )
    val cardStateFlow: StateFlow<ScratchCardState> = _cardStateFlow.asStateFlow()

    fun scratchCard() {
        _cardStateFlow.tryEmit(
            ScratchCardState.Scratched(
                code = scratchCardCodeGenerator()
            )
        )
    }

    fun activateCard(scratchedCard: ScratchCardState.Scratched) {
        _cardStateFlow.update {
            ScratchCardState.Activated(
                code = scratchedCard.code
            )
        }
    }
}
