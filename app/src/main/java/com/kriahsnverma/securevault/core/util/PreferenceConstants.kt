package com.kriahsnverma.securevault.core.util

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferenceConstants {
    // This creates the actual key object that DataStore understands
    val BIOMETRIC_ENABLED_KEY = booleanPreferencesKey("biometric_enabled")
}