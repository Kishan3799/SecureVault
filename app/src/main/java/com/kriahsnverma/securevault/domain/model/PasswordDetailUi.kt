package com.kriahsnverma.securevault.domain.model

sealed interface PasswordDetailUiState {
    data object Loading : PasswordDetailUiState
    data class Success(
        val id: Int,
        val title: String,
        val username: String,
        val decryptedPassword: String,
        val notes: String?,
        val url: String?
    ) : PasswordDetailUiState
    data class Error(val message: String) : PasswordDetailUiState
}