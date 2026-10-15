package sk.lukasvyletel.scratchcard.features.home.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import sk.lukasvyletel.scratchcard.R
import sk.lukasvyletel.scratchcard.features.card.ScratchCardState
import sk.lukasvyletel.scratchcard.shared.ui.ScratchCardCodePanel
import sk.lukasvyletel.scratchcard.shared.ui.ScratchCardFrame

@Composable
fun ScratchCardArtwork(cardState: ScratchCardState) {
    val code = cardState.code()
    ScratchCardFrame(height = 226.dp, tag = "scratchCard") {
        ScratchCardCodePanel(
            code = code,
            height = 108.dp,
            backgroundColor = if (code == null) R.color.scratch_soft_green else R.color.scratch_lime,
            tag = "cardCode",
        )
    }
}

private fun ScratchCardState.code(): String? = when (this) {
    ScratchCardState.Unscratched -> null
    is ScratchCardState.Scratched, is ScratchCardState.Activated -> code
}
