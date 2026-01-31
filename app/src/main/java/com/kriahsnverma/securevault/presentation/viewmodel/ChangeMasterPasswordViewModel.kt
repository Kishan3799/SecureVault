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
import java.security.SecureRandom
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
                // STEP 1: Generate the OLD KEY while the old hash/salt is still in the DB
                val oldKey = masterRepository.generateMasterKey(oldPassword)

                // STEP 2: Save the NEW password (this generates a NEW salt in the DB)
                masterRepository.saveNewMasterPassword(newPassword)

                // STEP 3: Generate the NEW KEY using the new salt now in the DB
                val newKey = masterRepository.generateMasterKey(newPassword)

                // STEP 4: Now re-encrypt
                passwordRepository.reEncryptAllData(oldKey, newKey)

                // STEP 5: Update session
                MasterKeyHolder.setKey(newKey)

                withContext(Dispatchers.Main) { onSuccess() }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { error = "Decryption Failed: Check current password" }
            } finally {
                isLoading = false
            }
        }
    }
}