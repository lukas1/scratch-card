package sk.lukasvyletel.scratchcard

import android.view.KeyEvent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sk.lukasvyletel.scratchcard.features.card.model.ScratchCardState
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private const val ACTIVATION_VERSION_THRESHOLD = 277028
private const val TEST_CARD_CODE = "instrumented-test-code"

@RunWith(AndroidJUnit4::class)
class AppNavigationTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    private val context
        get() = compose.activity

    private val mockWebServer
        get() = (context.application as ScratchCardTestApplication).mockWebServer

    @Test
    fun givenScratchInProgress_whenSystemBackIsPressed_thenScratchIsCancelled(): Unit = synchronized(TEST_EXECUTION_LOCK) {
        resetCardToUnscratched()
        val application = context.application as ScratchCardTestApplication
        val delayStarted = CompletableDeferred<Duration>()
        val delayCancelled = CompletableDeferred<Unit>()
        application.scratchDelay = { duration ->
            delayStarted.complete(duration)
            try {
                awaitCancellation()
            } finally {
                delayCancelled.complete(Unit)
            }
        }

        try {
            compose.onNodeWithTag("scratchButton").performClick()
            compose.onNodeWithText(context.getString(R.string.scratch_screen_title)).assertIsDisplayed()
            compose.onNodeWithTag("revealButton").performClick()
            compose.waitUntil(5_000) { delayStarted.isCompleted }
            assertEquals(2.seconds, runBlocking { delayStarted.await() })
            compose.onNodeWithText(context.getString(R.string.scratch_revealing_action)).assertIsDisplayed()

            InstrumentationRegistry.getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)

            compose.waitUntil(5_000) { delayCancelled.isCompleted }
            compose.onNodeWithText(context.getString(R.string.card_state_unscratched)).assertIsDisplayed()
            assertEquals(
                ScratchCardState.Unscratched,
                application.injector().scratchCardModule.scratchCardRepository.cardStateFlow.value,
            )
        } finally {
            application.scratchDelay = {}
        }
    }

    @Test
    fun givenUnscratchedCard_whenVersionIsAboveThreshold_thenCardBecomesActivated(): Unit = synchronized(TEST_EXECUTION_LOCK) {
        resetCardToUnscratched()
        mockWebServer.enqueue(versionResponse(ACTIVATION_VERSION_THRESHOLD + 1))

        revealCardAndOpenActivation()
        compose.onNodeWithTag("activateCardButton").performClick()

        compose.onNodeWithText(context.getString(R.string.activation_activated_action)).assertIsDisplayed()
        assertActivationRequest(mockWebServer.takeRequest(5, TimeUnit.SECONDS))

        compose.onNodeWithText(context.getString(R.string.navigate_back)).performClick()
        compose.onNodeWithText(context.getString(R.string.card_state_activated)).assertIsDisplayed()
    }

    @Test
    fun givenUnscratchedCard_whenVersionIsAtThreshold_thenActivationErrorIsShown(): Unit = synchronized(TEST_EXECUTION_LOCK) {
        resetCardToUnscratched()
        mockWebServer.enqueue(versionResponse(ACTIVATION_VERSION_THRESHOLD))

        revealCardAndOpenActivation()
        compose.onNodeWithTag("activateCardButton").performClick()

        compose.onNodeWithText(context.getString(R.string.activation_failed_error_body)).assertIsDisplayed()
        assertActivationRequest(mockWebServer.takeRequest(5, TimeUnit.SECONDS))
        dismissErrorAndReturnHome()
    }

    @Test
    fun givenUnscratchedCard_whenServerReturnsHttpFailure_thenUnexpectedErrorIsShown(): Unit = synchronized(TEST_EXECUTION_LOCK) {
        resetCardToUnscratched()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setHeader("Content-Type", "application/json")
                .setBody("""{"error":"Internal Server Error"}"""),
        )

        revealCardAndOpenActivation()
        compose.onNodeWithTag("activateCardButton").performClick()

        compose.onNodeWithText(context.getString(R.string.activation_unexpected_error_body)).assertIsDisplayed()
        assertActivationRequest(mockWebServer.takeRequest(5, TimeUnit.SECONDS))
        dismissErrorAndReturnHome()
    }

    private fun revealCardAndOpenActivation() {
        val navigation = (context.application as ScratchCardApplication).injector().globalModule.navigation
        compose.onNodeWithTag("scratchButton").assertIsDisplayed().assertIsEnabled().performClick()
        compose.waitUntil(5_000) { navigation.backStack.lastOrNull() == ScratchRoute }
        compose.waitForIdle()
        compose.onNodeWithText(context.getString(R.string.scratch_screen_title)).assertIsDisplayed()

        compose.onNodeWithTag("revealButton").performClick()
        compose.onNodeWithText(context.getString(R.string.scratch_revealed_action)).assertIsDisplayed()
        compose.onNodeWithText(context.getString(R.string.navigate_back)).performClick()

        compose.onNodeWithTag("activationButton").performClick()
        compose.onNodeWithText(context.getString(R.string.activation_screen_title)).assertIsDisplayed()
    }

    private fun resetCardToUnscratched() {
        val injector = (context.application as ScratchCardApplication).injector()
        val navigation = injector.globalModule.navigation
        navigation.backStack.clear()
        navigation.backStack.add(HomeRoute)
        injector.scratchCardModule.scratchCardRepository.resetCard()
        compose.waitForIdle()
        compose.onNodeWithText(context.getString(R.string.card_state_unscratched)).assertIsDisplayed()
    }

    private fun assertActivationRequest(request: RecordedRequest?) {
        assertNotNull("Expected an activation request", request)
        assertEquals("GET", request?.method)
        assertEquals("/version", request?.requestUrl?.encodedPath)
        assertEquals(TEST_CARD_CODE, request?.requestUrl?.queryParameter("code"))
    }

    private fun dismissErrorAndReturnHome() {
        compose.onNodeWithText(context.getString(R.string.activation_error_dismiss)).performClick()
        compose.onNodeWithText(context.getString(R.string.navigate_back)).performClick()
        compose.onNodeWithTag("scratchButton").assertIsDisplayed()
    }

    private fun versionResponse(androidVersion: Int): MockResponse = MockResponse()
        .setResponseCode(200)
        .setHeader("Content-Type", "application/json")
        .setBody("""{"android":"$androidVersion"}""")

    private companion object {
        val TEST_EXECUTION_LOCK = Any()
    }
}
