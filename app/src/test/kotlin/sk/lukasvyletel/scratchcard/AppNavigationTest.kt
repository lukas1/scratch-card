package sk.lukasvyletel.scratchcard

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.test.junit4.v2.createComposeRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sk.lukasvyletel.scratchcard.core.dependency_injection.LocalInjector
import sk.lukasvyletel.scratchcard.test.MockInjector

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class AppNavigationTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `GIVEN initial navigation WHEN displayed THEN Home icon bounds reach the splash target`() {
        var iconBounds: Rect? = null
        compose.setContent {
            CompositionLocalProvider(LocalInjector provides MockInjector()) {
                AppNavigation(onHomeIconPositioned = { iconBounds = it })
            }
        }

        compose.runOnIdle {
            val bounds = requireNotNull(iconBounds)
            assertTrue(bounds.width > 0f)
            assertTrue(bounds.height > 0f)
        }
    }
}
