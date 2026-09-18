package com.unal.senti_ma

import android.app.Application
import android.util.Log
import com.google.android.gms.security.ProviderInstaller
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class MainApplication : Application() {

    private val applicationScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        // initialize firebase
        FirebaseApp.initializeApp(this)

        applicationScope.launch {
            try {
                ProviderInstaller.installIfNeeded(this@MainApplication)
            } catch (t: Throwable) {
                Log.e(
                    "MainApplication",
                    "Error installing security provider",
                    t
                )
            }
        }
    }
}
