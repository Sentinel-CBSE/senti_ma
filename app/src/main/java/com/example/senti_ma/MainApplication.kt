package com.example.senti_ma

import android.app.Application
import android.util.Log
import com.google.android.gms.security.ProviderInstaller
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // initialize firebase
        FirebaseApp.initializeApp(this)

        try {
            ProviderInstaller.installIfNeeded(this@MainApplication)
        } catch (t: Throwable) {
            Log.e("Dev", "error to install the Provider:: ${t.message}", t)
        }
    }
}
