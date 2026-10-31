package sk.lukasvyletel.scratchcard.core.dependency_injection

import sk.lukasvyletel.scratchcard.features.card.dependency_injection.ScratchCardBindings
import sk.lukasvyletel.scratchcard.features.card.dependency_injection.createVersionService

class AppBindings(private val baseUrl: String) : Bindings {
    override fun globalBindings(): GlobalBindings = GlobalBindings()

    override fun scratchCardBindings(): ScratchCardBindings = ScratchCardBindings(
        versionServiceFactory = { createVersionService(baseUrl) },
    )
}
