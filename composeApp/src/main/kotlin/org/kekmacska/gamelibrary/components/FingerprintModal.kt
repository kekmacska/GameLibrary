package org.kekmacska.gamelibrary.components

import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import org.kekmacska.gamelibrary.providers.BiometricAuthenticationProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun Context.findActivity(): FragmentActivity{
    var ctx=this
    while(ctx is ContextWrapper){
        if(ctx is FragmentActivity)return ctx
        ctx=ctx.baseContext
    }
    throw IllegalStateException("Activity not found")
}

@Composable
fun BiometricModal(
    onAuthenticated:()->Unit,
    provider: BiometricAuthenticationProvider= BiometricAuthenticationProvider()
){
    val activity= LocalContext.current.findActivity()
    var failed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        provider.authenticate(activity, onSuccess = {onAuthenticated()}, onFail = {failed=true})
    }

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Authentication required",
                color= MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(16.dp))

            if(failed){
                Text("Authentication failed",
                    color=MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(Modifier.height(16.dp))

                Button(onClick = {
                    provider.authenticate(
                        activity, onSuccess = {onAuthenticated()},
                        onFail = {failed=true}
                    )
                }) {
                    Text("Try again")
                }
            }
        }
    }
}