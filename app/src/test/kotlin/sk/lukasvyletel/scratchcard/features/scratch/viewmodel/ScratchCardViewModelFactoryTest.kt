package sk.lukasvyletel.scratchcard.features.scratch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import org.junit.Assert.assertThrows
import org.junit.Test
import sk.lukasvyletel.scratchcard.test.MockInjector

class ScratchCardViewModelFactoryTest {
    @Test
    fun `GIVEN unsupported ViewModel type WHEN factory creates it THEN an error is thrown`() {
        val factory = ScratchCardViewModelFactory(MockInjector().scratchCardModule)

        assertThrows(IllegalArgumentException::class.java) {
            factory.create(UnsupportedViewModel::class.java, CreationExtras.Empty)
        }
    }
}

private class UnsupportedViewModel : ViewModel()
