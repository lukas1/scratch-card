package sk.lukasvyletel.scratchcard.core.dependency_injection

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test
import sk.lukasvyletel.scratchcard.features.card.api.ActivationVersion
import sk.lukasvyletel.scratchcard.features.card.model.ScratchCardState

class SharedInjectorTest {
    @Test
    fun `GIVEN default code generation WHEN card is scratched THEN a code is created`() = runTest {
        val injector = SharedInjector(
            versionService = { ActivationVersion("287028") },
            delayFunction = {},
        )
        val repository = injector.scratchCardModule.scratchCardRepository

        repository.scratchCard()

        val scratchedCard = repository.cardStateFlow.value as ScratchCardState.Scratched
        assertTrue(scratchedCard.code.isNotBlank())
    }
}
