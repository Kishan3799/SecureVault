package com.kriahsnverma.securevault.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferenceRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val IS_SETUP_COMPLETE = booleanPreferencesKey("is_setup_complete")
    }


    // Flow to observe if the setup process is complete
    val isSetupComplete: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.IS_SETUP_COMPLETE] ?: false
        }

    // Convenience function to read the value once
    suspend fun isSetupComplete(): Boolean {
        return isSetupComplete.first()
    }

    // Function to call AFTER the user successfully creates their master password
    suspend fun setSetupComplete(isComplete: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_SETUP_COMPLETE] = isComplete
        }
    }
}