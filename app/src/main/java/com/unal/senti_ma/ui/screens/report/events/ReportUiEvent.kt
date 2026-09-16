package com.unal.senti_ma.ui.screens.report.events

import com.unal.senti_ma.domain.model.Coordinates

sealed interface ReportUiEvent {

    data class UpdateLocation(
        val coordinates: Coordinates
    ) : ReportUiEvent

    data class StartHolding(
        val type: String
    ) : ReportUiEvent

    data object StopHolding : ReportUiEvent

    data object CancelReport : ReportUiEvent

}
