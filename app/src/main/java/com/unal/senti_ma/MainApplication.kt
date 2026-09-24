package com.unal.senti_ma

import android.app.Application
import android.util.Log
import com.google.android.gms.security.ProviderInstaller
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.unal.senti_ma.data.notification.NotificationHelper
import com.unal.senti_ma.domain.usecase.auth.ObserveAuthStateUseCase
import com.unal.senti_ma.domain.usecase.notification.RegisterDeviceTokenUseCase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var observeAuthStateUseCase: ObserveAuthStateUseCase

    @Inject
    lateinit var registerDeviceTokenUseCase: RegisterDeviceTokenUseCase

    private val applicationScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        private const val TAG = "MainApplication"
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        notificationHelper.createNotificationChannel()

        applicationScope.launch {
            try {
                ProviderInstaller.installIfNeeded(this@MainApplication)
            } catch (t: Throwable) {
                Log.e(TAG, "Error installing security provider", t)
            }
        }

        observeAndRegisterDeviceToken()
    }

    private fun observeAndRegisterDeviceToken() {
        applicationScope.launch {
            observeAuthStateUseCase()
                .filterNotNull()
                .distinctUntilChangedBy { user -> user.uid }
                .collect {
                    registerCurrentFcmToken()
                }
        }
    }

    private suspend fun registerCurrentFcmToken() {
        try {
            val token = FirebaseMessaging.getInstance().token.await()
            registerDeviceTokenUseCase(token)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching/registering FCM token", e)
        }
    }

}
