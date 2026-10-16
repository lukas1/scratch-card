package sk.lukasvyletel.scratchcard.debug

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

private const val MINIMUM_ANDROID_VERSION = 277000
private const val MAXIMUM_ANDROID_VERSION = 277056
private const val HTTP_OK = 200

class MockVersionServer {
    private val server = MockWebServer().apply {
        dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val version = Random.nextInt(MINIMUM_ANDROID_VERSION, MAXIMUM_ANDROID_VERSION + 1)
                return MockResponse()
                    .setResponseCode(HTTP_OK)
                    .setHeader("Content-Type", "application/json")
                    .setBody("""{"android":"$version"}""")
            }
        }
    }

    fun start(): String {
        return runBlocking(Dispatchers.IO) {
            server.start()
            server.url("/").toString()
        }
    }

    fun stop() {
        runBlocking(Dispatchers.IO) {
            server.shutdown()
        }
    }
}
