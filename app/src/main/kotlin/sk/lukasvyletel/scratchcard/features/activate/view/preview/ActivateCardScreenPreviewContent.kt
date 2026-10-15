@file:ExcludeFromCoverage

package sk.lukasvyletel.scratchcard.features.activate.view.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import sk.lukasvyletel.scratchcard.core.annotations.ExcludeFromCoverage
import sk.lukasvyletel.scratchcard.core.dependency_injection.LocalInjector
import sk.lukasvyletel.scratchcard.core.dependency_injection.PreviewInjector
import sk.lukasvyletel.scratchcard.features.activate.view.ActivateCardScreen

@Preview
@Composable
fun ActivateCardScreenPreviewContent(
    code: String? = null,
    isActivating: Boolean = false,
    showError: Boolean = false,
) {
    CompositionLocalProvider(LocalInjector provides PreviewInjector()) {
        ActivateCardScreen(code = code, isActivating = isActivating, showError = showError)
    }
}
