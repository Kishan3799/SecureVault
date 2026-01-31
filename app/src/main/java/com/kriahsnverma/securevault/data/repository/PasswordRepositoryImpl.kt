package com.kriahsnverma.securevault.data.repository

import com.kriahsnverma.securevault.core.crypto.EncryptionUtils
import com.kriahsnverma.securevault.core.crypto.MasterKeyHolder
import com.kriahsnverma.securevault.data.local.PasswordDao
import com.kriahsnverma.securevault.data.local.PasswordEntity
import kotlinx.coroutines.flow.Flow
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordRepositoryImpl @Inject constructor(
    private val dao: PasswordDao
) : PasswordRepository {

    override suspend fun addPassword(
        title: String,
        username: String,
        plainPassword: String,
        notes: String?,
        url: String?,
        category: String
    ) {
        val key = MasterKeyHolder.get() ?: throw IllegalStateException("Vault is Locked")

        val encryptedPassword = EncryptionUtils.encrypt(
            plainPassword,
            key
        )

        dao.insertPassword(
            PasswordEntity(
                title = title,
                username = username,
                encryptedPassword = encryptedPassword,
                notes = notes,
                url = url,
                category = category
            )
        )
    }

    override fun getPasswords(): Flow<List<PasswordEntity>> {
        return dao.getPasswords()
    }

    override suspend fun getPasswordById(id: Int): PasswordEntity? {
        return dao.getPasswordById(id)
    }

    override suspend fun updatePassword(
        id: Int,
        title: String,
        username: String,
        plainPassword: String,
        notes: String?,
        url: String?,
        category: String,
    ) {
        val key = MasterKeyHolder.get() ?: throw IllegalStateException("Vault is Locked")
        val encryptedPassword = EncryptionUtils.encrypt(
            plainPassword,
            key
        )

        val updatedEntity = PasswordEntity(
            id = id,
            title = title,
            username = username,
            encryptedPassword = encryptedPassword,
            notes = notes,
            url = url,
            category = category
        )

        dao.updatePassword(updatedEntity)
    }

    override suspend fun deletePasswordById(id: Int) {
        dao.deletePasswordById(id)
    }

    override suspend fun reEncryptAllData(oldKey: SecretKey, newKey: SecretKey) {
        val allEntries = dao.getAllPasswordsSync()

        allEntries.forEach { entity ->
            try {
                val decryptedPlainText = EncryptionUtils.decrypt(entity.encryptedPassword, oldKey)
                val newlyEncryptedSecret = EncryptionUtils.encrypt(decryptedPlainText, newKey)

                dao.updatePassword(entity.copy(encryptedPassword = newlyEncryptedSecret))
            } catch (e: Exception) {
                // Log error or handle it - if one fails, we might want to know why, 
                // but usually this happens if the oldKey is wrong.
                e.printStackTrace()
            }
        }
    }
}
