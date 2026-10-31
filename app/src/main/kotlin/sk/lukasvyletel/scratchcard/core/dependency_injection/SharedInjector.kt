package sk.lukasvyletel.scratchcard.core.dependency_injection

import sk.lukasvyletel.scratchcard.features.activate.dependency_injection.ActivateCardModule
import sk.lukasvyletel.scratchcard.features.card.dependency_injection.ScratchCardModule

class SharedInjector(private val bindings: Bindings) : Injector {
    private val modules = mutableMapOf<Class<*>, Lazy<Any>>()

    override val globalModule: GlobalModule
        get() = module(GlobalModule::class.java) { GlobalModule() }

    override val scratchCardModule: ScratchCardModule
        get() = module(ScratchCardModule::class.java) {
            ScratchCardModule(bindings.globalBindings(), bindings.scratchCardBindings())
        }

    override val activateCardModule: ActivateCardModule
        get() = module(ActivateCardModule::class.java) {
            ActivateCardModule(scratchCardRepository = scratchCardModule.scratchCardRepository)
        }

    private fun <T : Any> module(type: Class<T>, create: () -> T): T {
        val cached = synchronized(modules) {
            modules.getOrPut(type) { lazy { create() } }
        }
        return requireNotNull(type.cast(cached.value))
    }
}
