package com.unal.senti_ma.ui.screens.profile

import com.unal.senti_ma.domain.model.User

sealed class ProfileUiState {

    data object Idle : ProfileUiState()

    data object Loading : ProfileUiState()

    data class Success(
        val user: User
    ) : ProfileUiState()

}
