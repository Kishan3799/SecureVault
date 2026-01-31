package com.kriahsnverma.securevault.core.util

import com.kriahsnverma.securevault.core.crypto.MasterKeyHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VaultLockManager @Inject constructor() {
    private var unlockedAt: Long = 0L
    private var autoLockMillis: Long = 60_000 // default 1 min

    private val _isUnlockedFlow = MutableStateFlow(false)
    val isUnlockedFlow: StateFlow<Boolean> = _isUnlockedFlow.asStateFlow()

    val isUnlocked: Boolean
        get() = _isUnlockedFlow.value && MasterKeyHolder.isUnlocked()

    fun unlock() {
        _isUnlockedFlow.value = true
        unlockedAt = System.currentTimeMillis()
    }

    fun lock() {
        _isUnlockedFlow.value = false
        unlockedAt = 0L
        MasterKeyHolder.lock()
    }

    fun updateAutoLockMinutes(minutes: Int) {
        autoLockMillis = minutes * 60_000L
    }

    fun shouldAutoLock(): Boolean {
        if (!isUnlocked) return false
        return System.currentTimeMillis() - unlockedAt > autoLockMillis
    }

    fun refreshSession() {
        if (isUnlocked) {
            unlockedAt = System.currentTimeMillis()
        }
    }
}
