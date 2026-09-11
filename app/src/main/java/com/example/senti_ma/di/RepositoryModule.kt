package com.example.senti_ma.di

import com.example.senti_ma.data.repository.AuthRepositoryImpl
import com.example.senti_ma.data.repository.SettingsRepositoryImpl
import com.example.senti_ma.domain.repository.AuthRepository
import com.example.senti_ma.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger module that provides instances related to Repository.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

}
