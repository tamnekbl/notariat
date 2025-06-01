package ui.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import utils.Margin

@Composable
fun PageController(
    modifier: Modifier = Modifier,
    canNext: Boolean = true,
    canPrev: Boolean = true,
    onDelta: (Int) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Margin.spacing)
    ) {
        IconButton(
            enabled = canPrev,
            onClick = { onDelta(-1) }
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, null)
        }

        IconButton(
            enabled = canNext,
            onClick = { onDelta(1) }
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowRight, null)
        }
    }
}