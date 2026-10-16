package sk.lukasvyletel.scratchcard

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class ScratchCardAppTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        classLoader: ClassLoader?,
        className: String?,
        context: Context?,
    ): Application = super.newApplication(
        classLoader,
        ScratchCardTestApplication::class.java.name,
        context,
    )
}
