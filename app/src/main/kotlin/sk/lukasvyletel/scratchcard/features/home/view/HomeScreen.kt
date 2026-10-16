package sk.lukasvyletel.scratchcard.features.home.view

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.collect
import sk.lukasvyletel.scratchcard.ActivationRoute
import sk.lukasvyletel.scratchcard.R
import sk.lukasvyletel.scratchcard.ScratchRoute
import sk.lukasvyletel.scratchcard.core.annotations.ExcludeFromCoverage
import sk.lukasvyletel.scratchcard.core.dependency_injection.LocalInjector
import sk.lukasvyletel.scratchcard.features.card.ActivationError
import sk.lukasvyletel.scratchcard.features.card.ScratchCardRepository
import sk.lukasvyletel.scratchcard.features.card.ScratchCardState
import sk.lukasvyletel.scratchcard.features.home.view.preview.HomeScreenPreviewContent

@Composable
fun HomeScreen() {
    val cardState by LocalInjector.current.scratchCardModule.scratchCardRepository.cardStateFlow
        .collectAsStateWithLifecycle()
    HomeScreen(cardState)
}

@Composable
fun HomeScreen(cardState: ScratchCardState) {
    val injector = LocalInjector.current
    val navigation = injector.globalModule.navigation
    val scratchCardRepository = injector.scratchCardModule.scratchCardRepository
    val snackbarHostState = rememberHomeSnackbarHostState(scratchCardRepository)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = colorResource(R.color.scratch_cream),
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.scratch_cream))
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .padding(horizontal = 24.dp, vertical = 20.dp),
        ) {
            HomeHeader()
            Spacer(modifier = Modifier.height(24.dp))
            HomeIntroduction(cardState)
            Spacer(modifier = Modifier.height(20.dp))
            StateHeading(cardState)
            Spacer(modifier = Modifier.height(16.dp))
            ScratchCardArtwork(cardState)
            Spacer(modifier = Modifier.height(20.dp))
            HomeActionButtons(
                scratchEnabled = cardState == ScratchCardState.Unscratched,
                activationEnabled = cardState is ScratchCardState.Scratched,
                onScratchClick = { navigation.navigateTo(ScratchRoute) },
                onActivationClick = { navigation.navigateTo(ActivationRoute) },
            )
        }
    }
}

@Composable
private fun rememberHomeSnackbarHostState(
    scratchCardRepository: ScratchCardRepository,
): SnackbarHostState {
    val resources = LocalResources.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(scratchCardRepository, snackbarHostState, resources) {
        scratchCardRepository.activationErrorFlow
            .filterNotNull()
            .collect { error ->
                scratchCardRepository.dismissActivationError()
                snackbarHostState.showSnackbar(resources.getString(error.message()))
            }
    }

    return snackbarHostState
}

@StringRes
private fun ActivationError.message(): Int = when (this) {
    ActivationError.ACTIVATION_FAILED -> R.string.activation_failed_error_body
    ActivationError.UNEXPECTED_ERROR -> R.string.activation_unexpected_error_body
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F5EF, widthDp = 380)
@ExcludeFromCoverage
@Composable
private fun HomeScreenUnscratchedPreview() {
    HomeScreenPreviewContent(ScratchCardState.Unscratched)
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F5EF, widthDp = 380)
@ExcludeFromCoverage
@Composable
private fun HomeScreenScratchedPreview() {
    HomeScreenPreviewContent(ScratchCardState.Scratched("SC-4829-7316"))
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F5EF, widthDp = 380)
@ExcludeFromCoverage
@Composable
private fun HomeScreenActivatedPreview() {
    HomeScreenPreviewContent(ScratchCardState.Activated("SC-4829-7316"))
}
