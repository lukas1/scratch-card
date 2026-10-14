package sk.lukasvyletel.scratchcard.features.home.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import sk.lukasvyletel.scratchcard.R

private const val STATUS_BORDER_ALPHA = 0.12f
private const val UPPER_CIRCLE_X = 0.95f
private const val UPPER_CIRCLE_Y = 0.05f
private const val LOWER_CIRCLE_X = 0.03f
private const val LOWER_CIRCLE_Y = 1.06f

@Composable
fun CardDecoration() {
    val lime = colorResource(R.color.scratch_lime)
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = Color.White.copy(alpha = 0.055f),
            radius = size.minDimension * 0.62f,
            center = Offset(size.width * UPPER_CIRCLE_X, size.height * UPPER_CIRCLE_Y),
        )
        drawCircle(
            color = lime.copy(alpha = STATUS_BORDER_ALPHA),
            radius = size.minDimension * 0.34f,
            center = Offset(size.width * LOWER_CIRCLE_X, size.height * LOWER_CIRCLE_Y),
        )
    }
}
