package org.kekmacska.gamelibrary.components

import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import org.kekmacska.gamelibrary.providers.BiometricAuthenticationProvider
import org.kekmacska.gamelibrary.providers.findActivity
import org.kekmacska.gamelibrary.providers.isEmulator

@Composable
fun BiometricEffect(
    biometricAction: Boolean,
    onConsumed: () -> Unit
) {
    val context = LocalContext.current
    val activity = context.findActivity()

    Log.d("BIOMETRIC_DEBUG", "recomposed biometricAction=$biometricAction")

    if (activity == null) {
        Log.d("BIOMETRIC_DEBUG", "Activity NULL → abort")
        return
    }

    LaunchedEffect(biometricAction) {

        Log.d("BIOMETRIC_DEBUG", "LaunchedEffect biometricAction=$biometricAction")

        if (!biometricAction) return@LaunchedEffect

        if (isEmulator()) {
            Log.d("BIOMETRIC_DEBUG", "EMULATOR SHORTCUT")

            Toast.makeText(
                context,
                "Emulator → auto success",
                Toast.LENGTH_SHORT
            ).show()

            onConsumed()
            return@LaunchedEffect
        }

        val canAuth = BiometricManager.from(activity)
            .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)

        Log.d("BIOMETRIC_DEBUG", "canAuthenticate=$canAuth")

        if (canAuth != BiometricManager.BIOMETRIC_SUCCESS) {
            Log.d("BIOMETRIC_DEBUG", "NO BIOMETRIC")

            Toast.makeText(
                context,
                "No biometric available",
                Toast.LENGTH_SHORT
            ).show()

            onConsumed()
            return@LaunchedEffect
        }

        Log.d("BIOMETRIC_DEBUG", "CALLING PROVIDER")

        BiometricAuthenticationProvider().authenticate(
            activity,
            onSuccess = {
                Log.d("BIOMETRIC_DEBUG", "SUCCESS CALLBACK")
                onConsumed()
            },
            onFail = {
                Log.d("BIOMETRIC_DEBUG", "FAIL CALLBACK")
                onConsumed()
            },
            onCancel = {
                Log.d("BIOMETRIC_DEBUG", "CANCEL CALLBACK")
                onConsumed()
            }
        )
    }
}