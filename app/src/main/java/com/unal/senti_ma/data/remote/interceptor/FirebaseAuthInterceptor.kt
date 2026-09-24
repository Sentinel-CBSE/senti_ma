package com.unal.senti_ma.data.remote.interceptor

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthInterceptor @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val currentUser = firebaseAuth.currentUser
            ?: return chain.proceed(originalRequest)

        val idToken: String? = try {
            Tasks.await(
                currentUser.getIdToken(false),
                10,
                TimeUnit.SECONDS
            ).token
        } catch (_: Exception) {
            null
        }

        val newRequest = if (idToken != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $idToken")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(newRequest)
    }
}
