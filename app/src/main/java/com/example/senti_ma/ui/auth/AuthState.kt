package com.example.senti_ma.ui.auth

import com.example.senti_ma.domain.model.User

sealed interface AuthState {
    data object Loading : AuthState
    data object Unauthenticated : AuthState
    data class Authenticated(val user: User) : AuthState
}
