package sk.lukasvyletel.scratchcard

import android.app.Application
import sk.lukasvyletel.scratchcard.core.dependency_injection.Injector
import sk.lukasvyletel.scratchcard.core.dependency_injection.RealInjector
import sk.lukasvyletel.scratchcard.debug.MockVersionServer

open class ScratchCardApplication : Application() {
    private val mockVersionServer = MockVersionServer()
    private val injectorLock = Any()
    private var currentInjector: Injector? = null

    override fun onCreate() {
        super.onCreate()
        injector()
    }

    override fun onTerminate() {
        mockVersionServer.stop()
        super.onTerminate()
    }

    protected open fun createInjector(): Injector = RealInjector(mockVersionServer.start())

    fun injector(): Injector = synchronized(injectorLock) {
        currentInjector ?: createInjector().also { currentInjector = it }
    }
}
