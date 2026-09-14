package com.unal.senti_ma.ui.screens.robbery_map.map

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RadialGradient
import android.graphics.Shader
import androidx.core.graphics.createBitmap
import com.unal.senti_ma.domain.model.HeatmapPoint
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay

class HeatmapOverlay(
    private var points: List<HeatmapPoint> = emptyList(),
    private val radiusPx: Float = 80f
) : Overlay() {

    private var cachedBitmap: android.graphics.Bitmap? = null
    private var lastWidth = 0
    private var lastHeight = 0

    fun updatePoints(newPoints: List<HeatmapPoint>) {
        points = newPoints
        cachedBitmap = null
    }

    fun invalidateCache() {
        cachedBitmap = null
    }

    override fun draw(canvas: Canvas, mapView: MapView, shadow: Boolean) {
        if (shadow || points.isEmpty()) return

        val width = mapView.width
        val height = mapView.height
        if (width == 0 || height == 0) return

        if (cachedBitmap == null || lastWidth != width || lastHeight != height) {
            cachedBitmap = buildHeatmapBitmap(mapView, width, height)
            lastWidth = width
            lastHeight = height
        }

        cachedBitmap?.let { canvas.drawBitmap(it, 0f, 0f, null) }
    }

    private fun buildHeatmapBitmap(
        mapView: MapView,
        width: Int,
        height: Int
    ): android.graphics.Bitmap {
        val intensityBitmap = createBitmap(width, height)
        val intensityCanvas = Canvas(intensityBitmap)

        val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SCREEN)
        }

        val projection = mapView.projection
        val point = android.graphics.Point()

        points.forEach { p ->
            projection.toPixels(GeoPoint(p.latitude, p.longitude), point)
            if (point.x in -radiusPx.toInt()..(width + radiusPx.toInt()) &&
                point.y in -radiusPx.toInt()..(height + radiusPx.toInt())
            ) {
                val gradient = RadialGradient(
                    point.x.toFloat(), point.y.toFloat(), radiusPx,
                    intArrayOf(
                        Color.argb((255 * p.intensity.coerceIn(0f, 1f)).toInt(), 255, 255, 255),
                        Color.argb(0, 255, 255, 255)
                    ),
                    null, Shader.TileMode.CLAMP
                )
                dotPaint.shader = gradient
                intensityCanvas.drawCircle(point.x.toFloat(), point.y.toFloat(), radiusPx, dotPaint)
            }
        }

        val heatBitmap = createBitmap(width, height)
        val pixels = IntArray(width * height)
        intensityBitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        for (i in pixels.indices) {
            val alpha = Color.alpha(pixels[i])
            pixels[i] = if (alpha == 0) Color.TRANSPARENT else intensityToColor(alpha)
        }
        heatBitmap.setPixels(pixels, 0, width, 0, 0, width, height)

        return heatBitmap
    }

    private fun intensityToColor(alpha: Int): Int {
        val ratio = alpha / 255f
        val hue = (1f - ratio) * 240f
        val color = Color.HSVToColor(floatArrayOf(hue, 1f, 1f))
        val finalAlpha = (150 * ratio).toInt().coerceIn(0, 200)
        return Color.argb(finalAlpha, Color.red(color), Color.green(color), Color.blue(color))
    }

}
