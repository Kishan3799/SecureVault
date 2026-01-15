package com.kriahsnverma.securevault.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MasterPasswordDao {
    @Query("SELECT * FROM master_password WHERE id = 0")
    suspend fun getMasterPassword(): MasterPasswordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMasterPassword(masterPassword: MasterPasswordEntity)
}