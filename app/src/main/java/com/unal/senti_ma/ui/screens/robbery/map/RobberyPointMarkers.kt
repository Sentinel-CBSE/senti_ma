package com.unal.senti_ma.ui.screens.robbery.map

import com.unal.senti_ma.domain.model.RobberyPoint
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class RobberyPointMarkers(
    private val mapView: MapView,
    private val onRobberyClick: (String) -> Unit
) {

    private val markers = mutableListOf<Marker>()

    fun updatePoints(points: List<RobberyPoint>) {
        clearMarkers()

        val newMarkers = points.map { robbery ->
            Marker(mapView).apply {
                position = GeoPoint(robbery.latitude, robbery.longitude)
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                setOnMarkerClickListener { _, _ ->
                    onRobberyClick(robbery.id)
                    true
                }
            }
        }

        markers.addAll(newMarkers)
        mapView.overlays.addAll(newMarkers)
        mapView.invalidate()
    }

    fun setVisible(visible: Boolean) {
        markers.forEach { marker ->
            marker.isEnabled = visible
        }
        mapView.invalidate()
    }

    private fun clearMarkers() {
        if (markers.isNotEmpty()) {
            mapView.overlays.removeAll(markers)
            markers.clear()
        }
    }

    fun clear() {
        clearMarkers()
        mapView.invalidate()
    }

}
