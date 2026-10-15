package sk.lukasvyletel.scratchcard.core.dependency_injection

import android.os.Build
import androidx.compose.ui.test.junit4.v2.createComposeRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.VANILLA_ICE_CREAM])
class LocalInjectorTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `GIVEN no injector provider WHEN LocalInjector is read THEN an error is thrown`() {
        val exception = assertThrows(IllegalStateException::class.java) {
            compose.setContent { LocalInjector.current }
        }

        assertEquals("Injector was not provided", exception.message)
    }
}
