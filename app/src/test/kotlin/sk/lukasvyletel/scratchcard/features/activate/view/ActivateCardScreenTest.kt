package sk.lukasvyletel.scratchcard.features.activate.view

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
import androidx.compose.ui.semantics.SemanticsActions
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
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
import sk.lukasvyletel.scratchcard.features.card.ScratchCardState
import sk.lukasvyletel.scratchcard.test.MainDispatcherRule
import sk.lukasvyletel.scratchcard.test.MockInjector

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.VANILLA_ICE_CREAM])
@OptIn(ExperimentalCoroutinesApi::class)
class ActivateCardScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

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

    @Test
    fun `GIVEN a scratched card WHEN activation screen opens THEN code and action are shown`() = runTest {
        val injector = MockInjector(
            cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
        )
        val repository = injector.scratchCardModule.scratchCardRepository
        repository.scratchCard()
        advanceUntilIdle()

        compose.setContent {
            CompositionLocalProvider(LocalInjector provides injector) {
                ActivateCardScreen()
            }
        }

        compose.onNodeWithTag("activationCode").assertTextEquals(MockInjector.TEST_SCRATCH_CODE)
        compose.onNodeWithTag("activateCardButton").assertHasClickAction()

        val activateAction = compose.onNodeWithTag("activateCardButton")
            .fetchSemanticsNode().config[SemanticsActions.OnClick]
        compose.runOnIdle { activateAction.action?.invoke() }
        compose.runOnIdle {
            assertEquals(
                ScratchCardState.Activated(MockInjector.TEST_SCRATCH_CODE),
                repository.cardStateFlow.value,
            )
        }
    }

    @Test
    fun `GIVEN activation error WHEN dismissed THEN dismissal is requested`() {
        var dismissed = false
        compose.setContent {
            CompositionLocalProvider(LocalInjector provides MockInjector()) {
                ActivateCardScreenContent(
                    code = null,
                    showError = true,
                    onDismissError = { dismissed = true },
                )
            }
        }

        val context = ApplicationProvider.getApplicationContext<Context>()
        compose.onNodeWithTag("activationErrorDialog").assertIsDisplayed()
        compose.onNodeWithTag("activationErrorDismissButton")
            .assertTextEquals(context.getString(R.string.activation_error_dismiss))
            .performClick()
        compose.runOnIdle { assertEquals(true, dismissed) }
    }

    @Test
    fun `GIVEN activation in progress THEN activation action is unavailable`() {
        compose.setContent {
            CompositionLocalProvider(LocalInjector provides MockInjector()) {
                ActivateCardScreenContent(code = null, isActivating = true)
            }
        }

        compose.onNodeWithTag("activateCardButton").assertIsNotEnabled()
    }
}
