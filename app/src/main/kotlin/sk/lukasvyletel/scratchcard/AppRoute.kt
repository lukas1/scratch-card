package sk.lukasvyletel.scratchcard

sealed interface AppRoute

data object HomeRoute : AppRoute

data object ScratchRoute : AppRoute

data object ActivationRoute : AppRoute
