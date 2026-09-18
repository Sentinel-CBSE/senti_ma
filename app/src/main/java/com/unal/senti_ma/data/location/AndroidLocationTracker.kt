package com.unal.senti_ma.data.location

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.unal.senti_ma.domain.location.LocationTracker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidLocationTracker @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationTracker {

    override fun start() {
        val intent = Intent(context, LocationForegroundService::class.java)
        ContextCompat.startForegroundService(context, intent)
    }

    override fun stop() {
        val intent = Intent(context, LocationForegroundService::class.java)
        context.stopService(intent)
    }

}
