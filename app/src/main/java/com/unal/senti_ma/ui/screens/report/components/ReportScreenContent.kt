package com.unal.senti_ma.ui.screens.report.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.unal.senti_ma.R
import com.unal.senti_ma.ui.screens.report.ReportUiState
import com.unal.senti_ma.ui.screens.report.events.ReportUiEvent

@Composable
fun ReportScreenContent(
    uiState: ReportUiState,
    onEvent: (ReportUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = stringResource(
                R.string.title_report
            ),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = stringResource(
                R.string.text_report_description
            ),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        ReportTypeButton(
            type = "armed_robbery",
            text = stringResource(
                R.string.text_report_armed_robbery
            ),
            icon = Icons.Default.Warning,
            state = uiState,
            onStartHolding = { type ->
                onEvent(
                    ReportUiEvent.StartHolding(type)
                )
            },
            onStopHolding = {
                onEvent(
                    ReportUiEvent.StopHolding
                )
            },
            onCancel = {
                onEvent(
                    ReportUiEvent.CancelReport
                )
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        ReportTypeButton(
            type = "theft",
            text = stringResource(
                R.string.text_report_theft
            ),
            icon = Icons.AutoMirrored.Filled.DirectionsWalk,
            state = uiState,
            onStartHolding = { type ->
                onEvent(
                    ReportUiEvent.StartHolding(type)
                )
            },
            onStopHolding = {
                onEvent(
                    ReportUiEvent.StopHolding
                )
            },
            onCancel = {
                onEvent(
                    ReportUiEvent.CancelReport
                )
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        ReportTypeButton(
            type = "burglary",
            text = stringResource(
                R.string.text_report_burglary
            ),
            icon = Icons.Default.Home,
            state = uiState,
            onStartHolding = { type ->
                onEvent(
                    ReportUiEvent.StartHolding(type)
                )
            },
            onStopHolding = {
                onEvent(
                    ReportUiEvent.StopHolding
                )
            },
            onCancel = {
                onEvent(
                    ReportUiEvent.CancelReport
                )
            }
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        when (uiState) {
            ReportUiState.Idle -> Unit

            is ReportUiState.Holding -> {
                Text(
                    text = stringResource(
                        R.string.text_hold_to_report
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            is ReportUiState.Confirming -> {
                Text(
                    text = stringResource(
                        R.string.text_report_confirming
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            ReportUiState.Creating -> {
                ReportCreating()
            }

            is ReportUiState.Error -> {
                ReportError(
                    message = uiState.message
                )
            }
        }
    }
}
