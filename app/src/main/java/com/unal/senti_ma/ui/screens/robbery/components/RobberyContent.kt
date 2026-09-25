package com.unal.senti_ma.ui.screens.robbery.components

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.enums.RobberyDisplayMode
import com.unal.senti_ma.domain.model.Coordinates
import com.unal.senti_ma.domain.model.MapBounds
import com.unal.senti_ma.ui.screens.robbery.RobberyUiState
import com.unal.senti_ma.ui.screens.robbery.events.RobberyViewModelEvent
import com.unal.senti_ma.ui.screens.robbery.map.HeatmapOverlay
import com.unal.senti_ma.ui.screens.robbery.map.RobberyPointMarkers
import com.unal.senti_ma.ui.screens.robbery.map.rememberMapViewWithLifecycle
import kotlinx.coroutines.flow.Flow
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.library.R as OsmdroidR
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

@Composable
fun RobberyContent(
    modifier: Modifier = Modifier,
    uiState: RobberyUiState,
    currentLocation: Coordinates?,
    displayMode: RobberyDisplayMode,
    selectedAddress: String,
    selectedType: String?,
    selectedFromTimestamp: Long?,
    selectedToTimestamp: Long?,
    showDateDialog: Boolean,
    filtersExpanded: Boolean,
    viewModelEvent: Flow<RobberyViewModelEvent>,
    onFiltersExpandedChange: (Boolean) -> Unit,
    onDateDialogChange: (Boolean) -> Unit,
    onAddressChange: (String) -> Unit,
    onSearchAddress: () -> Unit,
    onTypeSelected: (String?) -> Unit,
    onDateRangeConfirm: (Long?, Long?) -> Unit,
    onDateRangeClear: () -> Unit,
    onMapPositionChanged: (
        MapBounds,
        Coordinates
    ) -> Unit,
    onToggleDisplayMode: () -> Unit,
    onRobberyClick: (String) -> Unit
) {
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle(context)

    val heatmapOverlay = remember {
        HeatmapOverlay()
    }

    val userLocationMarker = remember(
        mapView,
        context
    ) {
        Marker(mapView).apply {
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
    }

    val currentOnRobberyClick by rememberUpdatedState(
        onRobberyClick
    )

    val pointMarkers = remember(mapView) {
        RobberyPointMarkers(
            mapView = mapView,
            onRobberyClick = { id ->
                currentOnRobberyClick(id)
            }
        )
    }

    var hasCenteredOnLocation by remember {
        mutableStateOf(false)
    }

    fun updateMapPosition() {
        val center = mapView.mapCenter as GeoPoint
        val bounds = mapView.boundingBox

        onMapPositionChanged(
            MapBounds(
                northLat = bounds.latNorth,
                southLat = bounds.latSouth,
                eastLon = bounds.lonEast,
                westLon = bounds.lonWest
            ),
            Coordinates(
                latitude = center.latitude,
                longitude = center.longitude
            )
        )
    }

    DisposableEffect(mapView) {
        val listener = object : MapListener {

            override fun onScroll(
                event: ScrollEvent?
            ): Boolean {
                updateMapPosition()
                return true
            }

            override fun onZoom(
                event: ZoomEvent?
            ): Boolean {
                updateMapPosition()
                return true
            }
        }

        mapView.addMapListener(listener)

        onDispose {
            mapView.removeMapListener(listener)
        }
    }

    DisposableEffect(
        mapView,
        userLocationMarker
    ) {
        onDispose {
            mapView.overlays.remove(
                userLocationMarker
            )

            mapView.invalidate()
        }
    }

    DisposableEffect(mapView) {
        onDispose {
            pointMarkers.clear()
        }
    }

    LaunchedEffect(currentLocation) {
        val location = currentLocation
            ?: return@LaunchedEffect

        val geoPoint = GeoPoint(
            location.latitude,
            location.longitude
        )

        userLocationMarker.position = geoPoint

        if (!mapView.overlays.contains(userLocationMarker)) {
            mapView.overlays.add(userLocationMarker)
        }

        mapView.invalidate()

        if (!hasCenteredOnLocation) {
            hasCenteredOnLocation = true

            mapView.post {
                mapView.controller.setZoom(15.0)

                mapView.controller.animateTo(
                    geoPoint
                )

                updateMapPosition()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModelEvent.collect { event ->
            if (event is RobberyViewModelEvent.MoveMapToLocation) {
                mapView.controller.setZoom(15.0)

                mapView.controller.setCenter(
                    GeoPoint(
                        event.coordinates.latitude,
                        event.coordinates.longitude
                    )
                )

                mapView.post {
                    updateMapPosition()
                }
            }
        }
    }

    LaunchedEffect(
        uiState,
        displayMode
    ) {
        if (uiState is RobberyUiState.Success) {
            val data = uiState.robberyMapData

            val isHeatmap =
                displayMode == RobberyDisplayMode.HEATMAP

            heatmapOverlay.updatePoints(
                data.heatmapPoints
            )

            heatmapOverlay.isVisible = isHeatmap

            pointMarkers.updatePoints(
                data.robberyPoints
            )

            pointMarkers.setVisible(
                !isHeatmap
            )

            mapView.invalidate()
        }
    }

    if (showDateDialog) {
        RobberyDateRangeDialog(
            initialStartDateMillis =
                selectedFromTimestamp,
            initialEndDateMillis =
                selectedToTimestamp,
            onConfirm = onDateRangeConfirm,
            onDismiss = {
                onDateDialogChange(false)
            },
            onClear = onDateRangeClear
        )
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 12.dp,
                    start = 12.dp,
                    end = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = selectedAddress,
                onValueChange = onAddressChange,
                label = {
                    Text(
                        text = stringResource(
                            R.string.text_robbery_address_label
                        )
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(
                            R.string.text_robbery_address_placeholder
                        )
                    )
                },
                singleLine = true,
                trailingIcon = {
                    if (selectedAddress.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                onAddressChange("")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null
                            )
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            IconButton(
                onClick = onSearchAddress,
                enabled = selectedAddress.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(
                        R.string.description_robbery_search_address
                    )
                )
            }
        }

        RobberyFilters(
            expanded = filtersExpanded,
            onExpandedChange = onFiltersExpandedChange,
            selectedType = selectedType,
            selectedFromTimestamp =
                selectedFromTimestamp,
            selectedToTimestamp =
                selectedToTimestamp,
            onTypeSelected = onTypeSelected,
            onDateClick = {
                onDateDialogChange(true)
            }
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        HorizontalDivider()

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clipToBounds()
        ) {
            AndroidView(
                factory = {
                    mapView.apply {
                        setTileSource(
                            TileSourceFactory.MAPNIK
                        )

                        setMultiTouchControls(true)

                        isHorizontalMapRepetitionEnabled = false
                        isVerticalMapRepetitionEnabled = false

                        controller.setZoom(13.0)

                        controller.setCenter(
                            GeoPoint(
                                4.60971,
                                -74.08175
                            )
                        )

                        if (!overlays.contains(heatmapOverlay)) {
                            overlays.add(heatmapOverlay)
                        }

                        post {
                            updateMapPosition()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .clipToBounds(),
                update = {}
            )

            FloatingActionButton(
                onClick = {
                    if (currentLocation != null) {
                        val geoPoint = GeoPoint(
                            currentLocation.latitude,
                            currentLocation.longitude
                        )

                        mapView.controller.setZoom(15.0)

                        mapView.controller.animateTo(
                            geoPoint
                        )

                        mapView.post {
                            updateMapPosition()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            R.string.text_location_not_available_yet,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = 16.dp,
                        bottom = 88.dp
                    ),
                containerColor = Color.White,
                contentColor =
                    MaterialTheme.colorScheme.primary,
                elevation =
                    FloatingActionButtonDefaults.elevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription =
                        stringResource(
                            R.string.description_robbery_my_location
                        ),
                    modifier = Modifier.size(32.dp)
                )
            }

            FloatingActionButton(
                onClick = onToggleDisplayMode,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                val isHeatmap =
                    displayMode == RobberyDisplayMode.HEATMAP

                Icon(
                    imageVector = if (isHeatmap) {
                        Icons.Default.LocationOn
                    } else {
                        Icons.Default.Whatshot
                    },
                    contentDescription = stringResource(
                        if (isHeatmap) {
                            R.string.description_robbery_show_points
                        } else {
                            R.string.description_robbery_show_heatmap
                        }
                    )
                )
            }

            if (uiState is RobberyUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}
