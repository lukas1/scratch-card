package sk.lukasvyletel.scratchcard.features.scratch.view

import androidx.compose.runtime.Composable
import sk.lukasvyletel.scratchcard.R
import sk.lukasvyletel.scratchcard.shared.ui.DestinationScreen

@Composable
fun ScratchCardScreen() {
    DestinationScreen(
        title = R.string.scratch_screen_title,
        description = R.string.scratch_screen_description,
    )
}
