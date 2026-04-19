package org.kekmacska.gamelibrary.providers

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BiometricAuthenticationProvider {

    fun authenticate(
        activity: ComponentActivity,
        onSuccess: () -> Unit,
        onFail: () -> Unit,
        onCancel: () -> Unit
    ) {

        Log.d("BIOMETRIC_DEBUG", "authenticate() called")

        if (isEmulator()) {
            Log.d("BIOMETRIC_DEBUG", "EMULATOR PATH")

            AlertDialog.Builder(activity)
                .setTitle("Biometric Authentication")
                .setMessage("Simulated biometric prompt (emulator)")
                .setPositiveButton("Success") { _, _ -> onSuccess() }
                .setNegativeButton("Cancel") { _, _ -> onCancel() }
                .show()

            return
        }

        val fragmentActivity = activity as? FragmentActivity
        if (fragmentActivity == null) {
            Log.e("BIOMETRIC_DEBUG", "NOT A FragmentActivity, cannot run biometric prompt")
            onCancel()
            return
        }

        val executor = ContextCompat.getMainExecutor(activity)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Fingerprint verification")
            .setSubtitle("Authenticate to continue")
            .setNegativeButtonText("Cancel")
            .build()

        val biometricPrompt = BiometricPrompt(
            fragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    Log.d("BIOMETRIC_DEBUG", "SUCCESS")
                    onSuccess()
                }

                override fun onAuthenticationFailed() {
                    Log.d("BIOMETRIC_DEBUG", "FAILED")
                    onFail()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Log.d("BIOMETRIC_DEBUG", "ERROR: $errorCode")
                    onCancel()
                }
            }
        )

        biometricPrompt.authenticate(promptInfo)
    }
}