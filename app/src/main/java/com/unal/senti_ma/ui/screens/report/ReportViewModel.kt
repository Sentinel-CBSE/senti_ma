package com.unal.senti_ma.ui.screens.report

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.Coordinates
import com.unal.senti_ma.domain.usecase.location.GetCurrentLocationUseCase
import com.unal.senti_ma.domain.usecase.robbery.CreateRobberyReportUseCase
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
    private val createRobberyReportUseCase: CreateRobberyReportUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
) : ViewModel() {

    companion object {
        private const val HOLD_DURATION_MILLIS = 3_000L
        private const val CONFIRMATION_DURATION_MILLIS = 3_000L
    }

    private val _uiState = MutableStateFlow<ReportUiState>(ReportUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _viewModelEvent = MutableSharedFlow<ReportViewModelEvent>()
    val viewModelEvent = _viewModelEvent.asSharedFlow()

    private var currentLocation: Coordinates? = null

    private var timerJob: Job? = null

    fun onEvent(event: ReportUiEvent) {
        when (event) {
            is ReportUiEvent.StartHolding -> startHolding(event)
            is ReportUiEvent.StopHolding -> stopHolding()
            is ReportUiEvent.UpdateLocation -> updateLocation()
            is ReportUiEvent.CancelReport -> cancelReport()
        }
    }

    private fun updateLocation() {
        viewModelScope.launch {
            val coordinates = getCurrentLocationUseCase()
            if (coordinates != null) {
                currentLocation = coordinates
            }
        }
    }

    private fun startHolding(event: ReportUiEvent.StartHolding) {
        if (_uiState.value !is ReportUiState.Idle) {
            return
        }

        timerJob?.cancel()

        _uiState.value = ReportUiState.Holding(
            type = event.type
        )

        updateLocation()

        timerJob = viewModelScope.launch {
            delay(HOLD_DURATION_MILLIS.milliseconds)

            _uiState.value = ReportUiState.Confirming(
                type = event.type
            )

            delay(CONFIRMATION_DURATION_MILLIS.milliseconds)
            createReport(event.type)
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
            _uiState.value = ReportUiState.Idle
            viewModelScope.launch {
                _viewModelEvent.emit(
                    ReportViewModelEvent.Error(
                        message = context.getString(
                            R.string.text_location_unavailable
                        )
                    )
                )
            }

            return
        }

        _uiState.value = ReportUiState.Creating
        viewModelScope.launch {
            when (
                val result = createRobberyReportUseCase(
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
                    _uiState.value = ReportUiState.Idle
                    _viewModelEvent.emit(
                        ReportViewModelEvent.Error(
                            message = result.errorMessage
                        )
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
