package sk.lukasvyletel.scratchcard.shared.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sk.lukasvyletel.scratchcard.R

@Composable
fun DestinationScreen(
    @StringRes title: Int,
    @StringRes description: Int,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.scratch_cream))
            .padding(24.dp),
    ) {
        TextButton(onClick = onBack) {
            Text(text = stringResource(R.string.navigate_back), color = colorResource(R.color.scratch_ink))
        }
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = stringResource(title),
            color = colorResource(R.color.scratch_ink),
            fontFamily = FontFamily.Serif,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 46.sp,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(description),
            color = colorResource(R.color.scratch_muted_ink),
            fontSize = 16.sp,
        )
    }
}
