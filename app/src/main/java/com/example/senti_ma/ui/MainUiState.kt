package com.example.senti_ma.ui

import com.example.senti_ma.domain.model.User

/**
 * Represents the different states of the main UI.
 */
data class MainUiState(
    val user: User? = null,
)
