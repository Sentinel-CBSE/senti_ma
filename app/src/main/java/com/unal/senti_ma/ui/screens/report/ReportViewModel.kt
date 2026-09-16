package com.unal.senti_ma.ui.screens.report

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.Coordinates
import com.unal.senti_ma.domain.usecase.RobberyUseCases
import com.unal.senti_ma.ui.screens.report.events.ReportUiEvent
import com.unal.senti_ma.ui.screens.report.events.ReportViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ReportViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val robberyUseCases: RobberyUseCases
) : ViewModel() {

    companion object {
        private const val HOLD_DURATION_MILLIS = 3_000L
        private const val CONFIRMATION_DURATION_MILLIS = 3_000L
    }

    private val _uiState =
        MutableStateFlow<ReportUiState>(ReportUiState.Idle)

    val uiState = _uiState.asStateFlow()

    private val _viewModelEvent =
        MutableSharedFlow<ReportViewModelEvent>()

    val viewModelEvent = _viewModelEvent.asSharedFlow()

    private var currentLocation: Coordinates? = null

    private var timerJob: Job? = null

    fun onEvent(event: ReportUiEvent) {
        when (event) {
            is ReportUiEvent.UpdateLocation -> {
                updateLocation(event)
            }

            is ReportUiEvent.StartHolding -> {
                startHolding(event.type)
            }

            ReportUiEvent.StopHolding -> {
                stopHolding()
            }

            ReportUiEvent.CancelReport -> {
                cancelReport()
            }
        }
    }

    private fun updateLocation(
        event: ReportUiEvent.UpdateLocation
    ) {
        currentLocation = event.coordinates
    }

    private fun startHolding(type: String) {
        if (_uiState.value !is ReportUiState.Idle) {
            return
        }

        timerJob?.cancel()

        _uiState.value = ReportUiState.Holding(
            type = type
        )

        timerJob = viewModelScope.launch {
            delay(HOLD_DURATION_MILLIS.milliseconds)

            _uiState.value = ReportUiState.Confirming(
                type = type
            )

            delay(CONFIRMATION_DURATION_MILLIS.milliseconds)

            createReport(type)
        }
    }

    private fun stopHolding() {
        if (_uiState.value is ReportUiState.Holding) {
            timerJob?.cancel()
            timerJob = null

            _uiState.value = ReportUiState.Idle
        }
    }

    private fun cancelReport() {
        timerJob?.cancel()
        timerJob = null

        _uiState.value = ReportUiState.Idle
    }

    private fun createReport(type: String) {
        timerJob = null

        val location = currentLocation

        if (location == null) {
            _uiState.value = ReportUiState.Error(
                message = context.getString(
                    R.string.text_location_unavailable
                )
            )
            return
        }

        _uiState.value = ReportUiState.Creating

        viewModelScope.launch {
            when (
                val result = robberyUseCases.createRobberyReport(
                    type = type,
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            ) {
                is AppResult.Success -> {
                    _uiState.value = ReportUiState.Idle

                    _viewModelEvent.emit(
                        ReportViewModelEvent.ReportCreated(
                            message = context.getString(
                                R.string.text_report_created
                            )
                        )
                    )
                }

                is AppResult.Error -> {
                    _uiState.value = ReportUiState.Error(
                        message = result.errorMessage
                    )
                }

                AppResult.Cancelled -> {
                    _uiState.value = ReportUiState.Idle
                }
            }
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
    }

}
