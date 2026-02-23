package com.kriahsnverma.securevault.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kriahsnverma.securevault.data.local.PasswordEntity
import com.kriahsnverma.securevault.data.repository.PasswordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStreamWriter
import javax.inject.Inject

@HiltViewModel
class VaultSettingViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val passwordRepository: PasswordRepository
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

    fun exportVault(context: Context, uri: Uri, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val passwords = passwordRepository.getAllPasswordsSync()
                val json = Gson().toJson(passwords)
                
                withContext(Dispatchers.IO) {
                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        OutputStreamWriter(outputStream).use { writer ->
                            writer.write(json)
                        }
                    }
                }
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Export failed")
            }
        }
    }

    fun importVault(context: Context, uri: Uri, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val json = withContext(Dispatchers.IO) {
                    context.contentResolver.openInputStream(uri)?.bufferedReader().use { it?.readText() }
                }
                
                if (json != null) {
                    val type = object : TypeToken<List<PasswordEntity>>() {}.type
                    val passwords: List<PasswordEntity> = Gson().fromJson(json, type)
                    passwordRepository.importPasswords(passwords)
                    onSuccess()
                } else {
                    onError("Failed to read file")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Import failed")
            }
        }
    }
}
