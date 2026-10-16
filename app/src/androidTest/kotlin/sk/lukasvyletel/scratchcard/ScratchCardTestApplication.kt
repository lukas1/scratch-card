package sk.lukasvyletel.scratchcard

import sk.lukasvyletel.scratchcard.core.dependency_injection.Injector
import sk.lukasvyletel.scratchcard.core.dependency_injection.SharedInjector
import sk.lukasvyletel.scratchcard.features.card.api.ActivationVersion
import sk.lukasvyletel.scratchcard.features.card.api.ScratchCardApi
import sk.lukasvyletel.scratchcard.features.card.api.VersionService

class ScratchCardTestApplication : ScratchCardApplication() {
    override fun createInjector(): Injector = SharedInjector(
        scratchCardCodeGenerator = { "instrumented-test-code" },
        delayFunction = {},
        versionService = VersionService {
            ActivationVersion((ScratchCardApi.ACTIVATION_VERSION_THRESHOLD + 1).toString())
        },
    )
}
