package com.kriahsnverma.securevault.data.repository

import javax.crypto.SecretKey

/**
 * Interface defining the contract for managing the master password.
 */
interface MasterPasswordRepository {

    /**
     * Checks if a master password has been set in the database.
     * @return true if a hash exists, false otherwise.
     */
    suspend fun hasMasterPassword(): Boolean

    /**
     * Saves a new master password hash to the database.
     * This should only be called during initial app setup.
     * @param password The user's chosen plain-text password.
     */
    suspend fun saveNewMasterPassword(password: String)

    /**
     * Validates a plain-text password against the stored hash.
     * @param password The password entered by the user.
     * @return true if the password is correct, false otherwise.
     */
    suspend fun validateMasterPassword(password: String): Boolean

    /**
     * Generates the encryption/decryption SecretKey from the given password.
     * This should only be called after a successful password validation.
     * @param password The correct plain-text master password.
     * @return The derived SecretKey for cryptographic operations.
     */
    suspend fun generateMasterKey(password: String): SecretKey

//    suspend fun updateMasterPasswordWithSalt(newPassword: String, newSalt: ByteArray?)
}



