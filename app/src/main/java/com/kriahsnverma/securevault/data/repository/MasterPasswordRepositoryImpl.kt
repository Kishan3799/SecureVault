package com.kriahsnverma.securevault.data.repository

import com.kriahsnverma.securevault.core.crypto.EncryptionUtils
import com.kriahsnverma.securevault.data.local.MasterPasswordDao
import com.kriahsnverma.securevault.data.local.MasterPasswordEntity
import javax.crypto.SecretKey
import javax.inject.Inject

class MasterPasswordRepositoryImpl @Inject constructor(
    private val dao: MasterPasswordDao
) : MasterPasswordRepository {

    // A private helper to get the entity, reducing redundant code.
    private suspend fun getPasswordEntity(): MasterPasswordEntity? = dao.getMasterPassword()

    override suspend fun hasMasterPassword(): Boolean {
        // More direct and readable: does an entry exist?
        return getPasswordEntity() != null
    }

    override suspend fun saveNewMasterPassword(password: String) {
        // The repository is now responsible for the hashing step.
        val hash = EncryptionUtils.hashPassword(password)
        dao.saveMasterPassword(MasterPasswordEntity(passwordHash = hash))
    }

    override suspend fun validateMasterPassword(password: String): Boolean {
        val storedEntity = getPasswordEntity() ?: return false
        // Use the simple verifyPassword function here.
        return EncryptionUtils.verifyPassword(password, storedEntity.passwordHash)
    }

    override suspend fun generateMasterKey(password: String): SecretKey {
        val storedEntity = getPasswordEntity()
            ?: throw IllegalStateException("Cannot generate key: No master password is set.")

        // 1. Verify the password and get the full result object.
        val verificationResult = EncryptionUtils.verifyAndGetResult(password, storedEntity.passwordHash)

        // 2. Check if the password was actually correct before proceeding.
        if (!verificationResult.verified) {
            throw SecurityException("Incorrect password provided for key generation.")
        }

        // 3. Get the salt from the result object.
        val salt = verificationResult.details.rawSalt

        // 4. Use the correct password and the extracted salt to derive the key.
        return EncryptionUtils.generateKeyFromPassword(password, salt)
    }
}
