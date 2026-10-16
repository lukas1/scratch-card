@file:ExcludeFromCoverage

package sk.lukasvyletel.scratchcard.features.activate.view.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import sk.lukasvyletel.scratchcard.core.annotations.ExcludeFromCoverage
import sk.lukasvyletel.scratchcard.core.dependency_injection.LocalInjector
import sk.lukasvyletel.scratchcard.core.dependency_injection.PreviewInjector
import sk.lukasvyletel.scratchcard.features.activate.view.ActivateCardScreenContent
import sk.lukasvyletel.scratchcard.features.activate.viewmodel.ActivationButtonState
import sk.lukasvyletel.scratchcard.features.card.model.ActivationError

// Moved to a standalone file because Kover cannot exclude lambdas from coverage otherwise
@Preview
@Composable
fun ActivateCardScreenPreviewContent(
    code: String? = null,
    isActivating: Boolean = false,
    activationError: ActivationError? = null,
) {
    CompositionLocalProvider(LocalInjector provides PreviewInjector()) {
        ActivateCardScreenContent(
            code = code,
            activationButtonState = if (isActivating) {
                ActivationButtonState.ACTIVATING
            } else {
                ActivationButtonState.READY
            },
            activationError = activationError,
        )
    }
}
