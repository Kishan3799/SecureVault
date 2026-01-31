package com.kriahsnverma.securevault.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.kriahsnverma.securevault.core.util.VaultLockManager

@Composable
fun VaultGuard(
    vaultLockManager: VaultLockManager,
    onLocked: () -> Unit,
    content: @Composable () -> Unit
) {
    val isUnlocked by vaultLockManager.isUnlockedFlow.collectAsState()

    LaunchedEffect(isUnlocked) {
        if (!isUnlocked) {
            onLocked()
        }
    }

    if (isUnlocked) {
        content()
    }
}
