package com.kriahsnverma.securevault.core.crypto

import android.util.Base64
import at.favre.lib.crypto.bcrypt.BCrypt
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


object EncryptionUtils {
    private const val BCRYPT_COST_FACTOR = 12 // A good balance between speed and security.
    private const val PBKDF2_ITERATIONS = 65536 // Standard iteration count for key derivation.
    private const val PBKDF2_KEY_LENGTH = 256 //  For AES-256.


//    fun generateSalt(): String {
//        val salt = ByteArray(16)
//        SecureRandom().nextBytes(salt)
//        return Base64.encodeToString(salt, Base64.DEFAULT)
//    }

    /**
     * Hashes a password using BCrypt.
     * The returned string contains the algorithm, cost factor, salt, and hash.
     * This is the only string you need to store in the database.
     */
    fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(BCRYPT_COST_FACTOR, password.toCharArray())
    }

    // --- THIS IS THE KEY CHANGE ---
    /**
     * Verifies a password and returns the full result, which includes the salt.
     * This is the official way to get the salt from a hash with this library.
     *
     * @return A BCrypt.Result object. Use result.verified and result.rawSalt.
     */
    fun verifyAndGetResult(password: String, storedHash: String): BCrypt.Result {
        return BCrypt.verifyer().verify(password.toCharArray(), storedHash.toByteArray(StandardCharsets.UTF_8))
    }

    // The old verifyPassword can be removed or kept for other uses if needed.
    fun verifyPassword(password: String, storedHash: String): Boolean {
        return verifyAndGetResult(password, storedHash).verified
    }

    /**
     * Securely derives a SecretKey from a password and a salt using PBKDF2.
     * This key is used for encrypting/decrypting data, NOT for password storage.
     */
    fun generateKeyFromPassword(password: String, salt: ByteArray): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITERATIONS, PBKDF2_KEY_LENGTH)
        val secret = factory.generateSecret(spec)
        return SecretKeySpec(secret.encoded, "AES")
    }


}
