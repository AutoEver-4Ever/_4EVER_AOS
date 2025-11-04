package com.autoever.everp.di

import com.autoever.everp.auth.api.AuthApi
import com.autoever.everp.auth.api.HttpAuthApi
import com.autoever.everp.auth.api.HttpUserApi
import com.autoever.everp.auth.api.UserApi
import com.autoever.everp.auth.repository.AuthRepository
import com.autoever.everp.auth.repository.DefaultAuthRepository
import com.autoever.everp.auth.repository.DefaultUserRepository
import com.autoever.everp.auth.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: DefaultAuthRepository): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: DefaultUserRepository): UserRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AuthApiModule {
    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi = HttpAuthApi()

    @Provides
    @Singleton
    fun provideUserApi(): UserApi = HttpUserApi()
}

