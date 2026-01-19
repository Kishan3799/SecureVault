package com.kriahsnverma.securevault.di

import com.kriahsnverma.securevault.core.crypto.EncryptionUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CryptoModule {

    @Provides
    @Singleton
    fun provideEncryptionUtils(): EncryptionUtils {
        return EncryptionUtils // Return the singleton object
    }
}