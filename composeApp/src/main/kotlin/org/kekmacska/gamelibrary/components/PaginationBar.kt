package org.kekmacska.gamelibrary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PaginationBar(
    currentPage: Int,
    totalPages: Int,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(bottom = 36.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrev, enabled = currentPage > 1) {
            Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Previous page")
        }
        PageIndicator(currentPage, totalPages)
        IconButton(onClick = onNext, enabled = currentPage < totalPages) {
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = "Next page")
        }
    }
}

@Composable
fun PageIndicator(currentPage: Int, totalPages: Int) {
    Text(
        text = "$currentPage / $totalPages",
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(4.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}