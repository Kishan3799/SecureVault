package com.kriahsnverma.securevault.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriahsnverma.securevault.core.crypto.CryptoUtils
import com.kriahsnverma.securevault.core.crypto.MasterKeyHolder
import com.kriahsnverma.securevault.data.repository.PasswordRepository
import com.kriahsnverma.securevault.data.repository.PasswordRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPasswordViewModel @Inject constructor(
    private val passwordRepository: PasswordRepository,
    private val cryptoManager: CryptoUtils,
    saveStateHandle: SavedStateHandle
) : ViewModel() {

    private val passwordId: Int? = saveStateHandle["passwordId"] ?: -1
    val isEditMode = passwordId != -1

    init {
        if(isEditMode){
            loadExistingPassword()
        }
    }

    private fun loadExistingPassword() {
        viewModelScope.launch {
            val currentId = passwordId ?: return@launch
            if (currentId == -1) return@launch

            val masterKey = MasterKeyHolder.get() ?: return@launch
            val entity = passwordRepository.getPasswordById(currentId)

            entity?.let {
                try {
                    val decryptedPassword = cryptoManager.decrypt(it.encryptedPassword, masterKey)
                    title = it.title
                    username = it.username
                    password = decryptedPassword
                    notes = it.notes ?: ""
                    url = it.url ?: ""
                    category = it.category
                }catch (e: Exception){

                }
            }
        }
    }


    var title by mutableStateOf("")
        private set
    var username by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var notes by mutableStateOf("")
        private set
    var url by mutableStateOf("")
        private set
    var category by mutableStateOf("Login")
        private set

    fun onTitleChange(newTitle:String){
        title = newTitle
    }
    fun onUsernameChange(newUsername:String){
        username = newUsername
    }
    fun onPasswordChange(newPassword:String){
        password = newPassword
    }
    fun onNotesChange(newNotes:String){
        notes = newNotes
    }
    fun onUrlChange(newUrl:String){
        url = newUrl
    }
    fun onCategoryChange(newCategory:String){
        category = newCategory
    }




    fun savePassword(
       onSaveComplete: () -> Unit
    ) {
        if(title.isBlank() || username.isBlank() || password.isBlank()){
            return
        }

        viewModelScope.launch {
            if (isEditMode && passwordId != null && passwordId != -1) {
                passwordRepository.updatePassword(
                    id = passwordId,
                    title = title,
                    username = username,
                    plainPassword = password,
                    notes = notes,
                    url = url,
                    category = category
                )
            }  else {
                passwordRepository.addPassword(
                    title = title,
                    username = username,
                    plainPassword = password,
                    notes = notes,
                    url = url,
                    category = category
                )
            }

            onSaveComplete()
        }
    }
}