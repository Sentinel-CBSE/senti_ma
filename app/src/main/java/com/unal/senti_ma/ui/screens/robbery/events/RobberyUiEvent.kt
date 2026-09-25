package com.unal.senti_ma.ui.screens.robbery.events

import com.unal.senti_ma.domain.model.Coordinates
import com.unal.senti_ma.domain.model.MapBounds

sealed interface RobberyUiEvent {

    data object ClearState : RobberyUiEvent

    data class UpdateTypeFilter(
        val type: String?
    ) : RobberyUiEvent

    data class UpdateDateRangeFilter(
        val fromTimestamp: Long?,
        val toTimestamp: Long?
    ) : RobberyUiEvent

    data class UpdateAddressSearch(
        val address: String
    ) : RobberyUiEvent

    data object UpdateMapToPosition : RobberyUiEvent

    data class UpdateMapPosition(
        val mapBounds: MapBounds,
        val center: Coordinates
    ) : RobberyUiEvent

}
