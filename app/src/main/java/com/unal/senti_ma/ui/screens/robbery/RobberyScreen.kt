package com.unal.senti_ma.ui.screens.robbery

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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.enums.RobberyDisplayMode
import com.unal.senti_ma.domain.model.Coordinates
import com.unal.senti_ma.domain.model.MapBounds
import com.unal.senti_ma.ui.screens.robbery.components.RobberyDateRangeDialog
import com.unal.senti_ma.ui.screens.robbery.components.RobberyFilters
import com.unal.senti_ma.ui.screens.robbery.events.RobberyUiEvent
import com.unal.senti_ma.ui.screens.robbery.events.RobberyViewModelEvent
import com.unal.senti_ma.ui.screens.robbery.map.HeatmapOverlay
import com.unal.senti_ma.ui.screens.robbery.map.RobberyPointMarkers
import com.unal.senti_ma.ui.screens.robbery.map.rememberMapViewWithLifecycle
import com.unal.senti_ma.ui.shared.permissions.LocationPermissionHandler
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

@Composable
fun RobberyScreen(
    modifier: Modifier = Modifier,
    robberyViewModel: RobberyViewModel = hiltViewModel(),
    onRobberyClick: (String) -> Unit
) {
    val robberyUiState by robberyViewModel.uiState.collectAsStateWithLifecycle()

    val currentLocation by robberyViewModel
        .currentLocation
        .collectAsStateWithLifecycle()

    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle(context)

    val heatmapOverlay = remember {
        HeatmapOverlay()
    }

    val userLocationMarker = remember(mapView, context) {
        Marker(mapView).apply {
            setAnchor(
                Marker.ANCHOR_CENTER,
                Marker.ANCHOR_CENTER
            )

            icon = androidx.core.content.ContextCompat.getDrawable(
                context,
                org.osmdroid.library.R.drawable.person
            )

            setOnMarkerClickListener { _, _ ->
                true
            }
        }
    }

    val currentOnRobberyClick by rememberUpdatedState(onRobberyClick)
    val pointMarkers = remember(mapView) {
        RobberyPointMarkers(
            mapView = mapView,
            onRobberyClick = { id ->
                currentOnRobberyClick(id)
            }
        )
    }

    var showDateDialog by remember {
        mutableStateOf(false)
    }

    var filtersExpanded by remember {
        mutableStateOf(false)
    }

    var hasLocationPermission by remember {
        mutableStateOf(false)
    }

    var hasCenteredOnLocation by remember {
        mutableStateOf(false)
    }

    fun updateMapPosition() {
        val center = mapView.mapCenter as GeoPoint
        val bounds = mapView.boundingBox

        robberyViewModel.onEvent(
            RobberyUiEvent.UpdateMapPosition(
                mapBounds = MapBounds(
                    northLat = bounds.latNorth,
                    southLat = bounds.latSouth,
                    eastLon = bounds.lonEast,
                    westLon = bounds.lonWest
                ),
                center = Coordinates(
                    latitude = center.latitude,
                    longitude = center.longitude
                )
            )
        )
    }

    LocationPermissionHandler(
        request = true,
        onPermissionGranted = {
            hasLocationPermission = true
        },
        onPermissionDenied = {
            hasLocationPermission = false
            hasCenteredOnLocation = false

            Toast.makeText(
                context,
                R.string.text_location_permission_denied,
                Toast.LENGTH_SHORT
            ).show()
        }
    )

    DisposableEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            robberyViewModel.startLocationUpdates()
        }

        onDispose {
            robberyViewModel.stopLocationUpdates()
        }
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
            mapView.overlays.add(
                userLocationMarker
            )
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
        robberyViewModel.viewModelEvent.collect { event ->
            when (event) {
                is RobberyViewModelEvent.Error -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is RobberyViewModelEvent.MoveMapToLocation -> {
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
    }

    LaunchedEffect(
        robberyUiState,
        robberyViewModel.displayMode
    ) {
        val state = robberyUiState

        if (state is RobberyUiState.Success) {
            val data = state.robberyMapData

            val isHeatmap =
                robberyViewModel.displayMode ==
                        RobberyDisplayMode.HEATMAP

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
                robberyViewModel.selectedFromTimestamp,
            initialEndDateMillis =
                robberyViewModel.selectedToTimestamp,
            onConfirm = { from, to ->
                showDateDialog = false

                robberyViewModel.onEvent(
                    RobberyUiEvent.UpdateDateRangeFilter(
                        fromTimestamp = from,
                        toTimestamp = to
                    )
                )
            },
            onDismiss = {
                showDateDialog = false
            },
            onClear = {
                showDateDialog = false

                robberyViewModel.onEvent(
                    RobberyUiEvent.UpdateDateRangeFilter(
                        fromTimestamp = null,
                        toTimestamp = null
                    )
                )
            }
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
                value = robberyViewModel.selectedAddress,
                onValueChange = { address ->
                    robberyViewModel.onEvent(
                        RobberyUiEvent.UpdateAddressSearch(
                            address
                        )
                    )
                },
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
                    if (
                        robberyViewModel.selectedAddress
                            .isNotEmpty()
                    ) {
                        IconButton(
                            onClick = {
                                robberyViewModel.onEvent(
                                    RobberyUiEvent.UpdateAddressSearch(
                                        ""
                                    )
                                )
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
                onClick = {
                    robberyViewModel.onEvent(
                        RobberyUiEvent.UpdateMapToPosition
                    )
                },
                enabled =
                    robberyViewModel.selectedAddress
                        .isNotBlank()
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
            onExpandedChange = {
                filtersExpanded = it
            },
            selectedType =
                robberyViewModel.selectedType,
            selectedFromTimestamp =
                robberyViewModel.selectedFromTimestamp,
            selectedToTimestamp =
                robberyViewModel.selectedToTimestamp,
            onTypeSelected = { type ->
                robberyViewModel.onEvent(
                    RobberyUiEvent.UpdateTypeFilter(
                        type
                    )
                )
            },
            onDateClick = {
                showDateDialog = true
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

                        if (
                            !overlays.contains(
                                heatmapOverlay
                            )
                        ) {
                            overlays.add(
                                heatmapOverlay
                            )
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
                    val location = currentLocation

                    if (location != null) {
                        val geoPoint = GeoPoint(
                            location.latitude,
                            location.longitude
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
                onClick = {
                    robberyViewModel.onEvent(
                        RobberyUiEvent.ToggleDisplayMode
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                val isHeatmap =
                    robberyViewModel.displayMode ==
                            RobberyDisplayMode.HEATMAP

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

            if (
                robberyUiState is RobberyUiState.Loading
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}
