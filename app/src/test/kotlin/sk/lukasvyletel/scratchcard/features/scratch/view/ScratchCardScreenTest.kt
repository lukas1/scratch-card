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
import sk.lukasvyletel.scratchcard.test.MockInjector

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.VANILLA_ICE_CREAM])
class ScratchCardScreenTest {
    @get:Rule
    val compose = createComposeRule()

    private val injector = MockInjector()
    private val scratchCardRepository = injector.scratchCardModule.scratchCardRepository

    private fun setupView(isRevealing: Boolean = false, onRevealClick: () -> Unit = {}) {
        compose.setContent {
            CompositionLocalProvider(LocalInjector provides injector) {
                ScratchCardScreen(isRevealing = isRevealing, onRevealClick = onRevealClick)
            }
        }
    }

    @Test
    fun `GIVEN scratch screen THEN localized title and description are shown`() {
        setupView()

        val context = ApplicationProvider.getApplicationContext<Context>()
        compose.onNodeWithTag("destinationTitle")
            .assertTextEquals(context.getString(R.string.scratch_screen_title))
        compose.onNodeWithTag("destinationDescription")
            .assertTextEquals(context.getString(R.string.scratch_screen_description))
    }

    @Test
    fun `GIVEN scratch screen WHEN back is clicked THEN scratch route is removed`() {
        injector.globalModule.navigation.navigateTo(ScratchRoute)
        setupView()

        compose.onNodeWithTag("destinationBackButton").assertHasClickAction().performClick()

        compose.runOnIdle {
            assertEquals(listOf(HomeRoute), injector.globalModule.navigation.backStack.toList())
        }
    }

    @Test
    fun `GIVEN scratch screen WHEN reveal is tapped THEN reveal action is requested`() {
        var requested = false
        setupView(onRevealClick = { requested = true })

        compose.onNodeWithTag("scratchFoil").assertIsDisplayed()
        compose.onNodeWithTag("revealButton").performScrollTo().assertHasClickAction().performClick()
        compose.runOnIdle { assertEquals(true, requested) }
        compose.onNodeWithTag("scratchCode").assertTextEquals(MockInjector.TEST_SCRATCH_CODE)
        compose.onNodeWithTag("revealButton").assertIsNotEnabled()
    }

    @Test
    fun `GIVEN a revealed card THEN its code is shown and reveal is unavailable`() {
        scratchCardRepository.scratchCard()
        setupView()

        compose.onNodeWithTag("scratchCode").assertTextEquals(MockInjector.TEST_SCRATCH_CODE)
        compose.onNodeWithTag("revealButton").assertIsNotEnabled()
    }

    @Test
    fun `GIVEN reveal in progress THEN progress is shown and reveal is unavailable`() {
        setupView(isRevealing = true)

        val context = ApplicationProvider.getApplicationContext<Context>()
        compose.onNodeWithTag("scratchCode").assertTextEquals(context.getString(R.string.scratch_revealing_title))
        compose.onNodeWithTag("revealButton").assertIsNotEnabled()
    }
}
