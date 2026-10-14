package sk.lukasvyletel.scratchcard.features.home.view

import android.content.Context
import android.os.Build
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sk.lukasvyletel.scratchcard.R
import sk.lukasvyletel.scratchcard.features.card.ScratchCardState

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.VANILLA_ICE_CREAM])
class HomeScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `GIVEN an unscratched card THEN the status and hidden code are shown`() {
        compose.setContent {
            HomeScreen(
                cardState = ScratchCardState.Unscratched,
                onScratchClick = {},
                onActivationClick = {},
            )
        }

        val context = ApplicationProvider.getApplicationContext<Context>()
        compose.onNodeWithTag("cardStatus").assertTextEquals(context.getString(R.string.card_state_unscratched))
        compose.onNodeWithTag("cardCode").assertTextEquals(context.getString(R.string.home_hidden_code))
    }

    @Test
    fun `GIVEN a revealed card WHEN activated THEN the status changes and code stays visible`() {
        val code = "c533829f-cd4a-433d-9886-05f9b613db57"
        val state = mutableStateOf<ScratchCardState>(ScratchCardState.Scratched(code))
        compose.setContent {
            HomeScreen(cardState = state.value, onScratchClick = {}, onActivationClick = {})
        }

        val context = ApplicationProvider.getApplicationContext<Context>()
        compose.onNodeWithTag("cardStatus").assertTextEquals(context.getString(R.string.card_state_scratched))
        compose.onNodeWithTag("cardCode").assertTextEquals(code)

        compose.runOnIdle { state.value = ScratchCardState.Activated(code) }
        compose.onNodeWithTag("cardStatus").assertTextEquals(context.getString(R.string.card_state_activated))
        compose.onNodeWithTag("cardCode").assertTextEquals(code)
    }

    @Test
    fun `GIVEN the main screen WHEN action semantics are invoked THEN navigation callbacks run`() {
        val selectedActions = mutableListOf<String>()
        compose.setContent {
            HomeScreen(
                cardState = ScratchCardState.Unscratched,
                onScratchClick = { selectedActions.add("scratch") },
                onActivationClick = { selectedActions.add("activation") },
            )
        }

        val scratchAction = compose.onNodeWithTag("scratchButton")
            .assertHasClickAction()
            .fetchSemanticsNode().config[SemanticsActions.OnClick]
        val activationAction = compose.onNodeWithTag("activationButton")
            .assertHasClickAction()
            .fetchSemanticsNode().config[SemanticsActions.OnClick]
        compose.runOnIdle {
            scratchAction.action?.invoke()
            activationAction.action?.invoke()
        }

        compose.runOnIdle {
            assertEquals(listOf("scratch", "activation"), selectedActions)
        }
    }
}
