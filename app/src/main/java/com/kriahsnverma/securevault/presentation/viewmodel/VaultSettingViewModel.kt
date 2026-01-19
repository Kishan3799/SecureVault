package com.kriahsnverma.securevault.presentation.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
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

    private val THEME_KEY = stringPreferencesKey("app_theme")

    val appTheme = dataStore.data.map {
        it[THEME_KEY] ?: "System"
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "System")

    fun setTheme(theme:String){
        viewModelScope.launch {
            dataStore.edit { it[THEME_KEY] = theme }
        }
    }
}