package com.kriahsnverma.securevault.core.util

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import javax.inject.Inject
import javax.inject.Singleton

class AppLifecycleObserver @Inject constructor(
    private val vaultLockManager: VaultLockManager
) : DefaultLifecycleObserver {

    override fun onStop(owner: LifecycleOwner) {
        // App went to background
        vaultLockManager.lock()
    }
}