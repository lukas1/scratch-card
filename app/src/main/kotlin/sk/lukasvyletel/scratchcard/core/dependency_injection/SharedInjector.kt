package sk.lukasvyletel.scratchcard.core.dependency_injection

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import sk.lukasvyletel.scratchcard.features.card.api.VersionService
import sk.lukasvyletel.scratchcard.features.activate.dependency_injection.ActivateCardModule
import sk.lukasvyletel.scratchcard.features.card.DelayFunction
import sk.lukasvyletel.scratchcard.features.card.ScratchCardCodeGenerator
import sk.lukasvyletel.scratchcard.features.card.coroutineDelay
import sk.lukasvyletel.scratchcard.features.card.dependency_injection.ScratchCardModule
import kotlin.uuid.Uuid

class SharedInjector(
    private val versionService: VersionService,
    scratchCardCodeGenerator: ScratchCardCodeGenerator = { Uuid.random().toString() },
    delayFunction: DelayFunction = coroutineDelay,
    cpuBoundCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val activationCoroutineScope: CoroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO,
    ),
) : Injector {
    override val globalModule: GlobalModule by lazy { GlobalModule() }

    override val scratchCardModule: ScratchCardModule by lazy {
        ScratchCardModule(
            scratchCardCodeGenerator = scratchCardCodeGenerator,
            delayFunction = delayFunction,
            cpuBoundCoroutineDispatcher = cpuBoundCoroutineDispatcher,
            activationCoroutineScope = activationCoroutineScope,
            versionService = versionService,
        )
    }

    override val activateCardModule: ActivateCardModule by lazy {
        ActivateCardModule(scratchCardRepository = scratchCardModule.scratchCardRepository)
    }
}
