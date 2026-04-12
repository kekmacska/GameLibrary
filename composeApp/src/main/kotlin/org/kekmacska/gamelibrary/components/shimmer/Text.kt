package org.kekmacska.gamelibrary.components.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerText(
    text: String?,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    width: Dp? = null,
    height: Dp? = null,
) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    val density = LocalDensity.current

    if (text != null) {
        Text(text = text, style = style, modifier = modifier)
    } else {
        val heightDp = height ?: with(density) { style.lineHeight.toDp() }
        val widthModifier = width?.let { Modifier.width(it) } ?: Modifier.fillMaxWidth()

        Box(
            modifier = modifier
                .then(widthModifier)
                .height(heightDp)
                .shimmer(shimmer)
                .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
        )
    }
}