package com.unal.senti_ma.ui.screens.robbery

import com.unal.senti_ma.domain.model.RobberyMapData

sealed class RobberyUiState {

    data object Idle : RobberyUiState()

    data object Loading : RobberyUiState()

    data class Success(
        val robberyMapData: RobberyMapData
    ) : RobberyUiState()

}
