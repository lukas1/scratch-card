@file:ExcludeFromCoverage

package sk.lukasvyletel.scratchcard.shared.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sk.lukasvyletel.scratchcard.R
import sk.lukasvyletel.scratchcard.core.annotations.ExcludeFromCoverage
import sk.lukasvyletel.scratchcard.shared.ui.ScratchCardCodePanel
import sk.lukasvyletel.scratchcard.shared.ui.ScratchCardFrame

@Preview(showBackground = true, backgroundColor = 0xFFF7F5EF, widthDp = 380)
@Composable
fun ScratchCardPreview() {
    ScratchCardFrame(height = 226.dp, tag = "previewCard") {
        ScratchCardCodePanel(
            code = null,
            height = 108.dp,
            backgroundColor = R.color.scratch_soft_green,
            tag = "previewCode",
        )
    }
}
