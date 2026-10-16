package sk.lukasvyletel.scratchcard.features.scratch.view

import android.content.Context
import android.os.Build
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sk.lukasvyletel.scratchcard.HomeRoute
import sk.lukasvyletel.scratchcard.R
import sk.lukasvyletel.scratchcard.ScratchRoute
import sk.lukasvyletel.scratchcard.core.dependency_injection.LocalInjector
import sk.lukasvyletel.scratchcard.features.card.model.ScratchCardState
import sk.lukasvyletel.scratchcard.test.MockInjector
import kotlin.time.Duration.Companion.milliseconds

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.VANILLA_ICE_CREAM])
@OptIn(ExperimentalCoroutinesApi::class)
class ScratchCardScreenTest {
    @get:Rule
    val compose = createComposeRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val injector = MockInjector()

    private fun setupView(
        activeInjector: MockInjector = injector,
    ) {
        compose.setContent {
            CompositionLocalProvider(LocalInjector provides activeInjector) {
                ScratchCardScreen()
            }
        }
    }

    @Test
    fun `GIVEN scratch screen THEN localized title and description are shown`() {
        setupView()

        compose.onNodeWithTag("destinationTitle")
            .assertTextEquals(context.getString(R.string.scratch_screen_title))
        compose.onNodeWithTag("destinationDescription")
            .assertTextEquals(context.getString(R.string.scratch_screen_description))
    }

    @Test
    fun `WHEN back is clicked THEN scratch route is removed`() {
        injector.globalModule.navigation.navigateTo(ScratchRoute)
        setupView()

        compose.onNodeWithTag("destinationBackButton").assertHasClickAction().performClick()

        compose.runOnIdle {
            assertEquals(listOf(HomeRoute), injector.globalModule.navigation.backStack.toList())
        }
    }

    @Test
    fun `WHEN reveal is tapped THEN card begins revealing`() {
        val scheduler = TestCoroutineScheduler()
        val testDispatcher = StandardTestDispatcher(scheduler)
        val testInjector = MockInjector(cpuBoundCoroutineDispatcher = testDispatcher)
        setupView(activeInjector = testInjector)

        compose.onNodeWithTag("scratchFoil").assertIsDisplayed()
        compose.onNodeWithTag("revealButton").performScrollTo().assertHasClickAction().performClick()

        compose.onNodeWithTag("scratchCode")
            .assertTextEquals(context.getString(R.string.scratch_revealing_title))
        compose.onNodeWithTag("revealButton").assertIsDisplayed().assertIsNotEnabled()

        scheduler.advanceTimeBy(1_999.milliseconds)
        scheduler.runCurrent()
        compose.onNodeWithTag("scratchCode")
            .assertTextEquals(context.getString(R.string.scratch_revealing_title))
        compose.onNodeWithTag("revealButton").assertIsDisplayed().assertIsNotEnabled()
        scheduler.advanceTimeBy(1.milliseconds)
        scheduler.runCurrent()
        compose.waitUntil(timeoutMillis = 1_000) {
            testInjector.scratchCardModule.scratchCardRepository.cardStateFlow.value is
                ScratchCardState.Scratched
        }
        compose.onNodeWithTag("scratchCode").assertTextEquals(MockInjector.TEST_SCRATCH_CODE)
        compose.onNodeWithTag("revealButton").assertIsNotEnabled()
    }

    @Test
    fun `GIVEN a revealed card THEN its code is shown and reveal is unavailable`() = runTest {
        val testInjector = MockInjector(
            cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
        )
        testInjector.scratchCardModule.scratchCardRepository.scratchCard()
        advanceUntilIdle()
        setupView(activeInjector = testInjector)

        compose.onNodeWithTag("scratchCode").assertTextEquals(MockInjector.TEST_SCRATCH_CODE)
        compose.onNodeWithTag("revealButton").assertIsNotEnabled()
    }
}
