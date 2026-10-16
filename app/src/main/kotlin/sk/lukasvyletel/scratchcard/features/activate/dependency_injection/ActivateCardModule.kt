package sk.lukasvyletel.scratchcard.features.activate.dependency_injection

import sk.lukasvyletel.scratchcard.features.card.repository.ScratchCardRepository

class ActivateCardModule(
    val scratchCardRepository: ScratchCardRepository,
)
