package org.kekmacska.gamelibrary.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Table(modifier: Modifier= Modifier, content: @Composable () -> Unit) {
    Column(modifier=modifier) {
        content()
    }
}

@Composable
fun Tr(content: @Composable RowScope.() -> Unit) {
    Row(Modifier.fillMaxWidth()) {
        content()
    }
}

@Composable
fun RowScope.Cell(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .weight(1f)
            .padding(8.dp)
    ) {
        content()
    }
}