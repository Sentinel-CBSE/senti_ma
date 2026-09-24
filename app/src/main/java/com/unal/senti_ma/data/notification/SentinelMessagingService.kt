package com.unal.senti_ma.data.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.unal.senti_ma.domain.usecase.notification.RegisterInstallationIdUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SentinelMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var registerInstallationIdUseCase: RegisterInstallationIdUseCase

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private val serviceScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        private const val TAG = "SentinelMessaging"
    }

    override fun onRegistered(installationId: String) {
        super.onRegistered(installationId)

        Log.d(TAG, "Registered installation ID")

        serviceScope.launch {
            registerInstallationIdUseCase(installationId)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val notification = remoteMessage.notification

        val title = notification?.title
            ?: remoteMessage.data["title"]
            ?: return

        val body = notification?.body
            ?: remoteMessage.data["body"]
            ?: ""

        val eventId = remoteMessage.data["eventId"]

        notificationHelper.showEventNotification(
            title = title,
            body = body,
            eventId = eventId
        )
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

}
