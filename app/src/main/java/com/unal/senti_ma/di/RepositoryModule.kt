package com.unal.senti_ma.di

import com.unal.senti_ma.data.repository.AuthRepositoryImpl
import com.unal.senti_ma.data.repository.GeocodingRepositoryImpl
import com.unal.senti_ma.data.repository.LocationRepositoryImpl
import com.unal.senti_ma.data.repository.NotificationRepositoryImpl
import com.unal.senti_ma.data.repository.RobberyRepositoryImpl
import com.unal.senti_ma.data.repository.SettingsRepositoryImpl
import com.unal.senti_ma.data.repository.UserRepositoryImpl
import com.unal.senti_ma.domain.repository.AuthRepository
import com.unal.senti_ma.domain.repository.GeocodingRepository
import com.unal.senti_ma.domain.repository.LocationRepository
import com.unal.senti_ma.domain.repository.NotificationRepository
import com.unal.senti_ma.domain.repository.RobberyRepository
import com.unal.senti_ma.domain.repository.SettingsRepository
import com.unal.senti_ma.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindRobberyRepository(
        impl: RobberyRepositoryImpl
    ): RobberyRepository

    @Binds
    @Singleton
    abstract fun bindGeocodingRepository(
        impl: GeocodingRepositoryImpl
    ): GeocodingRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        impl: LocationRepositoryImpl
    ): LocationRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository

}
