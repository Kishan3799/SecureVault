package com.kriahsnverma.securevault.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriahsnverma.securevault.data.local.PasswordEntity
import com.kriahsnverma.securevault.data.repository.PasswordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PasswordRepository
): ViewModel() {
    val passwords = repository.getPasswords().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

//    fun addSample(){
//        viewModelScope.launch {
//            repository.addPassword(
//                PasswordEntity(title = "Gmail", username = "me@gmail.com", password = "encrypted_pass")
//            )
//        }
//    }
}