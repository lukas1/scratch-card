package sk.lukasvyletel.scratchcard.features.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sk.lukasvyletel.scratchcard.R
import sk.lukasvyletel.scratchcard.features.card.ScratchCardState

@Composable
fun ScratchCardArtwork(cardState: ScratchCardState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(226.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        colorResource(R.color.scratch_card_light),
                        colorResource(R.color.scratch_card_dark),
                    ),
                ),
            )
            .testTag("scratchCard"),
    ) {
        CardDecoration()
        Column(modifier = Modifier.fillMaxSize().padding(22.dp)) {
            CardHeader()
            Spacer(modifier = Modifier.weight(1f))
            CodePanel(cardState.code())
            Spacer(modifier = Modifier.weight(1f))
            CardFooter()
        }
    }
}

@Composable
fun CodePanel(code: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(
                colorResource(if (code == null) R.color.scratch_soft_green else R.color.scratch_lime),
            )
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.home_code_label),
            color = colorResource(R.color.scratch_ink).copy(alpha = 0.7f),
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
        )
        Text(
            text = code ?: stringResource(R.string.home_hidden_code),
            modifier = Modifier.fillMaxWidth().testTag("cardCode"),
            color = colorResource(R.color.scratch_ink),
            fontFamily = FontFamily.Monospace,
            fontSize = if (code == null) 25.sp else 14.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 18.sp,
            textAlign = TextAlign.Start,
            maxLines = 2,
        )
    }
}

private fun ScratchCardState.code(): String? = when (this) {
    ScratchCardState.Unscratched -> null
    is ScratchCardState.Scratched, is ScratchCardState.Activated -> code
}
