package sk.lukasvyletel.scratchcard.features.activate.view

import android.content.Context
import android.os.Build
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sk.lukasvyletel.scratchcard.ActivationRoute
import sk.lukasvyletel.scratchcard.HomeRoute
import sk.lukasvyletel.scratchcard.R
import sk.lukasvyletel.scratchcard.core.dependency_injection.LocalInjector
import sk.lukasvyletel.scratchcard.test.MockInjector

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.VANILLA_ICE_CREAM])
class ActivateCardScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `GIVEN activation screen THEN localized title and description are shown`() {
        compose.setContent {
            CompositionLocalProvider(LocalInjector provides MockInjector()) {
                ActivateCardScreen()
            }
        }

        val context = ApplicationProvider.getApplicationContext<Context>()
        compose.onNodeWithTag("destinationTitle")
            .assertTextEquals(context.getString(R.string.activation_screen_title))
        compose.onNodeWithTag("destinationDescription")
            .assertTextEquals(context.getString(R.string.activation_screen_description))
    }

    @Test
    fun `GIVEN activation screen WHEN back is clicked THEN activation route is removed`() {
        val injector = MockInjector()
        injector.globalModule.navigation.navigateTo(ActivationRoute)
        compose.setContent {
            CompositionLocalProvider(LocalInjector provides injector) {
                ActivateCardScreen()
            }
        }

        compose.onNodeWithTag("destinationBackButton").assertHasClickAction().performClick()

        compose.runOnIdle {
            assertEquals(listOf(HomeRoute), injector.globalModule.navigation.backStack.toList())
        }
    }
}
