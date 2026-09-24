package com.unal.senti_ma.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unal.senti_ma.domain.usecase.auth.ObserveAuthStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    observeAuthStateUseCase: ObserveAuthStateUseCase,
) : ViewModel() {

    val authState: StateFlow<AuthState> = observeAuthStateUseCase()
        .map { user ->
            when {
                user == null -> AuthState.Unauthenticated
                user.isAnonymous -> AuthState.Authenticated(user)
                !user.isEmailVerified -> AuthState.Unauthenticated
                else -> AuthState.Authenticated(user)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AuthState.Loading
        )

}
