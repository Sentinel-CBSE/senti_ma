package com.unal.senti_ma.ui.screens.robbery

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.enums.PermissionStatus
import com.unal.senti_ma.domain.enums.RobberyDisplayMode
import com.unal.senti_ma.ui.screens.robbery.components.RobberyContent
import com.unal.senti_ma.ui.screens.robbery.events.RobberyUiEvent
import com.unal.senti_ma.ui.screens.robbery.events.RobberyViewModelEvent
import com.unal.senti_ma.ui.shared.permissions.PermissionViewModel

@Composable
fun RobberyScreen(
    modifier: Modifier = Modifier,
    robberyViewModel: RobberyViewModel = hiltViewModel(),
    permissionViewModel: PermissionViewModel = hiltViewModel(),
    onRobberyClick: (String) -> Unit
) {
    val uiState by robberyViewModel.uiState.collectAsStateWithLifecycle()
    val formState by robberyViewModel.formState.collectAsStateWithLifecycle()

    val currentLocation by robberyViewModel.currentLocation.collectAsStateWithLifecycle()
    val locationPermissionStatus by permissionViewModel.locationPermissionStatus.collectAsStateWithLifecycle()

    val context = LocalContext.current

    var showDateDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var filtersExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    var displayMode by rememberSaveable {
        mutableStateOf(RobberyDisplayMode.HEATMAP)
    }

    DisposableEffect(locationPermissionStatus) {
        if (locationPermissionStatus == PermissionStatus.GRANTED) {
            robberyViewModel.startLocationUpdates()
        }

        onDispose {
            robberyViewModel.stopLocationUpdates()
        }
    }

    LaunchedEffect(locationPermissionStatus) {
        if (locationPermissionStatus == PermissionStatus.DENIED) {
            Toast.makeText(
                context,
                R.string.text_location_permission_denied,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        robberyViewModel.viewModelEvent.collect { event ->
            if (event is RobberyViewModelEvent.Error) {
                Toast.makeText(
                    context,
                    event.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    RobberyContent(
        modifier = modifier,
        uiState = uiState,
        formState = formState,
        currentLocation = currentLocation,
        displayMode = displayMode,
        showDateDialog = showDateDialog,
        filtersExpanded = filtersExpanded,
        viewModelEvent = robberyViewModel.viewModelEvent,
        onFiltersExpandedChange = {
            filtersExpanded = it
        },
        onDateDialogChange = {
            showDateDialog = it
        },
        onAddressChange = { address ->
            robberyViewModel.onEvent(
                RobberyUiEvent.UpdateAddressSearch(address)
            )
        },
        onSearchAddress = {
            robberyViewModel.onEvent(
                RobberyUiEvent.UpdateMapToPosition
            )
        },
        onTypeSelected = { type: String? ->
            robberyViewModel.onEvent(
                RobberyUiEvent.UpdateTypeFilter(type)
            )
        },
        onDateRangeConfirm = { from, to ->
            showDateDialog = false

            robberyViewModel.onEvent(
                RobberyUiEvent.UpdateDateRangeFilter(
                    fromTimestamp = from,
                    toTimestamp = to
                )
            )
        },
        onDateRangeClear = {
            showDateDialog = false

            robberyViewModel.onEvent(
                RobberyUiEvent.UpdateDateRangeFilter(
                    fromTimestamp = null,
                    toTimestamp = null
                )
            )
        },
        onMapPositionChanged = { mapBounds, center ->
            robberyViewModel.onEvent(
                RobberyUiEvent.UpdateMapPosition(
                    mapBounds = mapBounds,
                    center = center
                )
            )
        },
        onToggleDisplayMode = {
            displayMode =
                when (displayMode) {
                    RobberyDisplayMode.HEATMAP ->
                        RobberyDisplayMode.POINTS

                    RobberyDisplayMode.POINTS ->
                        RobberyDisplayMode.HEATMAP
                }
        },
        onRobberyClick = onRobberyClick
    )
}
