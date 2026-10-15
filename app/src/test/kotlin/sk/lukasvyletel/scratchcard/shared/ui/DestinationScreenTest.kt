package sk.lukasvyletel.scratchcard.shared.ui

import android.os.Build
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sk.lukasvyletel.scratchcard.R
import sk.lukasvyletel.scratchcard.core.dependency_injection.LocalInjector
import sk.lukasvyletel.scratchcard.test.MockInjector

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.VANILLA_ICE_CREAM])
class DestinationScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `GIVEN long destination content WHEN scrolled to the end THEN navigation header remains visible`() {
        compose.setContent {
            CompositionLocalProvider(LocalInjector provides MockInjector()) {
                DestinationScreen(
                    title = R.string.scratch_screen_title,
                    description = R.string.scratch_screen_description,
                    step = R.string.scratch_step,
                ) {
                    Spacer(Modifier.height(1400.dp))
                    Text(
                        text = stringResource(R.string.scratch_action_hint),
                        modifier = Modifier.testTag("bottomMarker"),
                    )
                }
            }
        }

        compose.onNodeWithTag("bottomMarker").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("destinationTitle").assertIsNotDisplayed()
        compose.onNodeWithTag("destinationBackButton").assertIsDisplayed()
        compose.onNodeWithTag("destinationStepBubble").assertIsDisplayed()
    }
}
