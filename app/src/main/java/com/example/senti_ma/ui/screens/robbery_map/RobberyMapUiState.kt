package com.example.senti_ma.ui.screens.robbery_map

import com.example.senti_ma.domain.model.HeatmapPoint

sealed class MapUiState {
    data object Idle : MapUiState()
    data object Loading : MapUiState()
    data class Success(val heatPoints: List<HeatmapPoint>) : MapUiState()
    data class Error(val message: String) : MapUiState()
}
