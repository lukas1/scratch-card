package sk.lukasvyletel.scratchcard

import sk.lukasvyletel.scratchcard.core.dependency_injection.Injector
import sk.lukasvyletel.scratchcard.test.MockInjector

class RobolectricScratchCardApplication : ScratchCardApplication() {
    override fun createInjector(): Injector = MockInjector()
}
