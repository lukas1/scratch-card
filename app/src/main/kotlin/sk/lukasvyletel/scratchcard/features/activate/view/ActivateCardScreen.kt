package sk.lukasvyletel.scratchcard.features.activate.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import sk.lukasvyletel.scratchcard.shared.ui.DestinationAction
import sk.lukasvyletel.scratchcard.shared.ui.DestinationScreen
import sk.lukasvyletel.scratchcard.shared.ui.SectionLabel
import sk.lukasvyletel.scratchcard.shared.ui.ScratchCardCodePanel
import sk.lukasvyletel.scratchcard.shared.ui.ScratchCardFrame
import sk.lukasvyletel.scratchcard.features.activate.view.preview.ActivateCardScreenPreviewContent

@Composable
fun ActivateCardScreen(
    code: String? = null,
    isActivating: Boolean = false,
    showError: Boolean = false,
    onActivateClick: () -> Unit = {},
    onDismissError: () -> Unit = {},
) {
    DestinationScreen(
        title = R.string.activation_screen_title,
        description = R.string.activation_screen_description,
        step = R.string.activation_step,
    ) {
        SectionLabel(R.string.activation_card_label, Modifier.padding(top = 26.dp, bottom = 12.dp))
        ActivationCard(code)
        ActivationInstructions()
        DestinationAction(
            label = if (isActivating) R.string.activation_activating_action else R.string.activation_action,
            tag = "activateCardButton",
            onClick = onActivateClick,
            modifier = Modifier.padding(top = 16.dp),
            enabled = !isActivating,
        )
        Text(
            text = stringResource(R.string.activation_action_hint),
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 20.dp),
            color = colorResource(R.color.scratch_muted_ink),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
        )
    }
    if (showError) {
        ActivationErrorDialog(onDismissError)
    }
}

@Composable
private fun ActivationCard(code: String?) {
    val codeStatus = if (code == null) R.string.activation_code_pending else R.string.activation_code_ready
    ScratchCardFrame(height = 252.dp, tag = "activationCard") {
        ScratchCardCodePanel(
            code = code,
            height = 120.dp,
            backgroundColor = R.color.scratch_lime,
            tag = "activationCode",
            status = codeStatus,
        )
    }
}

@Composable
private fun ActivationInstructions() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .border(1.dp, colorResource(R.color.scratch_ink).copy(alpha = 0.1f), RoundedCornerShape(18.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(40.dp).background(colorResource(R.color.scratch_soft_green), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "✓", color = colorResource(R.color.scratch_ink), fontSize = 19.sp)
        }
        Column(modifier = Modifier.padding(start = 14.dp)) {
            Text(
                text = stringResource(R.string.activation_instruction_title),
                color = colorResource(R.color.scratch_ink),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(R.string.activation_instruction_body),
                color = colorResource(R.color.scratch_muted_ink),
                fontSize = 13.sp,
                lineHeight = 18.sp,
            )
        }
    }
}

@Composable
private fun ActivationErrorDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("activationErrorDialog"),
        shape = RoundedCornerShape(24.dp),
        containerColor = colorResource(R.color.scratch_cream),
        title = {
            Text(
                text = stringResource(R.string.activation_error_title),
                color = colorResource(R.color.scratch_ink),
                fontFamily = FontFamily.Serif,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Text(
                text = stringResource(R.string.activation_error_body),
                color = colorResource(R.color.scratch_muted_ink),
                fontSize = 15.sp,
                lineHeight = 22.sp,
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.testTag("activationErrorDismissButton")) {
                Text(text = stringResource(R.string.activation_error_dismiss))
            }
        },
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F5EF, widthDp = 380)
@ExcludeFromCoverage
@Composable
private fun ActivateCardScreenPendingPreview() {
    ActivateCardScreenPreviewContent()
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F5EF, widthDp = 380)
@ExcludeFromCoverage
@Composable
private fun ActivateCardScreenPendingWhileActivatingPreview() {
    ActivateCardScreenPreviewContent(isActivating = true)
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F5EF, widthDp = 380)
@ExcludeFromCoverage
@Composable
private fun ActivateCardScreenReadyPreview() {
    ActivateCardScreenPreviewContent(code = "SC-4829-7316")
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F5EF, widthDp = 380)
@ExcludeFromCoverage
@Composable
private fun ActivateCardScreenReadyWhileActivatingPreview() {
    ActivateCardScreenPreviewContent(code = "SC-4829-7316", isActivating = true)
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F5EF, widthDp = 380)
@ExcludeFromCoverage
@Composable
private fun ActivateCardScreenPendingWithErrorPreview() {
    ActivateCardScreenPreviewContent(showError = true)
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F5EF, widthDp = 380)
@ExcludeFromCoverage
@Composable
private fun ActivateCardScreenPendingWhileActivatingWithErrorPreview() {
    ActivateCardScreenPreviewContent(isActivating = true, showError = true)
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F5EF, widthDp = 380)
@ExcludeFromCoverage
@Composable
private fun ActivateCardScreenReadyWithErrorPreview() {
    ActivateCardScreenPreviewContent(code = "SC-4829-7316", showError = true)
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F5EF, widthDp = 380)
@ExcludeFromCoverage
@Composable
private fun ActivateCardScreenReadyWhileActivatingWithErrorPreview() {
    ActivateCardScreenPreviewContent(code = "SC-4829-7316", isActivating = true, showError = true)
}

