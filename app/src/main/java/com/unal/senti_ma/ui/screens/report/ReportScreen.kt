package com.unal.senti_ma.ui.screens.report

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.model.Coordinates
import com.unal.senti_ma.ui.screens.report.components.ReportScreenContent
import com.unal.senti_ma.ui.screens.report.events.ReportUiEvent
import com.unal.senti_ma.ui.screens.report.events.ReportViewModelEvent
import com.unal.senti_ma.ui.shared.LocationPermissionHandler
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer

@Composable
fun ReportScreen(
    viewModel: ReportViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var hasLocationPermission by remember {
        mutableStateOf(false)
    }

    val locationProvider = remember {
        GpsMyLocationProvider(context)
    }

    val locationConsumer = remember {
        IMyLocationConsumer { location, _ ->
            if (location != null) {
                viewModel.onEvent(
                    ReportUiEvent.UpdateLocation(
                        coordinates = Coordinates(
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                    )
                )
            }
        }
    }

    LocationPermissionHandler(
        onPermissionGranted = {
            hasLocationPermission = true
        },
        onPermissionDenied = {
            hasLocationPermission = false

            Toast.makeText(
                context,
                R.string.text_location_permission_denied,
                Toast.LENGTH_SHORT
            ).show()
        }
    )

    DisposableEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            locationProvider.startLocationProvider(
                locationConsumer
            )
        }

        onDispose {
            locationProvider.stopLocationProvider()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.viewModelEvent.collect { event ->
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

    ReportScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}
