package com.kriahsnverma.securevault.presentation.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VaultSettingViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val BIOMETRIC_KEY = booleanPreferencesKey("biometric_enable")
    private val THEME_KEY = stringPreferencesKey("app_theme")
    private val AUTO_LOCK_MINUTES = intPreferencesKey("auto_lock_minutes")

    val appTheme = dataStore.data.map {
        it[THEME_KEY] ?: "System"
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "System")

    val biometricEnabled = dataStore.data.map { it[BIOMETRIC_KEY] ?: false }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val autoLockMinutes = dataStore.data.map { it[AUTO_LOCK_MINUTES] ?: 1 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    fun setTheme(theme: String) {
        viewModelScope.launch {
            dataStore.edit { it[THEME_KEY] = theme }
        }
    }

    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit {
                it[BIOMETRIC_KEY] = enabled
            }
        }
    }

    fun setAutoLockMinutes(minutes: Int) {
        viewModelScope.launch {
            dataStore.edit {
                it[AUTO_LOCK_MINUTES] = minutes
            }
        }
    }
}
