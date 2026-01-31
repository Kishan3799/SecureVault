package com.kriahsnverma.securevault.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriahsnverma.securevault.core.crypto.EncryptionUtils
import com.kriahsnverma.securevault.core.crypto.MasterKeyHolder
import com.kriahsnverma.securevault.data.local.PasswordEntity
import com.kriahsnverma.securevault.data.repository.PasswordRepository
import com.kriahsnverma.securevault.domain.model.PasswordDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordDetailViewModel @Inject constructor(
    private val passwordRepository: PasswordRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PasswordDetailUiState>(PasswordDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val passwordId: Int = checkNotNull(savedStateHandle["passwordId"])

    init {
        loadPassword()
    }

    private fun loadPassword() {
        viewModelScope.launch {
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

            // Decrypt the password using consistent EncryptionUtils
            val decryptedPassword = try {
                EncryptionUtils.decrypt(entity.encryptedPassword, masterKey)
            } catch (e: Exception) {
                _uiState.value = PasswordDetailUiState.Error("Decryption failed. The key may be incorrect.")
                return@launch
            }

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

    fun deletePassword(onDeleteComplete: () -> Unit) {
        viewModelScope.launch {
            passwordRepository.deletePasswordById(passwordId)
            onDeleteComplete()
        }
    }
}
