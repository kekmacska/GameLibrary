package org.kekmacska.gamelibrary.components.shimmer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerCollectiblesButtonPlaceholder(){
    val shimmer= rememberShimmer(ShimmerBounds.Window)

    Card(
        modifier = Modifier.fillMaxWidth().height(48.dp).shimmer(shimmer),
        shape = CardDefaults.shape,
        elevation = CardDefaults.cardElevation(4.dp)
    ){}
}