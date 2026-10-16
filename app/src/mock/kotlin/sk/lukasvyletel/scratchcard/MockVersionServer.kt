package sk.lukasvyletel.scratchcard

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
private const val HTTP_INTERNAL_SERVER_ERROR = 500
private const val SERVER_ERROR_RESPONSE_CHANCE_PERCENT = 20
private const val RESPONSE_DELAY_MILLISECONDS = 500L

class MockVersionServer {
    private val server = MockWebServer().apply {
        dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                Thread.sleep(RESPONSE_DELAY_MILLISECONDS)
                if (Random.nextInt(100) < SERVER_ERROR_RESPONSE_CHANCE_PERCENT) {
                    return MockResponse()
                        .setResponseCode(HTTP_INTERNAL_SERVER_ERROR)
                        .setHeader("Content-Type", "application/json")
                        .setBody("""{"error":"Internal Server Error"}""")
                }

                val version = Random.nextInt(MINIMUM_ANDROID_VERSION, MAXIMUM_ANDROID_VERSION + 1)
                return MockResponse()
                    .setResponseCode(HTTP_OK)
                    .setHeader("Content-Type", "application/json")
                    .setBody("""{"android":"$version"}""")
            }
        }
    }

    fun start(): String = runBlocking(Dispatchers.IO) {
        server.start()
        server.url("/").toString()
    }

}
