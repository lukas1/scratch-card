package sk.lukasvyletel.scratchcard

import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sk.lukasvyletel.scratchcard.test.MainDispatcherRule
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SplashToHeaderTransitionTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `GIVEN no header icon WHEN the exit listener runs THEN the splash is removed after a timeout`() = runTest {
        val splashScreen = mockk<SplashScreen>()
        val provider = mockk<SplashScreenViewProvider>()
        val listener = slot<SplashScreen.OnExitAnimationListener>()
        every { splashScreen.setOnExitAnimationListener(capture(listener)) } just Runs
        every { provider.remove() } just Runs

        val transition = SplashToHeaderTransition(splashScreen)
        transition.registerExitListener()
        listener.captured.onSplashScreenExit(provider)

        runCurrent()
        verify(exactly = 0) { provider.remove() }
        advanceTimeBy(500.milliseconds)
        runCurrent()
        verify(exactly = 1) { provider.remove() }
    }
}
