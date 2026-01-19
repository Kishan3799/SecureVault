package com.kriahsnverma.securevault.data.repository

import com.kriahsnverma.securevault.data.local.PasswordEntity
import kotlinx.coroutines.flow.Flow
import javax.crypto.SecretKey

interface PasswordRepository {
    suspend fun addPassword(title: String,
                            username: String,
                            plainPassword: String,
                            notes: String?,
                            url: String?,
                            category: String)
    fun getPasswords(): Flow<List<PasswordEntity>>

    suspend fun getPasswordById(id: Int): PasswordEntity?

    suspend fun updatePassword(id: Int,
                               title: String,
                               username: String,
                               plainPassword: String,
                               notes: String?,
                               url: String?,
                               category: String)


    suspend fun deletePasswordById(id:Int)

    suspend fun reEncryptAllData(oldKey: SecretKey, newKey: SecretKey)
}



