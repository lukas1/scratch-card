package sk.lukasvyletel.scratchcard.features.scratch.viewmodel

import androidx.lifecycle.ViewModel
import sk.lukasvyletel.scratchcard.features.card.ScratchCardRepository

class ScratchCardViewModel(
    private val scratchCardRepository: ScratchCardRepository,
) : ViewModel() {
    fun scratchCard() {
        scratchCardRepository.scratchCard()
    }
}
