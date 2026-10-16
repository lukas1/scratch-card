package sk.lukasvyletel.scratchcard.features.activate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import sk.lukasvyletel.scratchcard.features.activate.dependency_injection.ActivateCardModule
import sk.lukasvyletel.scratchcard.test.MockInjector

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ActivateCardViewModelFactoryTest {
    @Test
    fun `GIVEN an unsupported model class WHEN factory creates a viewmodel THEN an error is thrown`() {
        val injector = MockInjector()
        val factory = ActivateCardViewModelFactory(
            ActivateCardModule(injector.scratchCardModule.scratchCardRepository),
        )

        assertThrows(IllegalArgumentException::class.java) {
            factory.create(UnsupportedViewModel::class.java, CreationExtras.Empty)
        }
    }

    private class UnsupportedViewModel : ViewModel()
}
