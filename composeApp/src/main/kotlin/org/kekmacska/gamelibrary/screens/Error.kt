package org.kekmacska.gamelibrary.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kekmacska.gamelibrary.providers.ClipboardProvider

@Composable
fun ErrorScreen(exception: Throwable?, onRetry: () -> Unit) {
    val trace = exception?.stackTraceToString() ?: "Unknown error"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = "Error",
            Modifier
                .size(80.dp)
                .padding(bottom = 16.dp), tint = MaterialTheme.colorScheme.error
        )
        Text(
            text = "Something went sideways",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Stack trace",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(10.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
                .padding(12.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Text(
                text = trace,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp
            )
        }

        val context = LocalContext.current
        IconButton(onClick = {
            ClipboardProvider.copy(context, trace)
        }) {
            Icon(Icons.Default.ContentCopy, "Copy")
        }

        GithubIssues(issuesUrl = "https://github.com/kekmacska/GameLibrary/issues")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRetry) {
            Text(text = "Retry", textAlign = TextAlign.Center)
        }
    }
}


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