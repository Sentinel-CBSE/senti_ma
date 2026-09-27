package com.unal.senti_ma.ui.screens.report.events

sealed interface ReportViewModelEvent {

    data class Error(
        val message: String
    ) : ReportViewModelEvent

    data class ReportCreated(
        val message: String
    ) : ReportViewModelEvent

}
