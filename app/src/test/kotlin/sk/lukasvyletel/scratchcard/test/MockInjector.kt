package sk.lukasvyletel.scratchcard.test

import sk.lukasvyletel.scratchcard.core.dependency_injection.Injector
import sk.lukasvyletel.scratchcard.core.dependency_injection.SharedInjector
import sk.lukasvyletel.scratchcard.features.card.api.ActivationVersion
import sk.lukasvyletel.scratchcard.features.card.api.VersionService
import sk.lukasvyletel.scratchcard.features.card.api.ScratchCardApi
import kotlinx.coroutines.CoroutineScope
import sk.lukasvyletel.scratchcard.features.card.repository.ScratchCardCodeGenerator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MockInjector(
    scratchCardCodeGenerator: ScratchCardCodeGenerator = { TEST_SCRATCH_CODE },
    cpuBoundCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
    activationCoroutineScope: CoroutineScope = CoroutineScope(cpuBoundCoroutineDispatcher),
    versionService: VersionService = VersionService {
        ActivationVersion((ScratchCardApi.ACTIVATION_VERSION_THRESHOLD + 1).toString())
    },
) : Injector by SharedInjector(
    scratchCardCodeGenerator = scratchCardCodeGenerator,
    cpuBoundCoroutineDispatcher = cpuBoundCoroutineDispatcher,
    activationCoroutineScope = activationCoroutineScope,
    versionService = versionService,
) {
    companion object {
        const val TEST_SCRATCH_CODE = "c533829f-cd4a-433d-9886-05f9b613db57"
    }
}
