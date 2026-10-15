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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import sk.lukasvyletel.scratchcard.ActivationRoute
import sk.lukasvyletel.scratchcard.R
import sk.lukasvyletel.scratchcard.ScratchRoute
import sk.lukasvyletel.scratchcard.core.dependency_injection.LocalInjector
import sk.lukasvyletel.scratchcard.features.card.ScratchCardState

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
