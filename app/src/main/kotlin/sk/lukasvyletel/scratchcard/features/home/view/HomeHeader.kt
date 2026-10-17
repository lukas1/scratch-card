package sk.lukasvyletel.scratchcard.features.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sk.lukasvyletel.scratchcard.R

@Composable
fun HomeHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(38.dp), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(R.drawable.ic_brand_mark),
                contentDescription = null,
                modifier = Modifier.requiredSize(64.dp),
            )
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
