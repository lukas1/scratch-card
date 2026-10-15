package sk.lukasvyletel.scratchcard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import sk.lukasvyletel.scratchcard.core.dependency_injection.LocalInjector
import sk.lukasvyletel.scratchcard.features.activate.view.ActivateCardScreen
import sk.lukasvyletel.scratchcard.features.card.ScratchCardState
import sk.lukasvyletel.scratchcard.features.home.view.HomeScreen
import sk.lukasvyletel.scratchcard.features.scratch.view.ScratchCardScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navigation = LocalInjector.current.globalModule.navigation

    NavDisplay(
        backStack = navigation.backStack,
        onBack = { navigation.goBack() },
        modifier = modifier,
        entryProvider = { key ->
            when (key) {
                HomeRoute -> NavEntry(key) {
                    HomeScreen(cardState = ScratchCardState.Unscratched)
                }

                ScratchRoute -> NavEntry(key) {
                    ScratchCardScreen()
                }

                ActivationRoute -> NavEntry(key) {
                    ActivateCardScreen()
                }
            }
        },
    )
}
