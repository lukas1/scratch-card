package sk.lukasvyletel.scratchcard.core.dependency_injection

import androidx.compose.runtime.staticCompositionLocalOf
import sk.lukasvyletel.scratchcard.features.activate.dependency_injection.ActivateCardModule
import sk.lukasvyletel.scratchcard.features.card.dependency_injection.ScratchCardModule

interface Injector {
    val globalModule: GlobalModule
    val scratchCardModule: ScratchCardModule
    val activateCardModule: ActivateCardModule
}

val LocalInjector = staticCompositionLocalOf<Injector> {
    error("Injector was not provided")
}
