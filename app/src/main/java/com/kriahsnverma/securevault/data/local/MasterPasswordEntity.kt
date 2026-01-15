package com.kriahsnverma.securevault.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "master_password")
data class MasterPasswordEntity(
    @PrimaryKey val id: Int = 0,
    val passwordHash: String,
)

