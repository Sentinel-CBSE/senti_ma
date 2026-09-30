package com.unal.senti_ma.di

import com.unal.senti_ma.BuildConfig
import com.unal.senti_ma.data.remote.api.SentinelApi
import com.unal.senti_ma.data.remote.interceptor.ApiManagementInterceptor
import com.unal.senti_ma.data.remote.interceptor.FirebaseAuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            redactHeader("Authorization")

            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        firebaseAuthInterceptor: FirebaseAuthInterceptor,
        apiManagementInterceptor: ApiManagementInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(firebaseAuthInterceptor)
            .addInterceptor(apiManagementInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SENTI_BACK_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSentinelApi(retrofit: Retrofit): SentinelApi {
        return retrofit.create(SentinelApi::class.java)
    }

}
