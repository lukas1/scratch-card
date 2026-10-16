package sk.lukasvyletel.scratchcard.test

import kotlinx.coroutines.CoroutineDispatcher
import kotlin.coroutines.CoroutineContext

class QueuedCoroutineDispatcher : CoroutineDispatcher() {
    private val queuedTasks = ArrayDeque<Runnable>()

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        queuedTasks.addLast(block)
    }

    fun runQueuedTasks() {
        while (queuedTasks.isNotEmpty()) {
            queuedTasks.removeFirst().run()
        }
    }
}
