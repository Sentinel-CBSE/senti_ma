package com.unal.senti_ma.ui.screens.signUp.events

sealed interface SignUpViewModelEvent {

    data object Success : SignUpViewModelEvent

    data class Error(
        val message: String
    ) : SignUpViewModelEvent

}
