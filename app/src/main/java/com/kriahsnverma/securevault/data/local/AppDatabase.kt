package com.kriahsnverma.securevault.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PasswordEntity::class, MasterPasswordEntity::class], version = 4, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun passwordDao(): PasswordDao
    abstract fun masterPasswordDao(): MasterPasswordDao
}
