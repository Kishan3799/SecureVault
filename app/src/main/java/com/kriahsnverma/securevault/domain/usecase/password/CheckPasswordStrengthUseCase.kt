package com.kriahsnverma.securevault.domain.usecase.password

import com.kriahsnverma.securevault.domain.model.PasswordStrength
import javax.inject.Inject

class CheckPasswordStrengthUseCase @Inject constructor() {

    operator fun invoke(password:String) : PasswordStrength {
        if(password.length < 8) {
            return PasswordStrength.TOO_SHORT
        }

        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        val score = listOf(hasLowerCase, hasUpperCase, hasDigit, hasSpecialChar).count {it}

        return when(score) {
            1,2 -> PasswordStrength.WEAK
            3 -> PasswordStrength.MEDIUM
            4 -> PasswordStrength.STRONG
            else -> PasswordStrength.TOO_SHORT
        }
    }
}