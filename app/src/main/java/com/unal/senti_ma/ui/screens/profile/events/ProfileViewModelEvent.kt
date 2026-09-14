package com.unal.senti_ma.ui.screens.profile.events

sealed interface ProfileViewModelEvent {

    data class Success(
        val message: String
    ) : ProfileViewModelEvent

    data class Error(
        val message: String
    ) : ProfileViewModelEvent

}
