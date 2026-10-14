package sk.lukasvyletel.scratchcard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import sk.lukasvyletel.scratchcard.features.activate.view.ActivateCardScreen
import sk.lukasvyletel.scratchcard.features.card.ScratchCardState
import sk.lukasvyletel.scratchcard.features.home.view.HomeScreen
import sk.lukasvyletel.scratchcard.features.scratch.view.ScratchCardScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val backStack = remember { mutableStateListOf<AppRoute>(HomeRoute) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        modifier = modifier,
        entryProvider = { key ->
            when (key) {
                HomeRoute -> NavEntry(key) {
                    HomeScreen(
                        cardState = ScratchCardState.Unscratched,
                        onScratchClick = { backStack.add(ScratchRoute) },
                        onActivationClick = { backStack.add(ActivationRoute) }
                    )
                }

                ScratchRoute -> NavEntry(key) {
                    ScratchCardScreen(onBack = { backStack.removeLastOrNull() })
                }

                ActivationRoute -> NavEntry(key) {
                    ActivateCardScreen(onBack = { backStack.removeLastOrNull() })
                }
            }
        },
    )
}
