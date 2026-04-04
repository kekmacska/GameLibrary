package org.kekmacska.gamelibrary.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.kekmacska.gamelibrary.viewModels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(viewModel: MainViewModel) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchAttribute by viewModel.searchAttribute.collectAsState()
    val attributes = listOf("name", "releaseYear", "genre")

    var menuWidth by remember { mutableIntStateOf(0) }
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 0.dp)
    ) {

        TextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text("Search") },
            trailingIcon = {
                if(searchQuery.isNotEmpty()){
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        modifier = Modifier.clickable{viewModel.setSearchQuery("")}.size(16.dp)
                    )
                }
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        Spacer(Modifier.width(8.dp))

        Box(
            modifier = Modifier.width(120.dp)
        ) {

            TextField(
                value = "",
                onValueChange = {},
                readOnly = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    disabledIndicatorColor = Color.Transparent,
                    disabledContainerColor = TextFieldDefaults.colors().disabledContainerColor
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = searchAttribute,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(if (expanded) 180f else 0f)
                )
            }

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { expanded = !expanded }
                    .onGloballyPositioned { coordinates ->
                        menuWidth = coordinates.size.width
                    }
            )

            val density = LocalDensity.current
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(with(density) { menuWidth.toDp() })
            ) {
                attributes.forEach { attr ->
                    DropdownMenuItem(
                        text = { Text(attr) },
                        onClick = {
                            viewModel.setSearchAttribute(attr)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
