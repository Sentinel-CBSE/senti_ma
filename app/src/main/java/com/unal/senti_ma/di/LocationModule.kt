package com.unal.senti_ma.di

import com.unal.senti_ma.data.location.AndroidLocationTracker
import com.unal.senti_ma.data.location.LocationManager
import com.unal.senti_ma.domain.location.LocationClient
import com.unal.senti_ma.domain.location.LocationTracker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationClient(
        locationManager: LocationManager
    ): LocationClient

    @Binds
    @Singleton
    abstract fun bindLocationTracker(
        impl: AndroidLocationTracker
    ): LocationTracker

}
