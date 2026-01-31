package com.kriahsnverma.securevault.presentation.util

import android.content.Context
import androidx.biometric.BiometricManager

fun isBiometricAvailable(context: Context): Boolean {
    val manager = BiometricManager.from(context)
    return manager.canAuthenticate(
        BiometricManager.Authenticators.BIOMETRIC_STRONG
    ) == BiometricManager.BIOMETRIC_SUCCESS
}