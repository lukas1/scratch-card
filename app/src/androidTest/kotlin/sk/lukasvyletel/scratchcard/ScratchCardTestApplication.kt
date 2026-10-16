package sk.lukasvyletel.scratchcard

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import sk.lukasvyletel.scratchcard.core.coroutines.DelayFunction
import sk.lukasvyletel.scratchcard.core.dependency_injection.createVersionService
import sk.lukasvyletel.scratchcard.core.dependency_injection.Injector
import sk.lukasvyletel.scratchcard.core.dependency_injection.SharedInjector

class ScratchCardTestApplication : ScratchCardApplication() {
    val mockWebServer = MockWebServer()

    @Volatile
    var scratchDelay: DelayFunction = {}

    override fun createInjector(): Injector {
        runBlocking(Dispatchers.IO) { mockWebServer.start() }
        return SharedInjector(
            versionService = createVersionService(mockWebServer.url("/").toString()),
            scratchCardCodeGenerator = { "instrumented-test-code" },
            delayFunction = { duration -> scratchDelay(duration) },
        )
    }
}
