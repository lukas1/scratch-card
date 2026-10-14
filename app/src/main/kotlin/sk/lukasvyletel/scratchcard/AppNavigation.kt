package sk.lukasvyletel.scratchcard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import sk.lukasvyletel.scratchcard.features.home.view.HomeScreen

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
                    HomeScreen(modifier = Modifier.fillMaxSize())
                }
            }
        },
    )
}
