@file:ExcludeFromCoverage

package sk.lukasvyletel.scratchcard.features.home.view.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import sk.lukasvyletel.scratchcard.core.annotations.ExcludeFromCoverage
import sk.lukasvyletel.scratchcard.core.dependency_injection.LocalInjector
import sk.lukasvyletel.scratchcard.core.dependency_injection.PreviewInjector
import sk.lukasvyletel.scratchcard.features.card.model.ScratchCardState
import sk.lukasvyletel.scratchcard.features.home.view.HomeScreen

// Moved to a standalone file because Kover cannot exclude lambdas from coverage otherwise
@Preview
@Composable
fun HomeScreenPreviewContent(cardState: ScratchCardState = ScratchCardState.Unscratched) {
    CompositionLocalProvider(LocalInjector provides PreviewInjector()) {
        HomeScreen(cardState)
    }
}
