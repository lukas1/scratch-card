package sk.lukasvyletel.scratchcard.features.activate.dependency_injection

import sk.lukasvyletel.scratchcard.features.card.ScratchCardRepository

class ActivateCardModule(
    val scratchCardRepository: ScratchCardRepository,
)
