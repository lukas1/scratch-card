package sk.lukasvyletel.scratchcard.features.card

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.cancel
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class ScratchCardRepositoryTest {
    @Test
    fun `GIVEN two simultaneous scratch requests WHEN scratching completes THEN code is generated once`() =
        runTest {
            var generatedCodeCount = 0
            val repository = ScratchCardRepository(
                scratchCardCodeGenerator = {
                    generatedCodeCount += 1
                    "scratch-code"
                },
                cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
                activationCoroutineScope = CoroutineScope(
                    StandardTestDispatcher(testScheduler) + Job(),
                ),
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
            val repository = ScratchCardRepository(
                scratchCardCodeGenerator = { "scratch-code" },
                cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
                activationCoroutineScope = activationScope,
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
