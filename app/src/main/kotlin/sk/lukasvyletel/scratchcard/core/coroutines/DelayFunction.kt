package sk.lukasvyletel.scratchcard.core.coroutines

import kotlinx.coroutines.delay
import kotlin.time.Duration

typealias DelayFunction = suspend (Duration) -> Unit

val coroutineDelay: DelayFunction = { delay(it) }
