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

    @Test
    fun `GIVEN scratch screen THEN localized title and description are shown`() {
        compose.setContent {
            CompositionLocalProvider(LocalInjector provides MockInjector()) {
                ScratchCardScreen()
            }
        }

        val context = ApplicationProvider.getApplicationContext<Context>()
        compose.onNodeWithTag("destinationTitle")
            .assertTextEquals(context.getString(R.string.scratch_screen_title))
        compose.onNodeWithTag("destinationDescription")
            .assertTextEquals(context.getString(R.string.scratch_screen_description))
    }

    @Test
    fun `GIVEN scratch screen WHEN back is clicked THEN scratch route is removed`() {
        val injector = MockInjector()
        injector.globalModule.navigation.navigateTo(ScratchRoute)
        compose.setContent {
            CompositionLocalProvider(LocalInjector provides injector) {
                ScratchCardScreen()
            }
        }

        compose.onNodeWithTag("destinationBackButton").assertHasClickAction().performClick()

        compose.runOnIdle {
            assertEquals(listOf(HomeRoute), injector.globalModule.navigation.backStack.toList())
        }
    }

    @Test
    fun `GIVEN scratch screen WHEN reveal is tapped THEN reveal action is requested`() {
        var requested = false
        compose.setContent {
            CompositionLocalProvider(LocalInjector provides MockInjector()) {
                ScratchCardScreen(onRevealClick = { requested = true })
            }
        }

        compose.onNodeWithTag("scratchFoil").assertIsDisplayed()
        compose.onNodeWithTag("revealButton").performScrollTo().assertHasClickAction().performClick()
        compose.runOnIdle { assertEquals(true, requested) }
    }

    @Test
    fun `GIVEN a revealed card THEN its code is shown and reveal is unavailable`() {
        val code = "123e4567-e89b-12d3-a456-426614174000"
        compose.setContent {
            CompositionLocalProvider(LocalInjector provides MockInjector()) {
                ScratchCardScreen(code = code)
            }
        }

        compose.onNodeWithTag("scratchCode").assertTextEquals(code)
        compose.onNodeWithTag("revealButton").assertIsNotEnabled()
    }

    @Test
    fun `GIVEN reveal in progress THEN progress is shown and reveal is unavailable`() {
        compose.setContent {
            CompositionLocalProvider(LocalInjector provides MockInjector()) {
                ScratchCardScreen(isRevealing = true)
            }
        }

        val context = ApplicationProvider.getApplicationContext<Context>()
        compose.onNodeWithTag("scratchCode").assertTextEquals(context.getString(R.string.scratch_revealing_title))
        compose.onNodeWithTag("revealButton").assertIsNotEnabled()
    }
}
