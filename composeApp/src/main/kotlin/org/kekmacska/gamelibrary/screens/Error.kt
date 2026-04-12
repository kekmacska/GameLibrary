package org.kekmacska.gamelibrary.screens

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kekmacska.gamelibrary.components.GithubIssues
import org.kekmacska.gamelibrary.providers.ClipboardProvider
import org.kekmacska.gamelibrary.providers.Validators.isValidCustomApiUrl

@Composable
fun ErrorScreen(exception: Throwable?, onRetry: (String?) -> Unit) {
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

        //retry with custom url
        var customApiUrl by remember { mutableStateOf("") }
        var isValidUrl by remember { mutableStateOf(true) }

        OutlinedTextField(
            value = customApiUrl,
            onValueChange = {
                customApiUrl=it
                isValidUrl=isValidCustomApiUrl(it)
            },
            label={Text("Retry with specified custom API endpoint")},
            singleLine = true,
            isError = !isValidUrl,
            modifier = Modifier.fillMaxWidth()
        )

        if(!isValidUrl){
            Text(
                text = "Invalid URL (must be http/https and should be a base URL of the endpoints",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            onClick = {
                if(customApiUrl.isNotEmpty()&&isValidUrl){
                    onRetry(customApiUrl)
                }else{
                    onRetry(null)
                }
            },
            enabled = isValidUrl
        ) {
            Text(text = "Retry", textAlign = TextAlign.Center)
        }
    }
}