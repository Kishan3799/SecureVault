package com.kriahsnverma.securevault.domain.model

import com.kriahsnverma.securevault.data.local.PasswordEntity


data class VaultUiState(
    val selectedCategory: String = "All",
    val searchQuery: String = "",
    val passwords: List<PasswordEntity> = emptyList()
)