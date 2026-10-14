package sk.lukasvyletel.scratchcard.core.dependency_injection

class RealInjector : Injector {
    override val globalModule: GlobalModule by lazy { GlobalModule() }
}
