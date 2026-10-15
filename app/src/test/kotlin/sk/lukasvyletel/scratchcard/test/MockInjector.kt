package sk.lukasvyletel.scratchcard.test

import sk.lukasvyletel.scratchcard.core.dependency_injection.Injector
import sk.lukasvyletel.scratchcard.core.dependency_injection.SharedInjector
import sk.lukasvyletel.scratchcard.features.card.ScratchCardCodeGenerator

class MockInjector(
    scratchCardCodeGenerator: ScratchCardCodeGenerator = { TEST_SCRATCH_CODE }
) : Injector by SharedInjector(
    scratchCardCodeGenerator = scratchCardCodeGenerator
) {
    companion object {
        const val TEST_SCRATCH_CODE = "c533829f-cd4a-433d-9886-05f9b613db57"
    }
}
