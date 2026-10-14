package sk.lukasvyletel.scratchcard.features.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sk.lukasvyletel.scratchcard.R
import sk.lukasvyletel.scratchcard.features.card.ScratchCardState

private const val STATUS_BORDER_ALPHA = 0.12f

@Composable
fun StateHeading(cardState: ScratchCardState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.home_card_label),
            color = colorResource(R.color.scratch_ink),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
        )
        Row(
            modifier = Modifier
                .border(
                    1.dp,
                    colorResource(R.color.scratch_ink).copy(alpha = STATUS_BORDER_ALPHA),
                    CircleShape,
                )
                .padding(horizontal = 11.dp, vertical = 7.dp)
                .semantics(mergeDescendants = true) {}
                .testTag("cardStatus"),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.size(7.dp).background(colorResource(R.color.scratch_ink), CircleShape))
            Text(
                text = stringResource(cardState.statusResource()),
                modifier = Modifier.padding(start = 7.dp),
                color = colorResource(R.color.scratch_ink),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.7.sp,
            )
        }
    }
}

private fun ScratchCardState.statusResource(): Int = when (this) {
    ScratchCardState.Unscratched -> R.string.card_state_unscratched
    is ScratchCardState.Scratched -> R.string.card_state_scratched
    is ScratchCardState.Activated -> R.string.card_state_activated
}
