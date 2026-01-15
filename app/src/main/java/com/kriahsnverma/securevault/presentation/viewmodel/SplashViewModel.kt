package com.kriahsnverma.securevault.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriahsnverma.securevault.data.repository.UserPreferenceRepository
import com.kriahsnverma.securevault.data.usecase.HasMasterPasswordUseCase
import com.kriahsnverma.securevault.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val hasMasterPasswordUseCase: HasMasterPasswordUseCase,
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            // A small delay can prevent a jarring flash of the splash screen on fast devices.
            kotlinx.coroutines.delay(500)
            if (hasMasterPasswordUseCase()) {
                // Password exists -> go to Unlock screen
                _startDestination.value = Screen.UnlockScreen.route
            } else {
                // No password -> go to Onboarding/Setup flow
                _startDestination.value = Screen.OnboardingScreen.route
            }
        }
    }
}