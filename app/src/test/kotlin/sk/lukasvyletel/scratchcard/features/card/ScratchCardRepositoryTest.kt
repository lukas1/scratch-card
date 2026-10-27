package sk.lukasvyletel.scratchcard.features.card

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import sk.lukasvyletel.scratchcard.features.card.api.ActivationVersion
import sk.lukasvyletel.scratchcard.features.card.api.ScratchCardCodeGenerator
import sk.lukasvyletel.scratchcard.features.card.api.VersionService
import sk.lukasvyletel.scratchcard.features.card.dependency_injection.ScratchCardModule
import sk.lukasvyletel.scratchcard.features.card.model.ActivationError
import sk.lukasvyletel.scratchcard.features.card.model.ScratchCardState
import sk.lukasvyletel.scratchcard.features.card.repository.ScratchCardRepository
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class ScratchCardRepositoryTest {

    private fun createRepository(
        scratchCardCodeGenerator: ScratchCardCodeGenerator,
        cpuBoundCoroutineDispatcher: CoroutineDispatcher,
        activationCoroutineScope: CoroutineScope,
        versionService: VersionService = { ActivationVersion("287028") },
    ): ScratchCardRepository = ScratchCardModule(
        scratchCardCodeGenerator = scratchCardCodeGenerator,
        cpuBoundCoroutineDispatcher = cpuBoundCoroutineDispatcher,
        activationCoroutineScope = activationCoroutineScope,
        versionService = versionService,
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
    fun `GIVEN two simultaneous activation requests for the same card WHEN activation succeeds THEN API is called once`() =
        runTest {
            val activationScope = CoroutineScope(StandardTestDispatcher(testScheduler) + Job())
            val activationStarted = CompletableDeferred<Unit>()
            val completeActivation = CompletableDeferred<Unit>()
            var activationCallCount = 0
            val repository = createRepository(
                scratchCardCodeGenerator = { "scratch-code" },
                cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
                activationCoroutineScope = activationScope,
                versionService = {
                    activationCallCount += 1
                    activationStarted.complete(Unit)
                    completeActivation.await()
                    ActivationVersion("287028")
                },
            )
            repository.scratchCard()
            runCurrent()
            advanceTimeBy(2.seconds)
            runCurrent()
            val scratchedCard = repository.cardStateFlow.value as ScratchCardState.Scratched

            repository.activateCard(scratchedCard)
            runCurrent()
            activationStarted.await()

            repository.activateCard(scratchedCard)
            runCurrent()

            assertEquals(1, activationCallCount)

            completeActivation.complete(Unit)
            runCurrent()

            assertEquals(1, activationCallCount)
            assertEquals(ScratchCardState.Activated("scratch-code"), repository.cardStateFlow.value)
            activationScope.cancel()
        }

    @Test
    fun `GIVEN two simultaneous activation requests for the same card WHEN first activation fails THEN API is called once`() =
        runTest {
            val activationScope = CoroutineScope(StandardTestDispatcher(testScheduler) + Job())
            val activationStarted = CompletableDeferred<Unit>()
            val failActivation = CompletableDeferred<Unit>()
            var activationCallCount = 0
            val repository = createRepository(
                scratchCardCodeGenerator = { "scratch-code" },
                cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
                activationCoroutineScope = activationScope,
                versionService = {
                    activationCallCount += 1
                    activationStarted.complete(Unit)
                    failActivation.await()
                    error("Activation failed")
                },
            )
            repository.scratchCard()
            runCurrent()
            advanceTimeBy(2.seconds)
            runCurrent()
            val scratchedCard = repository.cardStateFlow.value as ScratchCardState.Scratched

            repository.activateCard(scratchedCard)
            runCurrent()
            activationStarted.await()

            repository.activateCard(scratchedCard)
            runCurrent()

            assertEquals(1, activationCallCount)

            failActivation.complete(Unit)
            runCurrent()

            assertEquals(1, activationCallCount)
            assertEquals(scratchedCard, repository.cardStateFlow.value)
            assertEquals(ActivationError.UNEXPECTED_ERROR, repository.activationErrorFlow.value)
            activationScope.cancel()
        }

    @Test
    fun `GIVEN an activation request for a different card WHEN request runs THEN API is not called`() =
        runTest {
            val activationScope = CoroutineScope(StandardTestDispatcher(testScheduler) + Job())
            var activationCallCount = 0
            val repository = createRepository(
                scratchCardCodeGenerator = { "scratch-code" },
                cpuBoundCoroutineDispatcher = StandardTestDispatcher(testScheduler),
                activationCoroutineScope = activationScope,
                versionService = {
                    activationCallCount += 1
                    ActivationVersion("287028")
                },
            )
            repository.scratchCard()
            runCurrent()
            advanceTimeBy(2.seconds)
            runCurrent()
            val scratchedCard = repository.cardStateFlow.value as ScratchCardState.Scratched

            repository.activateCard(ScratchCardState.Scratched("other-code"))
            runCurrent()

            assertEquals(0, activationCallCount)
            assertEquals(scratchedCard, repository.cardStateFlow.value)
            activationScope.cancel()
        }
}
