package sk.lukasvyletel.scratchcard.features.home.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sk.lukasvyletel.scratchcard.R
import sk.lukasvyletel.scratchcard.features.card.ScratchCardState

@Composable
fun HomeIntroduction(cardState: ScratchCardState) {
    Text(
        text = stringResource(cardState.titleResource()),
        color = colorResource(R.color.scratch_ink),
        fontFamily = FontFamily.Serif,
        fontSize = 43.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 46.sp,
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = stringResource(cardState.descriptionResource()),
        color = colorResource(R.color.scratch_muted_ink),
        fontSize = 15.sp,
        lineHeight = 22.sp,
    )
}

private fun ScratchCardState.titleResource(): Int = when (this) {
    ScratchCardState.Unscratched -> R.string.home_title_unscratched
    is ScratchCardState.Scratched -> R.string.home_title_scratched
    is ScratchCardState.Activated -> R.string.home_title_activated
}

private fun ScratchCardState.descriptionResource(): Int = when (this) {
    ScratchCardState.Unscratched -> R.string.home_description_unscratched
    is ScratchCardState.Scratched -> R.string.home_description_scratched
    is ScratchCardState.Activated -> R.string.home_description_activated
}
