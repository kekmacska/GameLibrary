package org.kekmacska.gamelibrary.providers

import android.os.Build
import android.util.Log

fun isEmulator(): Boolean {
    val result = (Build.FINGERPRINT.startsWith("generic")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || Build.HARDWARE.contains("goldfish")
            || Build.HARDWARE.contains("ranchu"))

    Log.d("BIOMETRIC_DEBUG", "isEmulator = $result | MODEL=${Build.MODEL} | FINGERPRINT=${Build.FINGERPRINT}")

    return result
}