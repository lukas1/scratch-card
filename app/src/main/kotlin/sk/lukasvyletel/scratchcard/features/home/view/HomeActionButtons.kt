package sk.lukasvyletel.scratchcard.features.home.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sk.lukasvyletel.scratchcard.R

private val actionButtonShape = RoundedCornerShape(18.dp)
private val actionButtonHeight = 60.dp

@Composable
fun HomeActionButtons(
    scratchEnabled: Boolean,
    activationEnabled: Boolean,
    onScratchClick: () -> Unit,
    onActivationClick: () -> Unit,
    onResetClick: () -> Unit,
) {
    Text(
        text = stringResource(R.string.home_next_step),
        color = colorResource(R.color.scratch_muted_ink),
        fontFamily = FontFamily.Monospace,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
    )
    Button(
        onClick = onScratchClick,
        enabled = scratchEnabled,
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp).height(actionButtonHeight).testTag("scratchButton"),
        shape = actionButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.scratch_ink),
            contentColor = Color.White,
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
    ) {
        ActionContent(stringResource(R.string.home_scratch_action), Color.White)
    }
    val activationButtonModifier = Modifier
        .fillMaxWidth()
        .padding(top = 10.dp)
        .height(actionButtonHeight)
        .testTag("activationButton")
    if (activationEnabled) {
        OutlinedButton(
            onClick = onActivationClick,
            modifier = activationButtonModifier,
            shape = actionButtonShape,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(R.color.scratch_ink)),
            border = BorderStroke(1.dp, colorResource(R.color.scratch_ink).copy(alpha = 0.25f)),
        ) {
            ActionContent(stringResource(R.string.home_activation_action), colorResource(R.color.scratch_ink))
        }
    } else {
        val disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        Button(
            onClick = onActivationClick,
            enabled = false,
            modifier = activationButtonModifier,
            shape = actionButtonShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.scratch_ink),
                contentColor = Color.White,
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        ) {
            ActionContent(stringResource(R.string.home_activation_action), disabledContentColor)
        }
    }
    ResetCardButton(onClick = onResetClick)
}

@Composable
private fun ResetCardButton(onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp).testTag("resetCardButton"),
    ) {
        Text(text = stringResource(R.string.home_reset_action), color = colorResource(R.color.scratch_muted_ink))
    }
}
