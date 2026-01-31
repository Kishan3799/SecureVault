package com.kriahsnverma.securevault.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriahsnverma.securevault.core.crypto.MasterKeyHolder
import com.kriahsnverma.securevault.core.util.VaultLockManager
import com.kriahsnverma.securevault.data.usecase.UnlockVaultUseCase
import com.kriahsnverma.securevault.presentation.util.BiometricAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UnlockUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class UnlockViewModel @Inject constructor(
    private val unlockVaultUseCase: UnlockVaultUseCase,
    private val vaultLockManager: VaultLockManager,
    private val biometricAuthManager: BiometricAuthManager,
    ): ViewModel() {
    // State for the text field
    var password by mutableStateOf("")
        private set

    // A single state object for the rest of the UI
    var uiState by mutableStateOf(UnlockUiState())
        private set

    // A Channel for one-time navigation events
    private val _unlockedEvent = Channel<Unit>()
    val unlockedEvent = _unlockedEvent.receiveAsFlow()

    fun onPasswordChange(newPass: String) {
        password = newPass
    }

    fun onUnlockClicked() {
        if (password.isBlank()) {
            uiState = uiState.copy(error = "Password cannot be empty")
            return
        }

        // Set loading state and clear previous error
        uiState = uiState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            // The use case now handles both validation and key generation
            val masterKey = unlockVaultUseCase(password)

            if (masterKey != null) {
                // Success! The use case returned a key.
                MasterKeyHolder.setKey(masterKey) // This "unlocks" the vault
                vaultLockManager.unlock()
                _unlockedEvent.send(Unit) // Send navigation event

            } else {
                // Failure! The use case returned null, meaning the password was wrong.
                uiState = uiState.copy(error = "Incorrect password")
            }

            // Always turn off loading state at the end
            uiState = uiState.copy(isLoading = false)
        }
    }

    fun onBiometricUnlockClicked(activity: FragmentActivity) {
        biometricAuthManager.showBiometricPrompt(
            activity = activity,
            onSuccess = {
                // 1. Restore the key from cache so it's available for decryption
                val restored = MasterKeyHolder.restoreFromBiometric()
                
                if (restored) {
                    // 2. Update the vault state
                    vaultLockManager.unlock()

                    // 3. Trigger navigation
                    viewModelScope.launch {
                        _unlockedEvent.send(Unit)
                    }
                } else {
                    // This happens if the app process was killed and cache is gone.
                    // The user must use their password.
                    uiState = uiState.copy(error = "Biometric session expired. Please use Master Password.")
                }
            },
            onError = { errorMessage ->
                // Update UI with the error message
                uiState = uiState.copy(error = errorMessage)
            }
        )
    }

}