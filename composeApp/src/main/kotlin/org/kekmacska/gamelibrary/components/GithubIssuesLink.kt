package org.kekmacska.gamelibrary.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun GithubIssues(issuesUrl: String) {
    val uriHandler = LocalUriHandler.current
    val annotated = buildAnnotatedString {
        append("Please check your internet connection. If you suspect this is a bug or a service outage, please report it with the copied stack trace in ")
        pushStringAnnotation(tag = "issues", annotation = issuesUrl)
        withStyle(
            MaterialTheme.typography.bodySmall.toSpanStyle().copy(
                color = MaterialTheme.colorScheme.primary
            )
        ) {
            append("github issues")
        }
        pop()
        append(". Thank you!")
    }

    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    Text(
        text = annotated,
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val layout = layoutResult ?: return@detectTapGestures
                    val pos = layout.getOffsetForPosition(offset)
                    annotated
                        .getStringAnnotations("issues", pos, pos)
                        .firstOrNull()
                        ?.let { uriHandler.openUri(it.item) }
                }
            },
        onTextLayout = { layoutResult = it }
    )
}