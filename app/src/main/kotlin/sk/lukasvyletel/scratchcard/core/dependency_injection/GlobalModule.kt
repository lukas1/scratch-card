package sk.lukasvyletel.scratchcard.core.dependency_injection

import sk.lukasvyletel.scratchcard.core.navigation.Navigation

class GlobalModule {
    val navigation: Navigation = Navigation()
}
