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

        points.forEach { robbery ->
            val marker = Marker(mapView).apply {
                position = GeoPoint(
                    robbery.latitude,
                    robbery.longitude
                )

                setAnchor(
                    Marker.ANCHOR_CENTER,
                    Marker.ANCHOR_BOTTOM
                )

                setOnMarkerClickListener { _, _ ->
                    onRobberyClick(robbery.id)
                    true
                }
            }

            markers += marker
            mapView.overlays.add(marker)
        }
    }

    fun setVisible(visible: Boolean) {
        markers.forEach { marker ->
            marker.isEnabled = visible
        }

        mapView.invalidate()
    }

    private fun clearMarkers() {
        markers.forEach { marker ->
            mapView.overlays.remove(marker)
        }

        markers.clear()
    }

    fun clear() {
        clearMarkers()
        mapView.invalidate()
    }

}
