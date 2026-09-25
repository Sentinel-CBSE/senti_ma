package com.unal.senti_ma.ui.screens.robbery.map

import android.content.Context
import androidx.core.content.ContextCompat
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.library.R as OsmdroidR

class RobberyMapController(
    private val mapView: MapView,
    context: Context,
    private val onRobberyClick: (String) -> Unit
) {

    val heatmapOverlay = HeatmapOverlay()

    val userLocationMarker = Marker(mapView).apply {
        setAnchor(
            Marker.ANCHOR_CENTER,
            Marker.ANCHOR_CENTER
        )

        icon = ContextCompat.getDrawable(
            context,
            OsmdroidR.drawable.person
        )

        setOnMarkerClickListener { _, _ ->
            true
        }
    }

    val pointMarkers = RobberyPointMarkers(
        mapView = mapView,
        onRobberyClick = onRobberyClick
    )

    fun clear() {
        pointMarkers.clear()

        mapView.overlays.remove(userLocationMarker)
        mapView.overlays.remove(heatmapOverlay)

        mapView.invalidate()
    }

}
