package sk.lukasvyletel.scratchcard.shared.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import sk.lukasvyletel.scratchcard.core.dependency_injection.LocalInjector

@Composable
fun DestinationScreen(
    @StringRes title: Int,
    @StringRes description: Int,
    @StringRes step: Int,
    content: @Composable ColumnScope.() -> Unit,
) {
    val navigation = LocalInjector.current.globalModule.navigation
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.scratch_cream))
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(
                modifier = Modifier.testTag("destinationBackButton"),
                onClick = { navigation.goBack() },
            ) {
                Text(text = stringResource(R.string.navigate_back), color = colorResource(R.color.scratch_ink))
            }
            Text(
                text = stringResource(step),
                modifier = Modifier
                    .border(1.dp, colorResource(R.color.scratch_ink).copy(alpha = 0.16f), CircleShape)
                    .padding(horizontal = 13.dp, vertical = 7.dp)
                    .testTag("destinationStepBubble"),
                color = colorResource(R.color.scratch_muted_ink),
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
        ) {
            Text(
                modifier = Modifier.padding(top = 25.dp).testTag("destinationTitle"),
                text = stringResource(title),
                color = colorResource(R.color.scratch_ink),
                fontFamily = FontFamily.Serif,
                fontSize = 43.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 46.sp,
            )
            Text(
                modifier = Modifier.padding(top = 12.dp).testTag("destinationDescription"),
                text = stringResource(description),
                color = colorResource(R.color.scratch_muted_ink),
                fontSize = 15.sp,
                lineHeight = 22.sp,
            )
            content()
        }
    }
}

@Composable
fun SectionLabel(@StringRes text: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(text),
        modifier = modifier,
        color = colorResource(R.color.scratch_muted_ink),
        fontFamily = FontFamily.Monospace,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.6.sp,
    )
}

@Composable
fun DestinationAction(
    @StringRes label: Int,
    tag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().height(62.dp).testTag(tag),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.scratch_ink),
            contentColor = Color.White,
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(label), fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(text = "↗", fontSize = 24.sp, lineHeight = 24.sp)
        }
    }
}
