package sk.lukasvyletel.scratchcard.features.card

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.cancel
import sk.lukasvyletel.scratchcard.features.card.api.ActivationVersion
import org.junit.Assert.assertEquals
import org.junit.Test
import sk.lukasvyletel.scratchcard.features.card.dependency_injection.ScratchCardModule
import sk.lukasvyletel.scratchcard.features.card.model.ScratchCardState
import sk.lukasvyletel.scratchcard.features.card.repository.ScratchCardCodeGenerator
import sk.lukasvyletel.scratchcard.features.card.repository.ScratchCardRepository
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class ScratchCardRepositoryTest {

    private fun createRepository(
        scratchCardCodeGenerator: ScratchCardCodeGenerator,
        cpuBoundCoroutineDispatcher: CoroutineDispatcher,
        activationCoroutineScope: CoroutineScope
    ): ScratchCardRepository = ScratchCardModule(
        scratchCardCodeGenerator = scratchCardCodeGenerator,
        cpuBoundCoroutineDispatcher = cpuBoundCoroutineDispatcher,
        activationCoroutineScope = activationCoroutineScope,
        versionService = { ActivationVersion("287028") }
    ).scratchCardRepository

    @Test
    fun `GIVEN two simultaneous scratch requests WHEN scratching completes THEN code is generated once`() =
        runTest {
            var generatedCodeCount = 0
            val repository = createRepository(
                scratchCardCodeGenerator = {
                    generatedCodeCount += 1
                    "scratch-code"
                },
                cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
                activationCoroutineScope = CoroutineScope(
                    StandardTestDispatcher(testScheduler) + Job(),
                )
            )

            launch { repository.scratchCard() }
            launch { repository.scratchCard() }
            runCurrent()
            advanceTimeBy(2.seconds)
            runCurrent()

            assertEquals(1, generatedCodeCount)
            assertEquals(
                ScratchCardState.Scratched("scratch-code"),
                repository.cardStateFlow.value,
            )
        }

    @Test
    fun `GIVEN two simultaneous activation requests WHEN activation completes THEN card is activated once`() =
        runTest {
            val activationScope = CoroutineScope(StandardTestDispatcher(testScheduler) + Job())
            val repository = createRepository(
                scratchCardCodeGenerator = { "scratch-code" },
                cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
                activationCoroutineScope = activationScope
            )
            repository.scratchCard()
            runCurrent()
            advanceTimeBy(2.seconds)
            runCurrent()
            val scratchedCard = repository.cardStateFlow.value as ScratchCardState.Scratched

            repository.activateCard(scratchedCard)
            repository.activateCard(ScratchCardState.Scratched("other-code"))
            runCurrent()

            assertEquals(ScratchCardState.Activated("scratch-code"), repository.cardStateFlow.value)
            activationScope.cancel()
        }
}
