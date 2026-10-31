package sk.lukasvyletel.scratchcard.test

import sk.lukasvyletel.scratchcard.core.dependency_injection.Injector
import sk.lukasvyletel.scratchcard.core.dependency_injection.SharedInjector
import sk.lukasvyletel.scratchcard.core.dependency_injection.GlobalBindings
import sk.lukasvyletel.scratchcard.features.card.api.ActivationVersion
import sk.lukasvyletel.scratchcard.features.card.api.VersionService
import sk.lukasvyletel.scratchcard.features.card.api.ScratchCardApi
import kotlinx.coroutines.CoroutineScope
import sk.lukasvyletel.scratchcard.features.card.api.ScratchCardCodeGenerator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import sk.lukasvyletel.scratchcard.features.card.dependency_injection.ScratchCardBindings

class MockInjector(
    scratchCardCodeGenerator: ScratchCardCodeGenerator = { TEST_SCRATCH_CODE },
    cpuBoundCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
    activationCoroutineScope: CoroutineScope = CoroutineScope(cpuBoundCoroutineDispatcher),
    versionServiceFactory: () -> VersionService = {
        VersionService {
            ActivationVersion((ScratchCardApi.ACTIVATION_VERSION_THRESHOLD + 1).toString())
        }
    },
) : Injector by SharedInjector(object : TestBindings {
    override fun globalBindings(): GlobalBindings = GlobalBindings(
        cpuBoundCoroutineDispatcher = cpuBoundCoroutineDispatcher,
    )

    override fun scratchCardBindings(): ScratchCardBindings = ScratchCardBindings(
        versionServiceFactory = versionServiceFactory,
        scratchCardCodeGenerator = scratchCardCodeGenerator,
        activationCoroutineScope = activationCoroutineScope,
    )
}) {
    companion object {
        const val TEST_SCRATCH_CODE = "c533829f-cd4a-433d-9886-05f9b613db57"
    }
}
