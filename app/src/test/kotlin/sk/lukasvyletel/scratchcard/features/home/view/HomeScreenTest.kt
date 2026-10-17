package sk.lukasvyletel.scratchcard.features.home.view

import android.content.Context
import android.os.Build
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import sk.lukasvyletel.scratchcard.features.card.api.VersionService
import sk.lukasvyletel.scratchcard.features.card.api.ActivationVersion
import sk.lukasvyletel.scratchcard.features.card.api.ScratchCardApi
import sk.lukasvyletel.scratchcard.ActivationRoute
import sk.lukasvyletel.scratchcard.HomeRoute
import sk.lukasvyletel.scratchcard.R
import sk.lukasvyletel.scratchcard.core.dependency_injection.LocalInjector
import sk.lukasvyletel.scratchcard.features.card.model.ScratchCardState
import sk.lukasvyletel.scratchcard.test.MockInjector

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.VANILLA_ICE_CREAM])
@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenTest {
    @get:Rule
    val compose = createComposeRule()

    private val injector = MockInjector()

    private fun setupView(activeInjector: MockInjector = injector) {
        compose.setContent {
            CompositionLocalProvider(LocalInjector provides activeInjector) {
                HomeScreen(onHeaderIconPositioned = {})
            }
        }
    }

    @Test
    fun `GIVEN an unscratched card THEN the status and hidden code are shown`() {
        setupView()

        val context = ApplicationProvider.getApplicationContext<Context>()
        compose.onNodeWithTag("cardStatus").assertTextEquals(context.getString(R.string.card_state_unscratched))
        compose.onNodeWithTag("cardCode").assertTextEquals(context.getString(R.string.card_hidden_code))
    }

    @Test
    fun `GIVEN an unscratched card THEN activation is disabled`() {
        setupView()

        compose.onNodeWithTag("activationButton").assertIsNotEnabled()
    }

    @Test
    fun `GIVEN a scratched card THEN scratching is disabled`() = runTest {
        val testInjector = MockInjector(
            cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
            activationCoroutineScope = backgroundScope,
        )
        testInjector.scratchCardModule.scratchCardRepository.scratchCard()
        advanceUntilIdle()
        setupView(testInjector)

        compose.onNodeWithTag("scratchButton").assertIsNotEnabled()
    }

    @Test
    fun `GIVEN a scratched card WHEN reset is clicked THEN the card returns to its unscratched state`() = runTest {
        val testInjector = MockInjector(
            cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
            activationCoroutineScope = backgroundScope,
        )
        val repository = testInjector.scratchCardModule.scratchCardRepository
        repository.scratchCard()
        advanceUntilIdle()
        setupView(testInjector)

        compose.onNodeWithTag("resetCardButton")
            .performScrollTo()
            .performClick()

        compose.runOnIdle {
            assertEquals(ScratchCardState.Unscratched, repository.cardStateFlow.value)
        }
        compose.onNodeWithTag("scratchButton").assertIsEnabled()
        val context = ApplicationProvider.getApplicationContext<Context>()
        compose.onNodeWithTag("cardCode").assertTextEquals(context.getString(R.string.card_hidden_code))
    }

    @Test
    fun `GIVEN activation is pending WHEN reset is clicked THEN a late response is ignored`() =
        runTest {
        val activationStarted = CompletableDeferred<Unit>()
        val activationResponse = CompletableDeferred<ActivationVersion>()
        val testInjector = MockInjector(
            cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
            activationCoroutineScope = backgroundScope,
            versionService = VersionService {
                activationStarted.complete(Unit)
                activationResponse.await()
            },
        )
        val repository = testInjector.scratchCardModule.scratchCardRepository
        val code = MockInjector.TEST_SCRATCH_CODE
        repository.scratchCard()
        advanceUntilIdle()
        setupView(testInjector)

        repository.activateCard(ScratchCardState.Scratched(code))
        runCurrent()
        activationStarted.await()

        compose.onNodeWithTag("resetCardButton")
            .performScrollTo()
            .performClick()
        compose.runOnIdle {
            assertEquals(ScratchCardState.Unscratched, repository.cardStateFlow.value)
        }

        activationResponse.complete(
            ActivationVersion((ScratchCardApi.ACTIVATION_VERSION_THRESHOLD + 1).toString()),
        )
        runCurrent()

        compose.runOnIdle {
            assertEquals(ScratchCardState.Unscratched, repository.cardStateFlow.value)
        }
        val context = ApplicationProvider.getApplicationContext<Context>()
        compose.onNodeWithTag("cardStatus")
            .assertTextEquals(context.getString(R.string.card_state_unscratched))
        compose.onNodeWithTag("cardCode").assertTextEquals(context.getString(R.string.card_hidden_code))
    }

    @Test
    fun `GIVEN a revealed card WHEN activated THEN the status changes and code stays visible`() =
        runTest {
            val code = MockInjector.TEST_SCRATCH_CODE
            val testInjector = MockInjector(
                cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
                activationCoroutineScope = backgroundScope,
            )
            testInjector.scratchCardModule.scratchCardRepository.scratchCard()
            advanceUntilIdle()
            setupView(testInjector)

            val context = ApplicationProvider.getApplicationContext<Context>()
            compose.onNodeWithTag("cardStatus").assertTextEquals(context.getString(R.string.card_state_scratched))
            compose.onNodeWithTag("cardCode").assertTextEquals(code)

            compose.runOnIdle {
                testInjector.scratchCardModule.scratchCardRepository.activateCard(
                    ScratchCardState.Scratched(code = code)
                )
            }
            runCurrent()
            compose.onNodeWithTag("cardStatus").assertTextEquals(context.getString(R.string.card_state_activated))
            compose.onNodeWithTag("cardCode").assertTextEquals(code)
        }

    @Test
    fun `GIVEN home is visible WHEN activation fails THEN an error snackbar is shown`() = runTest {
        val testInjector = MockInjector(
            cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
            activationCoroutineScope = backgroundScope,
            versionService = VersionService { throw IllegalStateException("Activation failed") },
        )
        val repository = testInjector.scratchCardModule.scratchCardRepository
        repository.scratchCard()
        advanceUntilIdle()
        setupView(testInjector)

        repository.activateCard(ScratchCardState.Scratched(MockInjector.TEST_SCRATCH_CODE))
        runCurrent()
        compose.waitForIdle()

        val context = ApplicationProvider.getApplicationContext<Context>()
        compose.onNodeWithText(context.getString(R.string.activation_unexpected_error_body)).assertIsDisplayed()
    }

    @Test
    fun `GIVEN home is visible WHEN card version is too low THEN activation failure snackbar is shown`() = runTest {
        val testInjector = MockInjector(
            cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
            activationCoroutineScope = backgroundScope,
            versionService = VersionService {
                ActivationVersion(ScratchCardApi.ACTIVATION_VERSION_THRESHOLD.toString())
            },
        )
        val repository = testInjector.scratchCardModule.scratchCardRepository
        repository.scratchCard()
        advanceUntilIdle()
        setupView(testInjector)

        repository.activateCard(ScratchCardState.Scratched(MockInjector.TEST_SCRATCH_CODE))
        runCurrent()
        compose.waitForIdle()

        val context = ApplicationProvider.getApplicationContext<Context>()
        compose.onNodeWithText(context.getString(R.string.activation_failed_error_body)).assertIsDisplayed()
    }

    @Test
    fun `GIVEN a revealed card WHEN activation is clicked THEN only the activation route is added`() =
        runTest {
            val testInjector = MockInjector(
                cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
                activationCoroutineScope = backgroundScope,
            )
            testInjector.scratchCardModule.scratchCardRepository.scratchCard()
            advanceUntilIdle()
            setupView(testInjector)

            val activationAction = compose.onNodeWithTag("activationButton")
                .assertHasClickAction()
                .fetchSemanticsNode().config[SemanticsActions.OnClick]
            compose.runOnIdle {
                activationAction.action?.invoke()
            }

            compose.runOnIdle {
                assertEquals(
                    listOf(HomeRoute, ActivationRoute),
                    testInjector.globalModule.navigation.backStack.toList()
                )
            }
        }

    @Test
    fun `GIVEN the home screen WHEN the header is laid out THEN icon bounds are reported`() {
        var iconBounds: Rect? = null
        compose.setContent {
            CompositionLocalProvider(LocalInjector provides injector) {
                HomeScreen(onHeaderIconPositioned = { iconBounds = it })
            }
        }

        compose.runOnIdle {
            val bounds = requireNotNull(iconBounds)
            assertTrue(bounds.width > 0f)
            assertTrue(bounds.height > 0f)
        }
    }

    @Test
    @GraphicsMode(GraphicsMode.Mode.LEGACY)
    fun `GIVEN scratch card artwork WHEN captured THEN the card is rendered`() {
        compose.setContent {
            ScratchCardArtwork(ScratchCardState.Unscratched)
        }

        val image = compose.onNodeWithTag("scratchCard").captureToImage()

        assertTrue(image.width > 0)
        assertTrue(image.height > 0)
    }
}
