package com.kriahsnverma.securevault.data.usecase

import com.kriahsnverma.securevault.data.repository.MasterPasswordRepository
import jakarta.inject.Inject
import javax.crypto.SecretKey

/**
 * Use case for the initial setup of the master password.
 * This is the ONLY place where a new master password should be created.
 */
class SetupMasterPasswordUseCase @Inject constructor(
    private val repository: MasterPasswordRepository // <-- 2. Depend on the interface
) {
    /**
     * Hashes the given password and saves it.
     */
    suspend operator fun invoke(password: String) {
        // 3. Delegate the entire operation to the repository.
        // The use case doesn't need to know about hashing or salt.
        repository.saveNewMasterPassword(password)
    }
}

/**
 * Use case for unlocking the vault.
 * It validates the password and, if correct, generates the master key.
 */
class UnlockVaultUseCase @Inject constructor(
    private val repository: MasterPasswordRepository // <-- Depend on the interface
) {
    /**
     * Validates the password.
     * @return The SecretKey if the password is correct, or null if it's incorrect.
     */
    suspend operator fun invoke(password: String): SecretKey? {
        val isPasswordCorrect = repository.validateMasterPassword(password)

        return if (isPasswordCorrect) {
            // If valid, also perform the key generation.
            repository.generateMasterKey(password)
        } else {
            null
        }
    }
}

/**
 * Use case to check if the app has been set up already.
 */
class HasMasterPasswordUseCase @Inject constructor(
    private val repository: MasterPasswordRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.hasMasterPassword()
    }
}
