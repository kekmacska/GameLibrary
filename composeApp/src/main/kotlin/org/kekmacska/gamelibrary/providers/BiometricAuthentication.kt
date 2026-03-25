package org.kekmacska.gamelibrary.providers

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BiometricAuthenticationProvider{
    fun authenticate(
        activity: FragmentActivity,
        onSuccess: ()-> Unit,
        onFail:()->Unit
    ){
        val executor= ContextCompat.getMainExecutor(activity)
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Fingerprint verification")
            .setSubtitle("To log into your session, you must authenticate with fingerprint")
            .setNegativeButtonText("Cancel and leave")
            .build()

        val biometricPrompt= BiometricPrompt(activity,executor,
            object : BiometricPrompt.AuthenticationCallback(){
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ){
                    onSuccess()
                }
                override fun onAuthenticationFailed(){
                    onFail()
                }
            }
        )
        biometricPrompt.authenticate(promptInfo)
    }
}