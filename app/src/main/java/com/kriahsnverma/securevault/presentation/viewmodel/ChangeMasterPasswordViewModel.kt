package com.kriahsnverma.securevault.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriahsnverma.securevault.core.crypto.EncryptionUtils
import com.kriahsnverma.securevault.core.crypto.MasterKeyHolder
import com.kriahsnverma.securevault.data.repository.MasterPasswordRepository
import com.kriahsnverma.securevault.data.repository.PasswordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChangeMasterPasswordViewModel @Inject constructor(
    private val masterRepository: MasterPasswordRepository,
    private val passwordRepository: PasswordRepository,
    private val cryptoManager: EncryptionUtils,
): ViewModel() {
    var oldPassword by mutableStateOf("")
    var newPassword by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun changePassword(onSuccess: () -> Unit) {
        error = null
        if (newPassword != confirmPassword) {
            error = "New Passwords do not match"
            return
        }
        if (newPassword.length < 8) {
            error = "Password must be at least 8 characters"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                // 1. Validate the old password
                val isValid = masterRepository.validateMasterPassword(oldPassword)
                if (!isValid) {
                    withContext(Dispatchers.Main) { error = "Current password is incorrect" }
                    return@launch
                }

                // 2. Generate the OLD key (needed to decrypt current data)
                val oldKey = masterRepository.generateMasterKey(oldPassword)

                // 3. To generate the NEW key, we first need to save the new password
                // because EncryptionUtils/MasterPasswordRepository derives the key
                // based on the SALT stored in the BCrypt hash.

                // Step A: Hash and save new master password
                masterRepository.saveNewMasterPassword(newPassword)

                // Step B: Generate the NEW key from the new hash/salt
                val newKey = masterRepository.generateMasterKey(newPassword)

                // 4. Migrate all data to the new key
                passwordRepository.reEncryptAllData(oldKey, newKey)

                // 5. Update the Session Key in memory so the user isn't logged out
                MasterKeyHolder.setKey(newKey)

                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    error = e.localizedMessage ?: "An unexpected error occurred"
                }
            } finally {
                isLoading = false
            }
        }
    }
}