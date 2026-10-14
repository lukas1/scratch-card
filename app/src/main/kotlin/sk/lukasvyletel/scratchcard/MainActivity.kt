package sk.lukasvyletel.scratchcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import sk.lukasvyletel.scratchcard.core.dependency_injection.LocalInjector

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                CompositionLocalProvider(
                    LocalInjector provides (application as ScratchCardApplication).injector(),
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = colorResource(R.color.scratch_cream),
                    ) { innerPadding ->
                        AppNavigation(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                        )
                    }
                }
            }
        }
    }
}
