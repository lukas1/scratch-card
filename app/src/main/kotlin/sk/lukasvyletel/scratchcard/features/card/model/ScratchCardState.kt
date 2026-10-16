package sk.lukasvyletel.scratchcard.features.card.model

sealed interface ScratchCardState {
    data object Unscratched : ScratchCardState

    sealed interface WithCode : ScratchCardState {
        val code: String
    }

    data class Scratched(override val code: String) : WithCode

    data class Activated(override val code: String) : WithCode
}
