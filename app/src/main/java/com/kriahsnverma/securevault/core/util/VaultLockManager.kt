package com.kriahsnverma.securevault.core.util

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VaultLockManager @Inject constructor() {
    private var unlockedAt: Long = 0L
    private var autoLockMillis: Long = 60_000 //default 1 min

    var isUnlocked: Boolean = false
        private set

    fun unlock() {
        isUnlocked = true
        unlockedAt = System.currentTimeMillis()
    }

    fun lock() {
        isUnlocked = false
        unlockedAt = 0L
    }

    fun updateAutoLock(minutes:Int) {
        autoLockMillis = minutes * 60_000L
    }

    fun shouldAutoLock() : Boolean {
        if(!isUnlocked) return false
        return System.currentTimeMillis() - unlockedAt > autoLockMillis
    }

    fun refreshSession() {
        if(isUnlocked) {
            unlockedAt = System.currentTimeMillis()
        }
    }


}