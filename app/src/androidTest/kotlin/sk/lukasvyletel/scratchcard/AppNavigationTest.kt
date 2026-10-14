package sk.lukasvyletel.scratchcard

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppNavigationTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun givenHomeScreen_whenNavigatingToScratchAndBackThenActivationAndBack_thenReturnsHomeEachTime() {
        val context = compose.activity

        compose.onNodeWithTag("scratchButton").assertIsDisplayed().performClick()
        compose.onNodeWithText(context.getString(R.string.scratch_screen_title)).assertIsDisplayed()

        compose.onNodeWithText(context.getString(R.string.navigate_back)).performClick()
        compose.onNodeWithTag("activationButton").assertIsDisplayed()

        compose.onNodeWithTag("activationButton").performClick()
        compose.onNodeWithText(context.getString(R.string.activation_screen_title)).assertIsDisplayed()

        compose.onNodeWithText(context.getString(R.string.navigate_back)).performClick()
        compose.onNodeWithTag("scratchButton").assertIsDisplayed()
    }
}
