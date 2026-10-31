package sk.lukasvyletel.scratchcard.test

import sk.lukasvyletel.scratchcard.core.dependency_injection.Bindings
import sk.lukasvyletel.scratchcard.core.dependency_injection.GlobalBindings
import sk.lukasvyletel.scratchcard.features.card.dependency_injection.ScratchCardBindings

interface TestBindings : Bindings {
    override fun globalBindings(): GlobalBindings = error("Global bindings were not provided")

    override fun scratchCardBindings(): ScratchCardBindings =
        error("Scratch card bindings were not provided")
}
