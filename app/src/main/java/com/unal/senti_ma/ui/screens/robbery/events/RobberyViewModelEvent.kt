package com.unal.senti_ma.ui.screens.robbery.events

import com.unal.senti_ma.domain.model.Coordinates

sealed interface RobberyViewModelEvent {

    data class Error(
        val message: String
    ) : RobberyViewModelEvent

    data class MoveMapToLocation(
        val coordinates: Coordinates
    ) : RobberyViewModelEvent

}
