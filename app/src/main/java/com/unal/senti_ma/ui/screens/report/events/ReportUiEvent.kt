package com.unal.senti_ma.ui.screens.report.events

sealed interface ReportUiEvent {

    data class StartHolding(
        val type: String
    ) : ReportUiEvent

    data object StopHolding : ReportUiEvent

    data object UpdateLocation : ReportUiEvent

    data object CancelReport : ReportUiEvent

}
