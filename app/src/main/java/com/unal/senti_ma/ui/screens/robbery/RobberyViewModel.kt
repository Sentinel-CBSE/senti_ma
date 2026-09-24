package com.unal.senti_ma.ui.screens.robbery

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unal.senti_ma.domain.enums.RobberyDisplayMode
import com.unal.senti_ma.domain.location.LocationSubscription
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.Coordinates
import com.unal.senti_ma.domain.model.MapBounds
import com.unal.senti_ma.domain.usecase.geocoding.GetCoordinatesFromAddressUseCase
import com.unal.senti_ma.domain.usecase.location.AcquireLocationUpdatesUseCase
import com.unal.senti_ma.domain.usecase.location.ObserveCurrentLocationUseCase
import com.unal.senti_ma.domain.usecase.robbery.GetRobberyMapDataUseCase
import com.unal.senti_ma.ui.screens.robbery.events.RobberyUiEvent
import com.unal.senti_ma.ui.screens.robbery.events.RobberyViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class RobberyViewModel @Inject constructor(
    private val getRobberyMapDataUseCase: GetRobberyMapDataUseCase,
    private val getCoordinatesFromAddressUseCase: GetCoordinatesFromAddressUseCase,
    private val acquireLocationUpdatesUseCase: AcquireLocationUpdatesUseCase,
    observeCurrentLocationUseCase: ObserveCurrentLocationUseCase
) : ViewModel() {

    companion object {
        private const val MAP_RELOAD_DEBOUNCE_MS = 400L
    }

    private val _uiState = MutableStateFlow<RobberyUiState>(RobberyUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _viewModelEvent = MutableSharedFlow<RobberyViewModelEvent>(replay = 0)
    val viewModelEvent = _viewModelEvent.asSharedFlow()

    val currentLocation: StateFlow<Coordinates?> = observeCurrentLocationUseCase()
    private var locationSubscription: LocationSubscription? = null

    var selectedType by mutableStateOf<String?>(null)
        private set

    var selectedFromTimestamp by mutableStateOf<Long?>(null)
        private set

    var selectedToTimestamp by mutableStateOf<Long?>(null)
        private set

    var selectedAddress by mutableStateOf("")
        private set

    var selectedLocation by mutableStateOf<Coordinates?>(null)
        private set

    var displayMode by mutableStateOf(RobberyDisplayMode.HEATMAP)
        private set

    private var reloadJob: Job? = null
    private var addressJob: Job? = null

    private var currentMapBounds: MapBounds? = null

    fun startLocationUpdates() {
        if (locationSubscription != null) {
            return
        }

        locationSubscription = acquireLocationUpdatesUseCase()
    }

    fun stopLocationUpdates() {
        locationSubscription?.close()
        locationSubscription = null
    }

    fun onEvent(event: RobberyUiEvent) {
        when (event) {
            is RobberyUiEvent.ClearState -> clearState()
            is RobberyUiEvent.ToggleDisplayMode -> toggleDisplayMode()
            is RobberyUiEvent.UpdateTypeFilter -> updateTypeFilter(event)
            is RobberyUiEvent.UpdateDateRangeFilter -> updateDateRangeFilter(event)
            is RobberyUiEvent.UpdateAddressSearch -> updateAddressSearch(event)
            is RobberyUiEvent.UpdateMapToPosition -> updateMapToPosition()
            is RobberyUiEvent.UpdateMapPosition -> updateMapPosition(event)
        }
    }

    private fun clearState() {
        reloadJob?.cancel()
        addressJob?.cancel()

        _uiState.value = RobberyUiState.Idle

        selectedType = null
        selectedFromTimestamp = null
        selectedToTimestamp = null
        selectedAddress = ""
        selectedLocation = null
        displayMode = RobberyDisplayMode.HEATMAP
        currentMapBounds = null
    }

    private fun toggleDisplayMode() {
        displayMode =
            when (displayMode) {
                RobberyDisplayMode.HEATMAP ->
                    RobberyDisplayMode.POINTS

                RobberyDisplayMode.POINTS ->
                    RobberyDisplayMode.HEATMAP
            }
    }

    private fun updateTypeFilter(
        event: RobberyUiEvent.UpdateTypeFilter
    ) {
        selectedType = event.type
        reload()
    }

    private fun updateDateRangeFilter(
        event: RobberyUiEvent.UpdateDateRangeFilter
    ) {
        selectedFromTimestamp = event.fromTimestamp
        selectedToTimestamp = event.toTimestamp
        reload()
    }

    private fun updateAddressSearch(
        event: RobberyUiEvent.UpdateAddressSearch
    ) {
        selectedAddress = event.address
    }

    private fun updateMapToPosition() {
        if (selectedAddress.isBlank()) {
            return
        }

        addressJob?.cancel()

        addressJob = viewModelScope.launch {
            when (
                val result =
                    getCoordinatesFromAddressUseCase(
                        selectedAddress
                    )
            ) {
                is AppResult.Success -> {
                    selectedLocation = result.data

                    _viewModelEvent.emit(
                        RobberyViewModelEvent.MoveMapToLocation(
                            result.data
                        )
                    )
                }

                is AppResult.Error -> {
                    _viewModelEvent.emit(
                        RobberyViewModelEvent.Error(
                            result.errorMessage
                        )
                    )
                }

                AppResult.Cancelled -> Unit
            }
        }
    }

    private fun updateMapPosition(
        event: RobberyUiEvent.UpdateMapPosition
    ) {
        currentMapBounds = event.mapBounds
        selectedLocation = event.center

        reload()
    }

    private fun reload() {
        val mapBounds = currentMapBounds
            ?: return

        reloadJob?.cancel()

        reloadJob = viewModelScope.launch {
            delay(MAP_RELOAD_DEBOUNCE_MS.milliseconds)

            _uiState.value = RobberyUiState.Loading

            when (
                val result =
                    getRobberyMapDataUseCase(
                        mapBounds = mapBounds,
                        fromTimestamp = selectedFromTimestamp,
                        toTimestamp = selectedToTimestamp,
                        type = selectedType
                    )
            ) {
                is AppResult.Success -> {
                    _uiState.value =
                        RobberyUiState.Success(
                            robberyMapData = result.data
                        )
                }

                is AppResult.Error -> {
                    _uiState.value = RobberyUiState.Idle

                    _viewModelEvent.emit(
                        RobberyViewModelEvent.Error(
                            result.errorMessage
                        )
                    )
                }

                AppResult.Cancelled -> {
                    _uiState.value = RobberyUiState.Idle
                }
            }
        }
    }

    override fun onCleared() {
        locationSubscription?.close()
        locationSubscription = null
    }

}
