package sk.lukasvyletel.scratchcard.features.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sk.lukasvyletel.scratchcard.R

@Composable
fun HomeHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(38.dp).background(colorResource(R.color.scratch_lime), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "✳", color = colorResource(R.color.scratch_ink), fontSize = 23.sp, lineHeight = 23.sp)
        }
        Text(
            text = stringResource(R.string.home_brand_name),
            modifier = Modifier.padding(start = 12.dp),
            color = colorResource(R.color.scratch_ink),
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
        )
    }
}
