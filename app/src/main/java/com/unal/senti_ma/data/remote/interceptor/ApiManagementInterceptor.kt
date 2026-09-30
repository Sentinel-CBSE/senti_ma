package com.unal.senti_ma.data.remote.interceptor

import com.unal.senti_ma.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiManagementInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(
                "Ocp-Apim-Subscription-Key",
                BuildConfig.SENTI_APIM_SUBSCRIPTION_KEY
            )
            .build()

        return chain.proceed(request)
    }
}
