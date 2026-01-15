package com.kriahsnverma.securevault.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kriahsnverma.securevault.domain.model.PasswordGeneratorState
import com.kriahsnverma.securevault.domain.usecase.password.PasswordGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PasswordGeneratorViewModel @Inject constructor(): ViewModel() {
    private val _state = mutableStateOf(PasswordGeneratorState())
    val state: State<PasswordGeneratorState> = _state

    init {
        generatePassword()
    }

    fun onLengthChange(value:Int) {
        _state.value = _state.value.copy(length = value)
        generatePassword()
    }

    fun onUppercaseToggle(value:Boolean){
        _state.value = _state.value.copy(includeUppercase = value)
        generatePassword()
    }

    fun onLowercaseToggle(value:Boolean) {
        _state.value = _state.value.copy(includeLowercase = value)
        generatePassword()
    }

    fun onNumberToggle(value:Boolean) {
        _state.value = _state.value.copy(includeNumber = value)
        generatePassword()
    }

    fun onSymbolToggle(value: Boolean){
        _state.value = _state.value.copy(includeSymbols = value)
        generatePassword()
    }

    private fun generatePassword(){
        _state.value = _state.value.copy(
            generatedPassword = PasswordGenerator.generate(
                length = _state.value.length,
                upper = _state.value.includeUppercase,
                lower = _state.value.includeLowercase,
                number = _state.value.includeNumber,
                symbol = _state.value.includeSymbols
            )
        )
    }
}