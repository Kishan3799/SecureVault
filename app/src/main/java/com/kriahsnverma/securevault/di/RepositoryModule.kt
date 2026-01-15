package com.kriahsnverma.securevault.di

import com.kriahsnverma.securevault.data.repository.MasterPasswordRepository
import com.kriahsnverma.securevault.data.repository.MasterPasswordRepositoryImpl
import com.kriahsnverma.securevault.data.repository.PasswordRepository
import com.kriahsnverma.securevault.data.repository.PasswordRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMasterPasswordRepository(
        impl: MasterPasswordRepositoryImpl // Hilt knows how to create this because it has an @Inject constructor
    ): MasterPasswordRepository

    @Binds
    @Singleton
    abstract fun bindPasswordRepository(
        passwordRepositoryImpl: PasswordRepositoryImpl // Hilt also knows how to create this
    ): PasswordRepository
}