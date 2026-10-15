@file:ExcludeFromCoverage

package sk.lukasvyletel.scratchcard.features.scratch.view.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import sk.lukasvyletel.scratchcard.core.annotations.ExcludeFromCoverage
import sk.lukasvyletel.scratchcard.core.dependency_injection.LocalInjector
import sk.lukasvyletel.scratchcard.core.dependency_injection.PreviewInjector
import sk.lukasvyletel.scratchcard.features.scratch.view.ScratchCardScreenContent

@Preview
@Composable
fun ScratchCardScreenPreviewContent(code: String? = null, isRevealing: Boolean = false) {
    CompositionLocalProvider(LocalInjector provides PreviewInjector()) {
        ScratchCardScreenContent(code = code, isRevealing = isRevealing, onRevealClick = {})
    }
}
