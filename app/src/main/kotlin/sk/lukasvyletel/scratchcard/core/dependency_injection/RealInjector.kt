package sk.lukasvyletel.scratchcard.core.dependency_injection

class RealInjector(baseUrl: String) : Injector by SharedInjector(AppBindings(baseUrl))
