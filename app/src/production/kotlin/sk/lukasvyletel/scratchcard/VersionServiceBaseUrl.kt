package sk.lukasvyletel.scratchcard

class VersionServiceBaseUrl {
    fun get(): String = VERSION_SERVICE_BASE_URL

    private companion object {
        const val VERSION_SERVICE_BASE_URL = "https://api.o2.sk/"
    }
}
