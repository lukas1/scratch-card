package sk.lukasvyletel.scratchcard

class VersionServiceBaseUrl {
    fun get(): String = VERSION_SERVICE_BASE_URL

    private companion object {
        // This is a just a placeholder URL to be replaced with real URL
        const val VERSION_SERVICE_BASE_URL = "https://10.0.2.2:8080/"
    }
}
