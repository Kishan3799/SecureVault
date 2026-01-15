package com.kriahsnverma.securevault.domain.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme

import androidx.compose.ui.graphics.Color


enum class PasswordStrength(
    val displayText: String,
    val color: Color,
    val progress: Float
) {
    TOO_SHORT("Too Short", Color.Red, 0.1f),
    WEAK("Weak", Color.Red, 0.25f),
    MEDIUM("Medium", Color.Yellow, 0.5f),
    STRONG("Strong", Color.Green, 0.75f),
}