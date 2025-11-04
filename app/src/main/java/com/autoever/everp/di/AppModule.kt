package com.autoever.everp.di

import android.content.Context
import android.content.SharedPreferences
import com.autoever.everp.auth.session.TokenStore
import com.autoever.everp.auth.session.TokenStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideTokenStore(prefs: SharedPreferences): TokenStore = TokenStoreImpl(prefs)
}
