package sk.lukasvyletel.scratchcard.core.dependency_injection

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import sk.lukasvyletel.scratchcard.features.card.ScratchCardCodeGenerator
import sk.lukasvyletel.scratchcard.features.card.dependency_injection.ScratchCardModule
import kotlin.uuid.Uuid

class SharedInjector(
    scratchCardCodeGenerator: ScratchCardCodeGenerator = { Uuid.random().toString() },
    cpuBoundCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : Injector {
    override val globalModule: GlobalModule by lazy { GlobalModule() }

    override val scratchCardModule: ScratchCardModule by lazy {
        ScratchCardModule(
            scratchCardCodeGenerator = scratchCardCodeGenerator,
            cpuBoundCoroutineDispatcher = cpuBoundCoroutineDispatcher,
        )
    }
}
