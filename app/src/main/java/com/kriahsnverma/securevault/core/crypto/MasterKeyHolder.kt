package com.kriahsnverma.securevault.core.crypto

import javax.crypto.SecretKey

object MasterKeyHolder {
    private var secretKey: SecretKey? = null

    // We keep a copy of the key that survives "auto-lock"
    // but is only restored after a successful biometric authentication.
    private var biometricKeyCache: SecretKey? = null

    fun isUnlocked(): Boolean {
        return secretKey != null
    }

    /**
     * Called when the vault is locked (e.g. by inactivity).
     * We clear the active secretKey so decryption fails, but we keep
     * the biometricKeyCache so biometrics can restore it.
     */
    fun lock(){
        secretKey = null
    }

    /**
     * Completely wipes everything. Called on logout or when
     * biometrics should no longer be able to unlock.
     */
    fun fullLock() {
        secretKey = null
        biometricKeyCache = null
    }

    fun get(): SecretKey? {
        return this.secretKey
    }

    fun setKey(key: SecretKey) {
        secretKey = key
        biometricKeyCache = key
    }

    /**
     * Restores the active key from the cache.
     * This should ONLY be called after a successful biometric prompt.
     */
    fun restoreFromBiometric(): Boolean {
        return if (biometricKeyCache != null) {
            secretKey = biometricKeyCache
            true
        } else {
            false
        }
    }

    fun getKey(): SecretKey {
        return secretKey ?:  throw IllegalStateException("Vault is Locked. Use MasterKeyHolder.get() for a safe call.")
    }

    fun clear() {
        this.secretKey = null
        this.biometricKeyCache = null
    }
}