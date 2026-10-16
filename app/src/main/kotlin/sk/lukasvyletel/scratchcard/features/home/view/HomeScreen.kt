package sk.lukasvyletel.scratchcard.features.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import sk.lukasvyletel.scratchcard.ActivationRoute
import sk.lukasvyletel.scratchcard.R
import sk.lukasvyletel.scratchcard.ScratchRoute
import sk.lukasvyletel.scratchcard.core.annotations.ExcludeFromCoverage
import sk.lukasvyletel.scratchcard.core.dependency_injection.LocalInjector
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
    val navigation = LocalInjector.current.globalModule.navigation
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.scratch_cream))
            .verticalScroll(rememberScrollState())
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
            onScratchClick = { navigation.navigateTo(ScratchRoute) },
            onActivationClick = { navigation.navigateTo(ActivationRoute) },
        )
    }
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
