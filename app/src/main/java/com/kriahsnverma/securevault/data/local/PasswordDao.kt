package com.kriahsnverma.securevault.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface PasswordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(password: PasswordEntity)

    @Query("SELECT * FROM passwords ORDER BY id DESC")
    fun getPasswords(): Flow<List<PasswordEntity>>

    @Query("SELECT * FROM passwords WHERE id = :id")
    suspend fun getPasswordById(id: Int): PasswordEntity?

    @Query("DELETE FROM passwords WHERE id = :id")
    suspend fun deletePasswordById(id: Int)

    @Update
    suspend fun updatePassword(password: PasswordEntity)


}