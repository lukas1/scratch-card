package sk.lukasvyletel.scratchcard.shared.ui

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sk.lukasvyletel.scratchcard.R
import sk.lukasvyletel.scratchcard.core.annotations.ExcludeFromCoverage
import sk.lukasvyletel.scratchcard.shared.ui.preview.ScratchCardPreview

private const val DECORATION_BORDER_ALPHA = 0.12f
private const val UPPER_CIRCLE_X = 0.95f
private const val UPPER_CIRCLE_Y = 0.05f
private const val LOWER_CIRCLE_X = 0.03f
private const val LOWER_CIRCLE_Y = 1.06f

@Composable
fun ScratchCardFrame(
    height: Dp,
    tag: String,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        colorResource(R.color.scratch_card_light),
                        colorResource(R.color.scratch_card_dark),
                    ),
                ),
            )
            .testTag(tag),
    ) {
        CardDecoration()
        Column(modifier = Modifier.fillMaxSize().padding(22.dp)) {
            CardHeader()
            Spacer(Modifier.weight(1f))
            content()
            Spacer(Modifier.weight(1f))
            CardFooter()
        }
    }
}

@Composable
fun ScratchCardCodePanel(
    code: String?,
    height: Dp,
    @ColorRes backgroundColor: Int,
    tag: String,
    @StringRes status: Int? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(17.dp))
            .background(colorResource(backgroundColor))
            .padding(18.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.card_code_label),
            color = colorResource(R.color.scratch_ink).copy(alpha = 0.7f),
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
        )
        Text(
            text = code ?: stringResource(R.string.card_hidden_code),
            modifier = Modifier.fillMaxWidth().testTag(tag),
            color = colorResource(R.color.scratch_ink),
            fontFamily = FontFamily.Monospace,
            fontSize = if (code == null) 25.sp else 14.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 19.sp,
            maxLines = 2,
        )
        if (status != null) {
            Text(
                text = stringResource(status),
                color = colorResource(R.color.scratch_muted_ink),
                fontSize = 11.sp,
            )
        }
    }
}

@Composable
private fun CardDecoration() {
    val lime = colorResource(R.color.scratch_lime)
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = Color.White.copy(alpha = 0.055f),
            radius = size.minDimension * 0.62f,
            center = Offset(size.width * UPPER_CIRCLE_X, size.height * UPPER_CIRCLE_Y),
        )
        drawCircle(
            color = lime.copy(alpha = DECORATION_BORDER_ALPHA),
            radius = size.minDimension * 0.34f,
            center = Offset(size.width * LOWER_CIRCLE_X, size.height * LOWER_CIRCLE_Y),
        )
    }
}

@Composable
private fun CardHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.card_brand),
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
        )
        Text(
            text = stringResource(R.string.card_number),
            color = Color.White.copy(alpha = 0.7f),
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
        )
    }
}

@Composable
private fun CardFooter() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.card_footer),
            color = Color.White.copy(alpha = 0.76f),
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            letterSpacing = 1.sp,
        )
        Text(text = "✳", color = colorResource(R.color.scratch_lime), fontSize = 19.sp)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F5EF, widthDp = 380)
@ExcludeFromCoverage
@Composable
private fun ScratchCardFramePreview() {
    ScratchCardPreview()
}
