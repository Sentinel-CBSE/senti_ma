package com.unal.senti_ma.di

import android.content.Context
import android.location.Geocoder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GeocoderModule {

    @Provides
    @Singleton
    fun provideGeocoder(
        @ApplicationContext context: Context
    ): Geocoder {
        return Geocoder(context)
    }

}
