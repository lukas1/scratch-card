package sk.lukasvyletel.scratchcard.features.scratch.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sk.lukasvyletel.scratchcard.R
import sk.lukasvyletel.scratchcard.core.annotations.ExcludeFromCoverage
import sk.lukasvyletel.scratchcard.core.dependency_injection.LocalInjector
import sk.lukasvyletel.scratchcard.core.dependency_injection.PreviewInjector
import sk.lukasvyletel.scratchcard.shared.ui.DestinationAction
import sk.lukasvyletel.scratchcard.shared.ui.DestinationScreen
import sk.lukasvyletel.scratchcard.shared.ui.SectionLabel
import sk.lukasvyletel.scratchcard.shared.ui.ScratchCardFrame

@Composable
fun ScratchCardScreen(
    code: String? = null,
    isRevealing: Boolean = false,
    onRevealClick: () -> Unit = {},
) {
    DestinationScreen(
        title = R.string.scratch_screen_title,
        description = R.string.scratch_screen_description,
        step = R.string.scratch_step,
    ) {
        SectionLabel(R.string.scratch_card_label, Modifier.padding(top = 30.dp, bottom = 12.dp))
        RevealCard(code, isRevealing)
        ScratchInstructions()
        DestinationAction(
            label = when {
                code != null -> R.string.scratch_revealed_action
                isRevealing -> R.string.scratch_revealing_action
                else -> R.string.scratch_reveal_action
            },
            tag = "revealButton",
            onClick = onRevealClick,
            modifier = Modifier.padding(top = 24.dp),
            enabled = code == null && !isRevealing,
        )
        Text(
            text = stringResource(R.string.scratch_action_hint),
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 20.dp),
            color = colorResource(R.color.scratch_muted_ink),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun RevealCard(code: String?, isRevealing: Boolean) {
    ScratchCardFrame(height = 266.dp, tag = "revealCard") {
        RevealFoil(code, isRevealing)
    }
}

@Composable
private fun RevealFoil(code: String?, isRevealing: Boolean) {
    val title = when {
        code != null -> code
        isRevealing -> stringResource(R.string.scratch_revealing_title)
        else -> stringResource(R.string.scratch_foil_title)
    }
    val caption = if (code == null) R.string.scratch_foil_caption else R.string.scratch_revealed_caption
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(134.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        colorResource(R.color.scratch_foil_light),
                        colorResource(R.color.scratch_foil_dark),
                    ),
                ),
            )
            .border(1.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(17.dp))
            .testTag("scratchFoil"),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "✳",
                color = colorResource(R.color.scratch_ink),
                fontSize = 27.sp,
                lineHeight = 30.sp,
            )
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth().testTag("scratchCode"),
                color = colorResource(R.color.scratch_ink),
                fontFamily = if (code == null) FontFamily.Serif else FontFamily.Monospace,
                fontSize = if (code == null) 25.sp else 14.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
                maxLines = 2,
            )
            Text(
                text = stringResource(caption),
                color = colorResource(R.color.scratch_muted_ink),
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.6.sp,
            )
        }
    }
}

@Composable
private fun ScratchInstructions() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 22.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .background(colorResource(R.color.scratch_soft_green), CircleShape)
                .padding(horizontal = 14.dp, vertical = 11.dp),
        ) {
            Text(text = "✳", color = colorResource(R.color.scratch_ink), fontSize = 19.sp)
        }
        Column(modifier = Modifier.padding(start = 14.dp)) {
            Text(
                text = stringResource(R.string.scratch_instruction_title),
                color = colorResource(R.color.scratch_ink),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(R.string.scratch_instruction_body),
                color = colorResource(R.color.scratch_muted_ink),
                fontSize = 13.sp,
                lineHeight = 18.sp,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F5EF, widthDp = 380)
@ExcludeFromCoverage
@Composable
private fun ScratchCardScreenReadyToRevealPreview() {
    ScratchCardScreenPreviewContent()
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F5EF, widthDp = 380)
@ExcludeFromCoverage
@Composable
private fun ScratchCardScreenRevealingPreview() {
    ScratchCardScreenPreviewContent(isRevealing = true)
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F5EF, widthDp = 380)
@ExcludeFromCoverage
@Composable
private fun ScratchCardScreenRevealedPreview() {
    ScratchCardScreenPreviewContent(code = "SC-4829-7316")
}

@Composable
private fun ScratchCardScreenPreviewContent(code: String? = null, isRevealing: Boolean = false) {
    CompositionLocalProvider(LocalInjector provides PreviewInjector()) {
        ScratchCardScreen(code = code, isRevealing = isRevealing)
    }
}
