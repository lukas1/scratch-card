package sk.lukasvyletel.scratchcard.core.dependency_injection

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import sk.lukasvyletel.scratchcard.core.coroutines.DelayFunction
import sk.lukasvyletel.scratchcard.core.coroutines.coroutineDelay
import sk.lukasvyletel.scratchcard.features.card.dependency_injection.ScratchCardBindings

interface Bindings {
    fun globalBindings(): GlobalBindings
    fun scratchCardBindings(): ScratchCardBindings
}

class GlobalBindings(
    val delayFunction: DelayFunction = coroutineDelay,
    val cpuBoundCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
)
