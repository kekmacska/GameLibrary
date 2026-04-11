package org.kekmacska.gamelibrary.components.shimmer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerMainGridPlaceholder() {

    val shimmer: Shimmer = rememberShimmer(ShimmerBounds.Window)

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(9) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .shimmer(shimmer)
            ) {
                Card(
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(Modifier.fillMaxSize()) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 10f)
                                .shimmer(shimmer)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(18.dp)
                                    .shimmer(shimmer)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .height(14.dp)
                                    .shimmer(shimmer)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShimmerMainListPlaceholder() {

    val shimmer: Shimmer = rememberShimmer(ShimmerBounds.Window)

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(5) {

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .shimmer(shimmer) //wrapping shimmer in shimmer makes it animate
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .size(80.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(vertical = 8.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(18.dp)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .height(14.dp)
                        )
                    }
                }
            }
        }
    }
}