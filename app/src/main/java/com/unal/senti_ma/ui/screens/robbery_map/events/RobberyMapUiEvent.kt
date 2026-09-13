package com.unal.senti_ma.ui.screens.robbery_map.events

import com.unal.senti_ma.domain.model.MapBounds

sealed interface RobberyMapUiEvent {
    data object ClearState : RobberyMapUiEvent
    data class UpdateMapBounds(val mapBounds: MapBounds) : RobberyMapUiEvent
    data class UpdateTypeFilter(val type: String?) : RobberyMapUiEvent
    data class UpdateDateRangeFilter(val fromTimestamp: Long?, val toTimestamp: Long?) : RobberyMapUiEvent
}
