package com.kriahsnverma.securevault.domain.usecase.password

object PasswordGenerator {
    private const val UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val LOWER = "abcdefghijklmnopqrstuvwxyz"
    private const val NUMBER = "0123456789"
    private const val SYMBOL = "!@#$%^&*()_+-=[]{}|;:,.<>?"

    fun generate(
        length: Int,
        upper:Boolean,
        lower:Boolean,
        number:Boolean,
        symbol:Boolean
    ): String {
        var pool = ""
        if(upper) pool += UPPER
        if(lower) pool += LOWER
        if(number) pool += NUMBER
        if(symbol) pool += SYMBOL

        if (pool.isEmpty()) {
            return ""
        }

        return (1..length)
            .map {pool.random()}
            .joinToString("")
    }
}