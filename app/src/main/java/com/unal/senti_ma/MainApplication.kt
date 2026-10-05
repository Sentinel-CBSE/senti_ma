package com.unal.senti_ma

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.unal.senti_ma.data.notification.NotificationHelper
import com.unal.senti_ma.domain.usecase.auth.ObserveAuthStateUseCase
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

    private val applicationScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        private const val TAG = "MainApplication"
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        notificationHelper.createNotificationChannel()

        observeAndRegisterInstallation()
    }

    private fun observeAndRegisterInstallation() {
        applicationScope.launch {
            observeAuthStateUseCase()
                .filterNotNull()
                .distinctUntilChangedBy { user -> user.uid }
                .collect {
                    registerCurrentInstallation()
                }
        }
    }

    private suspend fun registerCurrentInstallation() {
        try {
            FirebaseMessaging.getInstance()
                .register()
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error registering FCM installation", e)
        }
    }

}
