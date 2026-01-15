package com.kriahsnverma.securevault.core.crypto

import javax.crypto.SecretKey

object MasterKeyHolder {
    private var secretKey: SecretKey? = null

    fun isUnlocked(): Boolean {
        return secretKey != null
    }

    fun lock(){
        secretKey = null
    }

    fun get(): SecretKey? {
        return this.secretKey
    }

    fun setKey(key: SecretKey) {
        secretKey =key
    }

    fun getKey(): SecretKey {
        return secretKey ?:  throw IllegalStateException("Vault is Locked. Use MasterKeyHolder.get() for a safe call.")
    }

    fun clear() {
        this.secretKey = null
    }
}