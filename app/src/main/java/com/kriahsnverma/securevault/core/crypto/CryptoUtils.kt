package com.kriahsnverma.securevault.core.crypto


import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class CryptoUtils @Inject constructor() {
    private  val ITERATION_COUNT =  120_000
    private  val KEY_LENGTH = 256
    private  val AES_MODE = "AES/GCM/NoPadding"

    fun generateKey(masterPassword: String, salt: ByteArray): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(masterPassword.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
        val secret = factory.generateSecret(spec)
        return SecretKeySpec(secret.encoded, "AES")
    }

    fun encrypt(plainText: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance(AES_MODE)
        val iv = ByteArray(12).apply { SecureRandom().nextBytes(this) }
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, GCMParameterSpec(128, iv))
        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        val combined = iv + encryptedBytes
        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    fun decrypt(data: String, key: SecretKey): String {
        val decoded = Base64.decode(data, Base64.DEFAULT)
        val iv = decoded.copyOfRange(0, 12)
        val cipherText = decoded.copyOfRange(12, decoded.size)
        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
        return String(cipher.doFinal(cipherText), Charsets.UTF_8)
    }
}
