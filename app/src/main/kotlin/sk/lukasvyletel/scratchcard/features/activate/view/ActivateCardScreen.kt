package sk.lukasvyletel.scratchcard.features.activate.view

import androidx.compose.runtime.Composable
import sk.lukasvyletel.scratchcard.R
import sk.lukasvyletel.scratchcard.shared.ui.DestinationScreen

@Composable
fun ActivateCardScreen() {
    DestinationScreen(
        title = R.string.activation_screen_title,
        description = R.string.activation_screen_description,
    )
}
