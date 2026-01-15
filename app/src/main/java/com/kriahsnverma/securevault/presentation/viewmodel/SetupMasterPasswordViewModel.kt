package com.kriahsnverma.securevault.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriahsnverma.securevault.data.repository.UserPreferenceRepository
import com.kriahsnverma.securevault.data.usecase.SetupMasterPasswordUseCase
import com.kriahsnverma.securevault.domain.model.PasswordStrength
import com.kriahsnverma.securevault.domain.usecase.password.CheckPasswordStrengthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch



// --- Best Practice 1: Define a single UI State data class ---
data class SetupPasswordUiState(
    val passwordStrength: PasswordStrength = PasswordStrength.TOO_SHORT,
    val error: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class SetupMasterPasswordViewModel @Inject constructor(
    private val setupMasterPasswordUseCase: SetupMasterPasswordUseCase,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val checkPasswordStrengthUseCase: CheckPasswordStrengthUseCase
) : ViewModel() {

    // --- State for the text fields remains separate, which is fine ---
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set

    // --- Best Practice 2: Use a single state object for the rest of the UI ---
    var uiState by mutableStateOf(SetupPasswordUiState())
        private set

    // --- Best Practice 3: Use a Channel for one-time navigation events ---
    private val _setupCompleteEvent = Channel<Unit>()
    val setupCompleteEvent = _setupCompleteEvent.receiveAsFlow()


    fun onPasswordChange(newPass: String) {
        password = newPass
        // Update the state object immutably
        uiState = uiState.copy(passwordStrength = checkPasswordStrengthUseCase(newPass))
    }

    fun onConfirmPasswordChange(newConfirmPass: String) {
        confirmPassword = newConfirmPass
    }

    fun saveMasterPasswordAndCompleteSetup() {
        // Validation checks
        if (password.length < 8) { // Consider a more secure minimum length
            uiState = uiState.copy(error = "Password must be at least 8 characters long")
            return
        }
        if (password != confirmPassword) {
            uiState = uiState.copy(error = "Passwords do not match")
            return
        }
        if (uiState.passwordStrength == PasswordStrength.TOO_SHORT || uiState.passwordStrength == PasswordStrength.WEAK) {
            uiState = uiState.copy(error = "Password is too weak. Please choose a stronger one.")
            return
        }

        // Set loading state and clear previous errors
        uiState = uiState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                // Perform the async operations
                setupMasterPasswordUseCase(password)
                userPreferenceRepository.setSetupComplete(true)

                // --- Best Practice 4: Send a signal for navigation ---
                _setupCompleteEvent.send(Unit)

            } catch (e: Exception) {
                // Handle potential errors from the use case or repository
                uiState = uiState.copy(error = "An unexpected error occurred.")
            } finally {
                // Always turn off loading state
                uiState = uiState.copy(isLoading = false)
            }
        }
    }
}