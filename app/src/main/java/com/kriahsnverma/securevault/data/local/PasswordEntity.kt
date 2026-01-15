package com.kriahsnverma.securevault.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="passwords")
data class PasswordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val username: String,
    val encryptedPassword: String, // 🔐 encrypted
    val notes: String?,
    val url: String?,
    val category: String,
    val createdAt: Long = System.currentTimeMillis()
)