package sk.lukasvyletel.scratchcard.core.dependency_injection

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import sk.lukasvyletel.scratchcard.features.card.api.ActivationVersion
import sk.lukasvyletel.scratchcard.features.card.model.ScratchCardState
import sk.lukasvyletel.scratchcard.features.card.dependency_injection.ScratchCardBindings
import sk.lukasvyletel.scratchcard.test.TestBindings

class SharedInjectorTest {
    @Test
    fun `GIVEN unused bindings WHEN modules are requested THEN each module is created once`() {
        var globalRequests = 0
        var cardRequests = 0
        val injector = SharedInjector(object : TestBindings {
            override fun globalBindings(): GlobalBindings {
                globalRequests += 1
                return GlobalBindings()
            }

            override fun scratchCardBindings(): ScratchCardBindings {
                cardRequests += 1
                return ScratchCardBindings(
                    versionServiceFactory = { { ActivationVersion("287028") } },
                    scratchCardCodeGenerator = { "code" },
                )
            }
        })

        assertEquals(0, globalRequests)
        assertEquals(0, cardRequests)
        assertSame(injector.globalModule, injector.globalModule)
        assertEquals(0, globalRequests)
        assertSame(injector.scratchCardModule, injector.scratchCardModule)
        assertEquals(1, globalRequests)
        assertEquals(1, cardRequests)
        assertSame(injector.activateCardModule, injector.activateCardModule)
        assertSame(
            injector.scratchCardModule.scratchCardRepository,
            injector.activateCardModule.scratchCardRepository,
        )
        assertEquals(1, globalRequests)
        assertEquals(1, cardRequests)
    }

    @Test
    fun `GIVEN default code generation WHEN card is scratched THEN a code is created`() = runTest {
        val injector = SharedInjector(object : TestBindings {
            override fun globalBindings(): GlobalBindings = GlobalBindings(delayFunction = {})

            override fun scratchCardBindings(): ScratchCardBindings = ScratchCardBindings(
                versionServiceFactory = { { ActivationVersion("287028") } },
                activationCoroutineScope = backgroundScope,
            )
        })
        val repository = injector.scratchCardModule.scratchCardRepository

        repository.scratchCard()

        val scratchedCard = repository.cardStateFlow.value as ScratchCardState.Scratched
        assertTrue(scratchedCard.code.isNotBlank())
    }
}
