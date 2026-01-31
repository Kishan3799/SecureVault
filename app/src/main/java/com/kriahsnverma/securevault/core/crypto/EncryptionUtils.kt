package com.kriahsnverma.securevault.core.crypto

import android.util.Base64
import at.favre.lib.crypto.bcrypt.BCrypt
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object EncryptionUtils {
    private const val BCRYPT_COST_FACTOR = 12
    private const val PBKDF2_ITERATIONS = 65536
    private const val PBKDF2_KEY_LENGTH = 256
    private const val AES_MODE = "AES/GCM/NoPadding"
    private const val IV_LENGTH = 12
    private const val TAG_LENGTH = 128

    fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(BCRYPT_COST_FACTOR, password.toCharArray())
    }

    fun verifyAndGetResult(password: String, storedHash: String): BCrypt.Result {
        return BCrypt.verifyer().verify(password.toCharArray(), storedHash.toByteArray(StandardCharsets.UTF_8))
    }

    fun verifyPassword(password: String, storedHash: String): Boolean {
        return verifyAndGetResult(password, storedHash).verified
    }

    fun generateKeyFromPassword(password: String, salt: ByteArray): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITERATIONS, PBKDF2_KEY_LENGTH)
        val secret = factory.generateSecret(spec)
        return SecretKeySpec(secret.encoded, "AES")
    }

    fun encrypt(plainText: String, key: SecretKey): String {
        val cipher = Cipher.getInstance(AES_MODE)
        val iv = ByteArray(IV_LENGTH).apply { SecureRandom().nextBytes(this) }
        cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(TAG_LENGTH, iv))
        
        val encryptedBytes = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
        val combined = iv + encryptedBytes
        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    fun decrypt(encryptedData: String, key: SecretKey): String {
        val decoded = Base64.decode(encryptedData, Base64.NO_WRAP)
        val iv = decoded.copyOfRange(0, IV_LENGTH)
        val cipherText = decoded.copyOfRange(IV_LENGTH, decoded.size)
        
        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(TAG_LENGTH, iv))
        
        return String(cipher.doFinal(cipherText), StandardCharsets.UTF_8)
    }
}
