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
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.enums.RobberyDisplayMode
import com.unal.senti_ma.domain.model.Coordinates
import com.unal.senti_ma.domain.model.MapBounds
import com.unal.senti_ma.ui.screens.robbery.RobberyFormState
import com.unal.senti_ma.ui.screens.robbery.RobberyUiState
import com.unal.senti_ma.ui.screens.robbery.events.RobberyViewModelEvent
import com.unal.senti_ma.ui.screens.robbery.map.RobberyMapController
import kotlinx.coroutines.flow.Flow
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun RobberyContent(
    modifier: Modifier = Modifier,
    uiState: RobberyUiState,
    formState: RobberyFormState,
    currentLocation: Coordinates?,
    displayMode: RobberyDisplayMode,
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

    val currentOnRobberyClick by rememberUpdatedState(
        onRobberyClick
    )

    var mapView by remember {
        mutableStateOf<MapView?>(null)
    }

    var mapController by remember {
        mutableStateOf<RobberyMapController?>(null)
    }

    var hasCenteredOnLocation by remember {
        mutableStateOf(false)
    }

    fun updateMapPosition() {
        val map = mapView ?: return

        val center = map.mapCenter as GeoPoint
        val bounds = map.boundingBox

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
        val map = mapView
            ?: return@DisposableEffect onDispose {}

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

        map.addMapListener(listener)

        onDispose {
            map.removeMapListener(listener)
        }
    }

    LaunchedEffect(
        currentLocation,
        mapController
    ) {
        val map = mapView
            ?: return@LaunchedEffect

        val controller = mapController
            ?: return@LaunchedEffect

        val location = currentLocation
            ?: return@LaunchedEffect

        val geoPoint = GeoPoint(
            location.latitude,
            location.longitude
        )

        controller.userLocationMarker.position = geoPoint

        if (!map.overlays.contains(
                controller.userLocationMarker
            )
        ) {
            map.overlays.add(
                controller.userLocationMarker
            )
        }

        map.invalidate()

        if (!hasCenteredOnLocation) {
            hasCenteredOnLocation = true

            map.controller.setZoom(15.0)

            map.controller.animateTo(
                geoPoint
            )

            map.post {
                updateMapPosition()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModelEvent.collect { event ->
            if (event is RobberyViewModelEvent.MoveMapToLocation) {
                val map = mapView
                    ?: return@collect

                map.controller.setZoom(15.0)

                map.controller.setCenter(
                    GeoPoint(
                        event.coordinates.latitude,
                        event.coordinates.longitude
                    )
                )

                map.post {
                    updateMapPosition()
                }
            }
        }
    }

    LaunchedEffect(
        uiState,
        displayMode,
        mapController
    ) {
        val map = mapView
            ?: return@LaunchedEffect

        val controller = mapController
            ?: return@LaunchedEffect

        if (uiState is RobberyUiState.Success) {
            val data = uiState.robberyMapData

            val isHeatmap =
                displayMode == RobberyDisplayMode.HEATMAP

            controller.heatmapOverlay.updatePoints(
                data.heatmapPoints
            )

            controller.heatmapOverlay.isVisible =
                isHeatmap

            controller.pointMarkers.updatePoints(
                data.robberyPoints
            )

            controller.pointMarkers.setVisible(
                !isHeatmap
            )

            map.invalidate()
        }
    }

    if (showDateDialog) {
        RobberyDateRangeDialog(
            initialStartDateMillis = formState.fromTimestamp,
            initialEndDateMillis = formState.toTimestamp,
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
                value = formState.address,
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
                    if (formState.address.isNotEmpty()) {
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
                enabled = formState.address.isNotBlank()
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
            selectedType = formState.type,
            selectedFromTimestamp = formState.fromTimestamp,
            selectedToTimestamp = formState.toTimestamp,
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
                    MapView(context).apply {
                        id = R.id.map

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

                        val controller =
                            RobberyMapController(
                                mapView = this,
                                context = context,
                                onRobberyClick = {
                                    currentOnRobberyClick(it)
                                }
                            )

                        overlays.add(
                            controller.heatmapOverlay
                        )

                        mapView = this
                        mapController = controller

                        post {
                            updateMapPosition()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .clipToBounds(),
                update = {},
                onRelease = { map ->
                    mapController?.clear()

                    map.onPause()
                    map.onDetach()

                    if (mapView === map) {
                        mapView = null
                        mapController = null
                    }

                    hasCenteredOnLocation = false
                }
            )

            FloatingActionButton(
                onClick = {
                    val map = mapView

                    if (map != null && currentLocation != null) {
                        val geoPoint = GeoPoint(
                            currentLocation.latitude,
                            currentLocation.longitude
                        )

                        map.controller.setZoom(15.0)

                        map.controller.animateTo(
                            geoPoint
                        )

                        map.post {
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
