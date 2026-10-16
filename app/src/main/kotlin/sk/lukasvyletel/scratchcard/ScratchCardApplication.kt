package sk.lukasvyletel.scratchcard

import android.app.Application
import sk.lukasvyletel.scratchcard.core.dependency_injection.Injector
import sk.lukasvyletel.scratchcard.core.dependency_injection.RealInjector

open class ScratchCardApplication : Application() {
    private val versionServiceBaseUrl = VersionServiceBaseUrl()
    private val injectorLock = Any()
    private var currentInjector: Injector? = null

    override fun onCreate() {
        super.onCreate()
        injector()
    }

    protected open fun createInjector(): Injector = RealInjector(versionServiceBaseUrl.get())

    fun injector(): Injector = synchronized(injectorLock) {
        currentInjector ?: createInjector().also { currentInjector = it }
    }
}
