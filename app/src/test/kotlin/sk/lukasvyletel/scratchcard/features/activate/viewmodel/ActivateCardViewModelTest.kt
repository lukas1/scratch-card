package sk.lukasvyletel.scratchcard.features.activate.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sk.lukasvyletel.scratchcard.features.card.ScratchCardState
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
    fun `GIVEN activation requested WHEN view model is cleared THEN activation completes`() = runTest {
        val injector = MockInjector(
            cpuBoundCoroutineDispatcher = mainDispatcher,
            activationCoroutineScope = backgroundScope,
        )
        val repository = injector.scratchCardModule.scratchCardRepository
        repository.scratchCard()
        advanceTimeBy(2_000.milliseconds)
        runCurrent()

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
}
