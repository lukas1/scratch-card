package sk.lukasvyletel.scratchcard.features.activate.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sk.lukasvyletel.scratchcard.features.card.repository.ScratchCardRepository
import sk.lukasvyletel.scratchcard.features.card.model.ScratchCardState
import sk.lukasvyletel.scratchcard.test.MainDispatcherRule
import sk.lukasvyletel.scratchcard.test.MockInjector
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ActivateCardViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mainDispatcher = mainDispatcherRule.dispatcher

    @Test
    fun `GIVEN activation is in progress WHEN activation is requested again THEN request is ignored`() = runTest {
        val injector = MockInjector(
            cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
            activationCoroutineScope = backgroundScope,
        )
        val repository = injector.scratchCardModule.scratchCardRepository
        completeScratchCard(repository)

        val viewModel = createViewModel(injector)

        viewModel.activateCard()
        assertEquals(ActivationButtonState.ACTIVATING, viewModel.activationButtonState.value)
        viewModel.activateCard()
        assertEquals(ActivationButtonState.ACTIVATING, viewModel.activationButtonState.value)
        runCurrent()

        assertEquals(ActivationButtonState.ACTIVATED, viewModel.activationButtonState.value)
        assertEquals(
            ScratchCardState.Activated(MockInjector.TEST_SCRATCH_CODE),
            repository.cardStateFlow.value,
        )
    }

    @Test
    fun `GIVEN card is already activated WHEN activation is requested THEN request is ignored`() = runTest {
        val injector = MockInjector(
            cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
            activationCoroutineScope = backgroundScope,
        )
        val repository = injector.scratchCardModule.scratchCardRepository
        completeScratchCard(repository)
        repository.activateCard(ScratchCardState.Scratched(MockInjector.TEST_SCRATCH_CODE))
        runCurrent()

        val viewModel = createViewModel(injector)
        viewModel.activateCard()

        assertEquals(ActivationButtonState.ACTIVATED, viewModel.activationButtonState.value)
        assertEquals(
            ScratchCardState.Activated(MockInjector.TEST_SCRATCH_CODE),
            repository.cardStateFlow.value,
        )
    }

    @Test
    fun `GIVEN scratched card WHEN activation runs THEN progress state follows the request`() = runTest {
        val injector = MockInjector(
            cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
            activationCoroutineScope = backgroundScope,
        )
        val repository = injector.scratchCardModule.scratchCardRepository
        completeScratchCard(repository)

        val viewModel = createViewModel(injector)
        viewModel.activateCard()

        assertEquals(ActivationButtonState.ACTIVATING, viewModel.activationButtonState.value)
        runCurrent()
        assertEquals(ActivationButtonState.ACTIVATED, viewModel.activationButtonState.value)
        assertEquals(
            ScratchCardState.Activated(MockInjector.TEST_SCRATCH_CODE),
            repository.cardStateFlow.value,
        )
    }

    @Test
    fun `GIVEN activation requested WHEN view model is cleared THEN activation completes`() = runTest {
        val injector = MockInjector(
            cpuBoundCoroutineDispatcher = mainDispatcher,
            activationCoroutineScope = backgroundScope,
        )
        val repository = injector.scratchCardModule.scratchCardRepository
        completeScratchCard(repository)

        val viewModelStore = ViewModelStore()
        val viewModel = ViewModelProvider(
            viewModelStore,
            ActivateCardViewModelFactory(injector.activateCardModule),
        )[ActivateCardViewModel::class.java]
        viewModel.activateCard()
        viewModelStore.clear()
        runCurrent()

        assertEquals(
            ScratchCardState.Activated(MockInjector.TEST_SCRATCH_CODE),
            repository.cardStateFlow.value,
        )
    }

    private fun createViewModel(injector: MockInjector): ActivateCardViewModel = ViewModelProvider(
        ViewModelStore(),
        ActivateCardViewModelFactory(injector.activateCardModule),
    )[ActivateCardViewModel::class.java]

    private suspend fun TestScope.completeScratchCard(repository: ScratchCardRepository) {
        repository.scratchCard()
        advanceTimeBy(2_000.milliseconds)
        runCurrent()
    }
}
