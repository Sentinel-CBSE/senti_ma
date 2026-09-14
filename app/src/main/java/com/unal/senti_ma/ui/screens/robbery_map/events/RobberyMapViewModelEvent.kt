package com.unal.senti_ma.ui.screens.robbery_map.events

sealed interface RobberyMapViewModelEvent {

    data class Error(
        val message: String
    ) : RobberyMapViewModelEvent

}
