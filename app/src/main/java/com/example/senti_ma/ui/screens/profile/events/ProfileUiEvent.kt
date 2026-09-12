package com.example.senti_ma.ui.screens.profile.events

sealed class ProfileUiEvent {
    data object SignOut : ProfileUiEvent()
}
