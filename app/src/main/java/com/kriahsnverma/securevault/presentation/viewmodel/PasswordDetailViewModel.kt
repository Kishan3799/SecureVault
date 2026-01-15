package com.kriahsnverma.securevault.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriahsnverma.securevault.core.crypto.CryptoUtils
import com.kriahsnverma.securevault.core.crypto.MasterKeyHolder
import com.kriahsnverma.securevault.data.local.PasswordEntity
import com.kriahsnverma.securevault.data.repository.PasswordRepository
import com.kriahsnverma.securevault.domain.model.PasswordDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.crypto.SecretKey
import javax.inject.Inject

@HiltViewModel
class PasswordDetailViewModel @Inject constructor(
    private val passwordRepository: PasswordRepository,
    private val cryptoManager: CryptoUtils,
    savedStateHandle: SavedStateHandle, // 3. Inject SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<PasswordDetailUiState>(PasswordDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    // Get the passwordId from navigation arguments
    private val passwordId: Int = checkNotNull(savedStateHandle["passwordId"])

    init {
        loadPassword()
    }

    // 4. The ViewModel is now self-sufficient and doesn't need parameters
    private fun loadPassword() {
        viewModelScope.launch {
            // Get the master key from the secure holder
            val masterKey = MasterKeyHolder.get()
            if (masterKey == null) {
                _uiState.value = PasswordDetailUiState.Error("Session expired. Please unlock the vault again.")
                return@launch
            }

            val entity: PasswordEntity? = passwordRepository.getPasswordById(passwordId)
            if (entity == null) {
                _uiState.value = PasswordDetailUiState.Error("Password not found.")
                return@launch
            }

            // Decrypt the password
            val decryptedPassword = try {
                cryptoManager.decrypt(entity.encryptedPassword, masterKey)
            } catch (e: Exception) {
                _uiState.value = PasswordDetailUiState.Error("Decryption failed. The key may be incorrect.")
                return@launch
            }

            // Update the state to Success
            _uiState.value = PasswordDetailUiState.Success(
                id = entity.id,
                title = entity.title,
                username = entity.username,
                decryptedPassword = decryptedPassword,
                notes = entity.notes,
                url = entity.url
            )
        }
    }

    // 5. Delete function remains the same, but you could add a callback for navigation
    fun deletePassword(onDeleteComplete: () -> Unit) {
        viewModelScope.launch {
            passwordRepository.deletePasswordById(passwordId)
            onDeleteComplete()
        }
    }
}