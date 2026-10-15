package sk.lukasvyletel.scratchcard.core.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import sk.lukasvyletel.scratchcard.AppRoute
import sk.lukasvyletel.scratchcard.HomeRoute

class Navigation {
    val backStack: SnapshotStateList<AppRoute> = mutableStateListOf(HomeRoute)

    fun navigateTo(appRoute: AppRoute) {
        backStack.add(appRoute)
    }

    fun goBack(): AppRoute? = backStack.removeLastOrNull()
}
