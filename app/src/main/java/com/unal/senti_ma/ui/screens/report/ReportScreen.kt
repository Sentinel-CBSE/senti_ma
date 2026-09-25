package com.unal.senti_ma.ui.screens.report

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.enums.PermissionStatus
import com.unal.senti_ma.ui.screens.report.components.ReportContent
import com.unal.senti_ma.ui.screens.report.events.ReportUiEvent
import com.unal.senti_ma.ui.screens.report.events.ReportViewModelEvent
import com.unal.senti_ma.ui.shared.permissions.PermissionViewModel

@Composable
fun ReportScreen(
    reportViewModel: ReportViewModel = hiltViewModel(),
    permissionViewModel: PermissionViewModel = hiltViewModel()
) {
    val uiState by reportViewModel.uiState.collectAsStateWithLifecycle()

    val locationPermissionStatus by permissionViewModel.locationPermissionStatus.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(locationPermissionStatus) {
        when (locationPermissionStatus) {
            PermissionStatus.GRANTED -> {
                reportViewModel.onEvent(
                    ReportUiEvent.UpdateLocation
                )
            }

            PermissionStatus.DENIED -> {
                Toast.makeText(
                    context,
                    R.string.text_location_permission_denied,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        reportViewModel.viewModelEvent.collect { event ->
            when (event) {
                is ReportViewModelEvent.ReportCreated -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    ReportContent(
        uiState = uiState,
        onEvent = reportViewModel::onEvent
    )
}
