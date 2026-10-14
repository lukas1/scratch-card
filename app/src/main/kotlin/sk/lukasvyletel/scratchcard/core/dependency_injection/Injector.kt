package sk.lukasvyletel.scratchcard.core.dependency_injection

import androidx.compose.runtime.staticCompositionLocalOf

interface Injector {
    val globalModule: GlobalModule
}

val LocalInjector = staticCompositionLocalOf<Injector> {
    error("Injector was not provided")
}
