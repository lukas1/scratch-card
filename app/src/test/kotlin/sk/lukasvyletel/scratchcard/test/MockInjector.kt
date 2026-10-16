package sk.lukasvyletel.scratchcard.test

import sk.lukasvyletel.scratchcard.core.dependency_injection.Injector
import sk.lukasvyletel.scratchcard.core.dependency_injection.SharedInjector
import sk.lukasvyletel.scratchcard.features.card.ScratchCardCodeGenerator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MockInjector(
    scratchCardCodeGenerator: ScratchCardCodeGenerator = { TEST_SCRATCH_CODE },
    cpuBoundCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : Injector by SharedInjector(
    scratchCardCodeGenerator = scratchCardCodeGenerator,
    cpuBoundCoroutineDispatcher = cpuBoundCoroutineDispatcher,
) {
    companion object {
        const val TEST_SCRATCH_CODE = "c533829f-cd4a-433d-9886-05f9b613db57"
    }
}
