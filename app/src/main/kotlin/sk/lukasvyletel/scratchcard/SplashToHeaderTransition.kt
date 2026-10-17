package sk.lukasvyletel.scratchcard

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.compose.ui.geometry.Rect
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.time.Duration.Companion.milliseconds

class SplashToHeaderTransition(private val splashScreen: SplashScreen) :
    DefaultLifecycleObserver {
    private val scope = CoroutineScope(Job() + Dispatchers.Main.immediate)
    private val splashViewReady = CompletableDeferred<SplashScreenViewProvider>()
    private val headerIconReady = CompletableDeferred<Rect>()

    fun registerExitListener() {
        scope.launch {
            val provider = splashViewReady.await()
            try {
                val target = withTimeoutOrNull(HEADER_ICON_WAIT_TIMEOUT_MS.milliseconds) {
                    headerIconReady.await()
                }
                if (target != null) {
                    animate(provider, target)
                } else {
                    Log.e(TAG, "Home header icon bounds were not reported before splash exit")
                }
            } finally {
                provider.remove()
            }
        }
        splashScreen.setOnExitAnimationListener { provider ->
            if (!scope.isActive || !splashViewReady.complete(provider)) {
                provider.remove()
            }
        }
    }

    fun onHeaderIconPositioned(bounds: Rect) {
        headerIconReady.complete(bounds)
    }

    private suspend fun animate(provider: SplashScreenViewProvider, target: Rect) {
        val icon = provider.iconView
        if (icon.width == 0 || icon.height == 0) {
            return
        }
        if (target.width <= 0f || target.height <= 0f) {
            return
        }

        val iconLocation = IntArray(2)
        icon.getLocationInWindow(iconLocation)
        val horizontalTravel = target.center.x - (iconLocation[0] + icon.width / 2f)
        val verticalTravel = target.center.y - (iconLocation[1] + icon.height / 2f)
        val endScaleX = target.width / icon.width
        val endScaleY = target.height / icon.height

        suspendCancellableCoroutine { continuation ->
            val animator = ValueAnimator.ofFloat(0f, 1f).apply {
                duration = SPLASH_EXIT_DURATION_MS
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener { animation ->
                    val progress = animation.animatedValue as Float
                    icon.translationX = horizontalTravel * progress
                    icon.translationY = verticalTravel * progress
                    icon.scaleX = 1f + (endScaleX - 1f) * progress
                    icon.scaleY = 1f + (endScaleY - 1f) * progress
                    if (progress >= ICON_CROSSFADE_START) {
                        provider.view.alpha = (1f - progress) / (1f - ICON_CROSSFADE_START)
                    }
                }
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        if (continuation.isActive) continuation.resume(Unit)
                    }
                })
            }
            continuation.invokeOnCancellation { animator.cancel() }
            animator.start()
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        scope.cancel()
    }

    companion object {
        private const val SPLASH_EXIT_DURATION_MS = 500L
        private const val HEADER_ICON_WAIT_TIMEOUT_MS = 500L
        private const val ICON_CROSSFADE_START = 0.8f
        private const val TAG = "SplashToHeaderTransition"

        fun install(activity: ComponentActivity): SplashToHeaderTransition =
            SplashToHeaderTransition(activity.installSplashScreen())
    }
}
