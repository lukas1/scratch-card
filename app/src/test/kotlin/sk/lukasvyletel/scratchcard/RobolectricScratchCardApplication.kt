package sk.lukasvyletel.scratchcard

import sk.lukasvyletel.scratchcard.core.dependency_injection.Injector
import sk.lukasvyletel.scratchcard.features.card.test.cardTestInjector

class RobolectricScratchCardApplication : ScratchCardApplication() {
    override fun createInjector(): Injector = cardTestInjector()
}
