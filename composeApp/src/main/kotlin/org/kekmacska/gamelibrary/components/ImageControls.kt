package org.kekmacska.gamelibrary.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ImageControls(
    modifier: Modifier = Modifier,
    scale: Float,
    minZoom: Float,
    maxZoom: Float,
    onScaleChange: (Float) -> Unit,
    onCenter: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = {
                onScaleChange((scale + 0.25f).coerceAtMost(maxZoom))
            }
        ) {
            Icon(Icons.Default.Add, contentDescription = "zoom in")
        }

        IconButton(
            onClick = {
                onCenter()
                onScaleChange(1f)
            }
        ) {
            Icon(Icons.Default.CenterFocusStrong, contentDescription = "center")
        }

        IconButton(
            onClick = {
                onScaleChange((scale - 0.25f).coerceAtLeast(minZoom))
            }
        ) {
            Icon(Icons.Default.Remove, contentDescription = "zoom out")
        }
    }
}