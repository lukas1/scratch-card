package sk.lukasvyletel.scratchcard.core.dependency_injection

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import sk.lukasvyletel.scratchcard.features.card.api.ActivationVersion
import sk.lukasvyletel.scratchcard.core.coroutines.coroutineDelay

class AppBindingsTest {
    @Test
    fun `GIVEN production bindings WHEN dependencies are requested THEN defaults and version service work`() =
        runTest {
            val server = MockWebServer()
            server.start()
            try {
                val bindings = AppBindings(server.url("/").toString())
                val global = bindings.globalBindings()
                assertSame(Dispatchers.Default, global.cpuBoundCoroutineDispatcher)
                assertSame(coroutineDelay, global.delayFunction)

                val card = bindings.scratchCardBindings()
                assertTrue(card.activationCoroutineScope.coroutineContext[Job]?.isActive == true)
                val code = card.scratchCardCodeGenerator()
                assertTrue(code.isNotBlank())
                server.enqueue(MockResponse().setBody("""{"android":"287028"}"""))

                assertEquals(ActivationVersion("287028"), card.versionService().getVersion(code))
            } finally {
                server.shutdown()
            }
        }
}
