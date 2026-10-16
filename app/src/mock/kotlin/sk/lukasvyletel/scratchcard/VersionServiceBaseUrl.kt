package sk.lukasvyletel.scratchcard

class VersionServiceBaseUrl {
    private val server = MockVersionServer()

    fun get(): String = server.start()
}
