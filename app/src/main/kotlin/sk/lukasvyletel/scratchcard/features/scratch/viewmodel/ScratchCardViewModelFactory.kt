package sk.lukasvyletel.scratchcard.features.scratch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import sk.lukasvyletel.scratchcard.features.card.dependency_injection.ScratchCardModule

class ScratchCardViewModelFactory(
    private val scratchCardModule: ScratchCardModule,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        if (modelClass.isAssignableFrom(ScratchCardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScratchCardViewModel(
                scratchCardRepository = scratchCardModule.scratchCardRepository,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
