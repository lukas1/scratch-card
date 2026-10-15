package sk.lukasvyletel.scratchcard.features.card.dependency_injection

import sk.lukasvyletel.scratchcard.features.card.ScratchCardCodeGenerator
import sk.lukasvyletel.scratchcard.features.card.ScratchCardRepository

class ScratchCardModule(
    scratchCardCodeGenerator: ScratchCardCodeGenerator
) {
    val scratchCardRepository: ScratchCardRepository by lazy {
        ScratchCardRepository(
            scratchCardCodeGenerator = scratchCardCodeGenerator
        )
    }
}
