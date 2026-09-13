package com.unal.senti_ma.ui.screens.login.events

import android.app.Activity

sealed interface LoginUiEvent {
    data object ClearState: LoginUiEvent
    data object SignInAnonymously: LoginUiEvent
    data object SignInWithEmailAndPassword: LoginUiEvent
    data class SignInWithSavedCredentials(val activity: Activity): LoginUiEvent
    data class SignInWithGoogle(val activity: Activity): LoginUiEvent
    data class UpdateUserEmail(val newUserEmail: String): LoginUiEvent
    data class UpdateUserPassword(val newUserPassword: String): LoginUiEvent
}
