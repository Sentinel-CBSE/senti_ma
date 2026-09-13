package com.unal.senti_ma.ui.screens.robbery_map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.MapBounds
import com.unal.senti_ma.domain.usecase.RobberyUseCases
import com.unal.senti_ma.ui.screens.robbery_map.events.RobberyMapUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val robberyUseCases: RobberyUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<MapUiState>(MapUiState.Idle)
    val uiState = _uiState.asStateFlow()

    var selectedType by mutableStateOf<String?>(null)
        private set

    var selectedFromTimestamp by mutableStateOf<Long?>(null)
        private set

    var selectedToTimestamp by mutableStateOf<Long?>(null)
        private set

    private var currentMapBounds: MapBounds? = null

    fun onEvent(event: RobberyMapUiEvent) {
        when (event) {
            is RobberyMapUiEvent.ClearState -> clearState()
            is RobberyMapUiEvent.UpdateMapBounds -> updateMapBounds(event)
            is RobberyMapUiEvent.UpdateTypeFilter -> updateTypeFilter(event)
            is RobberyMapUiEvent.UpdateDateRangeFilter -> updateDateRangeFilter(event)
        }
    }

    private fun clearState() {
        _uiState.value = MapUiState.Idle
    }

    private fun updateMapBounds(event: RobberyMapUiEvent.UpdateMapBounds) {
        currentMapBounds = event.mapBounds
        reload()
    }

    private fun updateTypeFilter(event: RobberyMapUiEvent.UpdateTypeFilter) {
        selectedType = event.type
        reload()
    }

    private fun updateDateRangeFilter(event: RobberyMapUiEvent.UpdateDateRangeFilter) {
        selectedFromTimestamp = event.fromTimestamp
        selectedToTimestamp = event.toTimestamp
        reload()
    }

    private fun reload() {
        val mapBounds = currentMapBounds ?: return

        _uiState.value = MapUiState.Loading
        viewModelScope.launch {
            when (
                val result = robberyUseCases.getHeatmapPoints(
                    mapBounds = mapBounds,
                    type = selectedType,
                    fromTimestamp = selectedFromTimestamp,
                    toTimestamp = selectedToTimestamp
                )
            ) {
                is AppResult.Success -> { _uiState.value = MapUiState.Success(result.data) }
                is AppResult.Error -> { _uiState.value = MapUiState.Error(result.errorMessage) }
            }
        }
    }

}
