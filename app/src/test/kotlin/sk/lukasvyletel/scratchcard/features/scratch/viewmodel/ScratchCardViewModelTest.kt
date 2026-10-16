package sk.lukasvyletel.scratchcard.features.scratch.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sk.lukasvyletel.scratchcard.features.card.model.ScratchCardState
import sk.lukasvyletel.scratchcard.test.MainDispatcherRule
import sk.lukasvyletel.scratchcard.test.MockInjector
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ScratchCardViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mainDispatcher = mainDispatcherRule.dispatcher

    @Test
    fun `GIVEN scratch has completed WHEN scratch button is pressed again THEN card is scratched only once`() =
        runTest {
            var generatedCodeCount = 0
            val injector = MockInjector(
                scratchCardCodeGenerator = {
                    generatedCodeCount += 1
                    "code-$generatedCodeCount"
                },
                cpuBoundCoroutineDispatcher = mainDispatcher,
            )
            val viewModel = createViewModel(injector)
            val repository = injector.scratchCardModule.scratchCardRepository
            val scratchedCardStates = mutableListOf<ScratchCardState.Scratched>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                repository.cardStateFlow.collect { cardState ->
                    if (cardState is ScratchCardState.Scratched) scratchedCardStates.add(cardState)
                }
            }

            viewModel.scratchCard()
            runCurrent()
            advanceTimeBy(2_000.milliseconds)
            runCurrent()

            viewModel.scratchCard()
            runCurrent()
            advanceTimeBy(2_000.milliseconds)
            runCurrent()

            assertEquals(1, scratchedCardStates.size)
            assertEquals(1, generatedCodeCount)
        }

    @Test
    fun `GIVEN scratch requested WHEN one millisecond remains THEN loading stays active`() =
        runTest {
            val injector = MockInjector(cpuBoundCoroutineDispatcher = mainDispatcher)
            val viewModel = createViewModel(injector)

            viewModel.scratchCard()
            runCurrent()
            assertEquals(true, viewModel.isLoading.value)

            advanceTimeBy(1_999.milliseconds)
            runCurrent()

            assertEquals(true, viewModel.isLoading.value)
            assertEquals(null, viewModel.code.value)
        }

    @Test
    fun `GIVEN scratch requested WHEN two seconds pass THEN card is scratched and loading ends`() =
        runTest {
            val injector = MockInjector(cpuBoundCoroutineDispatcher = mainDispatcher)
            val viewModel = createViewModel(injector)

            viewModel.scratchCard()
            runCurrent()
            advanceTimeBy(2_000.milliseconds)
            runCurrent()

            assertEquals(false, viewModel.isLoading.value)
            val cardState = injector.scratchCardModule.scratchCardRepository.cardStateFlow.value
            assertEquals(
                ScratchCardState.Scratched(MockInjector.TEST_SCRATCH_CODE),
                cardState,
            )
        }

    @Test
    fun `GIVEN scratch screen is left during scratching WHEN two seconds pass THEN card remains unscratched`() =
        runTest {
            var generatedCodeCount = 0
            val injector = MockInjector(
                scratchCardCodeGenerator = {
                    generatedCodeCount += 1
                    MockInjector.TEST_SCRATCH_CODE
                },
                cpuBoundCoroutineDispatcher = mainDispatcher,
            )
            val viewModelStore = ViewModelStore()
            val viewModel = ViewModelProvider(
                viewModelStore,
                ScratchCardViewModelFactory(injector.scratchCardModule),
            )[ScratchCardViewModel::class.java]
            viewModel.scratchCard()
            runCurrent()
            assertEquals(true, viewModel.isLoading.value)

            viewModelStore.clear()
            advanceTimeBy(2_000.milliseconds)
            runCurrent()

            val cardState = injector.scratchCardModule.scratchCardRepository.cardStateFlow.value
            assertEquals(ScratchCardState.Unscratched, cardState)
            assertEquals(0, generatedCodeCount)
        }

    private fun createViewModel(injector: MockInjector): ScratchCardViewModel = ViewModelProvider(
        ViewModelStore(),
        ScratchCardViewModelFactory(injector.scratchCardModule),
    )[ScratchCardViewModel::class.java]
}
