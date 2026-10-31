package sk.lukasvyletel.scratchcard

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import sk.lukasvyletel.scratchcard.core.coroutines.DelayFunction
import sk.lukasvyletel.scratchcard.core.dependency_injection.Injector
import sk.lukasvyletel.scratchcard.core.dependency_injection.SharedInjector
import sk.lukasvyletel.scratchcard.core.dependency_injection.Bindings
import sk.lukasvyletel.scratchcard.core.dependency_injection.GlobalBindings
import sk.lukasvyletel.scratchcard.features.card.dependency_injection.ScratchCardBindings
import sk.lukasvyletel.scratchcard.features.card.dependency_injection.createVersionService

class ScratchCardTestApplication : ScratchCardApplication() {
    val mockWebServer = MockWebServer()

    @Volatile
    var scratchDelay: DelayFunction = {}

    override fun createInjector(): Injector {
        runBlocking(Dispatchers.IO) { mockWebServer.start() }
        return SharedInjector(object : Bindings {
            override fun globalBindings(): GlobalBindings = GlobalBindings(
                delayFunction = { duration -> scratchDelay(duration) },
            )

            override fun scratchCardBindings(): ScratchCardBindings = ScratchCardBindings(
                versionServiceFactory = {
                    createVersionService(mockWebServer.url("/").toString())
                },
                scratchCardCodeGenerator = { "instrumented-test-code" },
            )
        })
    }
}
