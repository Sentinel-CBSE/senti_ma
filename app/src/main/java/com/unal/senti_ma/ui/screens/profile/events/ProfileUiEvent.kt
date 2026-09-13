package com.unal.senti_ma.ui.screens.profile.events

sealed interface ProfileUiEvent {
    data object SignOut : ProfileUiEvent
}
