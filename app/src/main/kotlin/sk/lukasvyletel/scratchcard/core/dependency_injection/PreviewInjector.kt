package sk.lukasvyletel.scratchcard.core.dependency_injection

import sk.lukasvyletel.scratchcard.core.annotations.ExcludeFromCoverage
import sk.lukasvyletel.scratchcard.features.card.api.ActivationVersion
import sk.lukasvyletel.scratchcard.features.card.api.ScratchCardApi

@ExcludeFromCoverage
class PreviewInjector : Injector by SharedInjector(
    versionService = {
        ActivationVersion((ScratchCardApi.ACTIVATION_VERSION_THRESHOLD + 1).toString())
    },
)
