package com.unal.senti_ma.ui.auth

import com.unal.senti_ma.domain.model.User

sealed interface AuthState {
    data object Loading : AuthState
    data object Unauthenticated : AuthState
    data class Authenticated(val user: User) : AuthState
}
