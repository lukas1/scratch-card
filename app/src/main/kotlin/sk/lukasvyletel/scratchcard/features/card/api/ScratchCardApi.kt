package sk.lukasvyletel.scratchcard.features.card.api

import kotlinx.coroutines.CancellationException

class ScratchCardApi(
    private val versionService: VersionService,
) {
    suspend fun activateCard(code: String): Result<Boolean> = try {
        val version = versionService.getVersion(code).android.toInt()
        Result.success(version > ACTIVATION_VERSION_THRESHOLD)
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    companion object {
        const val ACTIVATION_VERSION_THRESHOLD = 277028
    }
}
