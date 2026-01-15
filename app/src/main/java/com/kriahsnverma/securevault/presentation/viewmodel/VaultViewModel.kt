package com.kriahsnverma.securevault.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kriahsnverma.securevault.data.repository.PasswordRepository // 1. Depend on the interface
import com.kriahsnverma.securevault.domain.model.VaultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.contains

@HiltViewModel
class VaultViewModel @Inject constructor(
    private val repository: PasswordRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow("All")

    private val _allPasswords = repository.getPasswords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState: StateFlow<VaultUiState> = combine(
        _searchQuery,
        _selectedCategory,
        _allPasswords
    ) { query, category, passwords ->
        val filteredPasswords = if (query.isBlank() && category == "All") {
            passwords // Optimization: return the original list if no filters are active
        } else {
            passwords.filter { password ->
                val categoryMatch = category == "All" || password.category.equals(category, ignoreCase = true)
                val searchMatch = query.isBlank() ||
                        password.title.contains(query, ignoreCase = true) ||
                        password.username.contains(query, ignoreCase = true) ||
                        password.url?.contains(query, ignoreCase = true) == true
                categoryMatch && searchMatch
            }
        }

        VaultUiState(
            searchQuery = query,
            selectedCategory = category,
            passwords = filteredPasswords
        )
    }.stateIn( // 2. Convert the combined flow into a StateFlow
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = VaultUiState() // Provide an initial state
    )
    // 5. Expose events for the UI to call
    fun onSearchChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onCategorySelect(category: String) {
        _selectedCategory.value = category
    }


}