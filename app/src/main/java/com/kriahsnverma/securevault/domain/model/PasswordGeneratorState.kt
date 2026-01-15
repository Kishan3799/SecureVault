package com.kriahsnverma.securevault.domain.model

data class PasswordGeneratorState(
    val length: Int = 8,
    val includeUppercase:Boolean = false,
    val includeLowercase: Boolean = false,
    val includeNumber: Boolean = true,
    val includeSymbols: Boolean = false,
    val generatedPassword: String = ""
)
