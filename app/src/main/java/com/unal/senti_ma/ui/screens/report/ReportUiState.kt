package com.unal.senti_ma.ui.screens.report

sealed class ReportUiState {

    data object Idle : ReportUiState()

    data class Holding(
        val type: String
    ) : ReportUiState()

    data class Confirming(
        val type: String
    ) : ReportUiState()

    data object Creating : ReportUiState()

    data class Error(
        val message: String
    ) : ReportUiState()

}
