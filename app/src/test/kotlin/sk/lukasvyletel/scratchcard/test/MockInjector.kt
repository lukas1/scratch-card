package sk.lukasvyletel.scratchcard.test

import sk.lukasvyletel.scratchcard.core.dependency_injection.GlobalModule
import sk.lukasvyletel.scratchcard.core.dependency_injection.Injector

class MockInjector : Injector {
    override val globalModule: GlobalModule = GlobalModule()
}
