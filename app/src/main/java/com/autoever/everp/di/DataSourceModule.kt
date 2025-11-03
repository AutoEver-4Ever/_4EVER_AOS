package com.autoever.everp.di

import com.autoever.everp.data.datasource.remote.SdRemoteDataSource
import com.autoever.everp.data.datasource.remote.http.impl.SdHttpRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Application 범위에 설치
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindsSdRemoteDataSource(sdRemoteDataSourceImpl: SdHttpRemoteDataSourceImpl): SdRemoteDataSource
}
