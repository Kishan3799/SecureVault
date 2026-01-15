package com.kriahsnverma.securevault.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.kriahsnverma.securevault.core.util.VaultLockManager

@Composable
fun VaultGuard(
    vaultLockManager: VaultLockManager,
    onLocked: () -> Unit,
    content: @Composable () -> Unit
) {
    LaunchedEffect(Unit) {
        if(!vaultLockManager.isUnlocked) {
            onLocked()
        }
    }

    content()
    
}