package sk.lukasvyletel.scratchcard.features.activate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import sk.lukasvyletel.scratchcard.features.activate.dependency_injection.ActivateCardModule

class ActivateCardViewModelFactory(
    private val activateCardModule: ActivateCardModule,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        if (modelClass.isAssignableFrom(ActivateCardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ActivateCardViewModel(
                scratchCardRepository = activateCardModule.scratchCardRepository,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
