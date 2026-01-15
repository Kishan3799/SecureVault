package com.kriahsnverma.securevault.di

import android.content.Context
import androidx.room.Room
import com.kriahsnverma.securevault.core.crypto.CryptoUtils
import com.kriahsnverma.securevault.data.local.AppDatabase
import com.kriahsnverma.securevault.data.local.MasterPasswordDao
import com.kriahsnverma.securevault.data.local.PasswordDao
import com.kriahsnverma.securevault.data.repository.MasterPasswordRepository
import com.kriahsnverma.securevault.data.repository.MasterPasswordRepositoryImpl
import com.kriahsnverma.securevault.data.repository.PasswordRepository
import com.kriahsnverma.securevault.data.repository.PasswordRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "secure_vault.db"
        ).fallbackToDestructiveMigration(false).build()
    }

    @Provides
    @Singleton
    fun provideMasterPasswordDao(db: AppDatabase): MasterPasswordDao {
        return db.masterPasswordDao()
    }

    @Provides
    @Singleton
    fun providePasswordDao(db: AppDatabase): PasswordDao {
        return db.passwordDao()
    }




}



