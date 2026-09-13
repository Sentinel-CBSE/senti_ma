package com.unal.senti_ma.ui.screens.robbery_map

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.model.MapBounds
import com.unal.senti_ma.ui.screens.robbery_map.components.LocationPermissionHandler
import com.unal.senti_ma.ui.screens.robbery_map.components.RobberyDateRangeDialog
import com.unal.senti_ma.ui.screens.robbery_map.components.RobberyTypeFilterDropdown
import com.unal.senti_ma.ui.screens.robbery_map.events.RobberyMapUiEvent
import com.unal.senti_ma.ui.screens.robbery_map.map.HeatmapOverlay
import com.unal.senti_ma.ui.screens.robbery_map.map.rememberMapViewWithLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import kotlin.time.Duration.Companion.milliseconds

private const val RELOAD_DEBOUNCE_MS = 400L

@Composable
fun RobberyMapScreen(
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel = hiltViewModel()
) {
    val robberyMapUiState by mapViewModel.uiState.collectAsState()

    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle(context)
    val heatmapOverlay = remember { HeatmapOverlay() }

    val coroutineScope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }
    var isDatePickerVisible by remember { mutableStateOf(false) }

    var hasLocationPermission by remember {
        mutableStateOf(
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var locationOverlay by remember {
        mutableStateOf<MyLocationNewOverlay?>(null)
    }

    fun scheduleReload(map: MapView) {
        debounceJob?.cancel()

        debounceJob = coroutineScope.launch {
            delay(RELOAD_DEBOUNCE_MS.milliseconds)

            val box = map.boundingBox
            if (box.latNorth <= box.latSouth || box.lonEast <= box.lonWest) {
                return@launch
            }

            mapViewModel.onEvent(
                RobberyMapUiEvent.UpdateMapBounds(
                    MapBounds(
                        northLat = box.latNorth,
                        southLat = box.latSouth,
                        eastLon = box.lonEast,
                        westLon = box.lonWest
                    )
                )
            )
        }
    }

    LocationPermissionHandler(
        onPermissionGranted = { hasLocationPermission = true },
        onPermissionDenied = {
            hasLocationPermission = false

            Toast.makeText(context, R.string.text_location_permission_denied, Toast.LENGTH_SHORT).show()
        }
    )

    LaunchedEffect(robberyMapUiState) {
        when (val state = robberyMapUiState) {
            is MapUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }

            is MapUiState.Success -> {
                heatmapOverlay.updatePoints(state.heatPoints)
                mapView.invalidate()
            }

            MapUiState.Idle,
            MapUiState.Loading -> Unit
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RobberyTypeFilterDropdown(
                selectedType = mapViewModel.selectedType,
                onTypeSelected = { type -> mapViewModel.onEvent(RobberyMapUiEvent.UpdateTypeFilter(type)) },
                modifier = Modifier.weight(1f)
            )

            TextButton(onClick = { isDatePickerVisible = true }) {
                Text(
                    text = stringResource(
                        if (mapViewModel.selectedFromTimestamp != null) {
                            R.string.text_date_filter_selected
                        } else {
                            R.string.text_date_filter_all
                        }
                    )
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clipToBounds()
        ) {
            AndroidView(
                factory = {
                    mapView.apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)

                        isHorizontalMapRepetitionEnabled = false
                        isVerticalMapRepetitionEnabled = false

                        overlays.add(heatmapOverlay)

                        controller.setZoom(12.0)
                        controller.setCenter(
                            org.osmdroid.util.GeoPoint(
                                4.60971,
                                -74.08175
                            )
                        )

                        addMapListener(
                            object : MapListener {
                                override fun onScroll(
                                    event: ScrollEvent?
                                ): Boolean {
                                    heatmapOverlay.invalidateCache()
                                    invalidate()
                                    scheduleReload(this@apply)
                                    return true
                                }

                                override fun onZoom(
                                    event: ZoomEvent?
                                ): Boolean {
                                    heatmapOverlay.invalidateCache()
                                    invalidate()
                                    scheduleReload(this@apply)
                                    return true
                                }
                            }
                        )

                        scheduleReload(this)
                    }
                },
                update = { view ->
                    if (hasLocationPermission && locationOverlay == null) {
                        val overlay = MyLocationNewOverlay(
                            GpsMyLocationProvider(context),
                            view
                        )

                        overlay.enableMyLocation()
                        overlay.runOnFirstFix {
                            view.post {
                                overlay.myLocation?.let { location ->
                                    view.controller.setZoom(15.0)
                                    view.controller.setCenter(location)
                                    scheduleReload(view)
                                }
                            }
                        }

                        view.overlays.add(overlay)
                        locationOverlay = overlay

                        view.invalidate()
                    } else if (!hasLocationPermission) {
                        locationOverlay?.let {
                            it.disableMyLocation()
                            view.overlays.remove(it)
                        }

                        locationOverlay = null
                        view.invalidate()
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            if (robberyMapUiState is MapUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    if (isDatePickerVisible) {
        RobberyDateRangeDialog(
            initialStartDateMillis = mapViewModel.selectedFromTimestamp,
            initialEndDateMillis = mapViewModel.selectedToTimestamp,
            onDismiss = { isDatePickerVisible = false },
            onConfirm = { from, to ->
                isDatePickerVisible = false

                mapViewModel.onEvent(
                    RobberyMapUiEvent.UpdateDateRangeFilter(
                        fromTimestamp = from,
                        toTimestamp = to
                    )
                )
            },
            onClear = {
                isDatePickerVisible = false

                mapViewModel.onEvent(
                    RobberyMapUiEvent.UpdateDateRangeFilter(
                        fromTimestamp = null,
                        toTimestamp = null
                    )
                )
            }
        )
    }
}
