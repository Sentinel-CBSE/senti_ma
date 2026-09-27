package com.unal.senti_ma.ui.screens.profile

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.model.EmergencyContact
import com.unal.senti_ma.ui.location.LocationViewModel
import com.unal.senti_ma.ui.screens.profile.components.EmergencyContactDialog
import com.unal.senti_ma.ui.screens.profile.components.ProfileContent
import com.unal.senti_ma.ui.screens.profile.events.ProfileUiEvent
import com.unal.senti_ma.ui.screens.profile.events.ProfileViewModelEvent
import com.unal.senti_ma.ui.settings.SettingsViewModel
import com.unal.senti_ma.ui.shared.permissions.LocationTrackingPermissionHandler
import com.unal.senti_ma.ui.shared.permissions.PermissionViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel(),
    permissionViewModel: PermissionViewModel = hiltViewModel()
) {
    val profileUiState by profileViewModel.uiState.collectAsState()
    val profileFormState by profileViewModel.formState.collectAsState()

    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
    val isLocationTrackingEnabled by settingsViewModel.isLocationTrackingEnabled.collectAsState()
    val backgroundLocationPermissionGranted by permissionViewModel.backgroundLocationPermissionGranted.collectAsState()

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isEditing by rememberSaveable {
        mutableStateOf(false)
    }

    var isUpdating by rememberSaveable {
        mutableStateOf(false)
    }

    var requestLocationTrackingPermission by rememberSaveable {
        mutableStateOf(false)
    }

    var showAddContactDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var editingContact by remember {
        mutableStateOf<EmergencyContact?>(null)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                permissionViewModel.refreshLocationPermission()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(
        isLocationTrackingEnabled,
        backgroundLocationPermissionGranted
    ) {
        if (
            isLocationTrackingEnabled &&
            !backgroundLocationPermissionGranted
        ) {
            locationViewModel.stopTracking()
            settingsViewModel.setLocationTrackingEnabled(false)
        }
    }

    LocationTrackingPermissionHandler(
        request = requestLocationTrackingPermission,
        onPermissionGranted = {
            requestLocationTrackingPermission = false

            settingsViewModel.setLocationTrackingEnabled(true)
            locationViewModel.startTracking()
        },
        onPermissionDenied = {
            requestLocationTrackingPermission = false
        }
    )

    LaunchedEffect(Unit) {
        profileViewModel.viewModelEvent.collect { event ->
            when (event) {
                is ProfileViewModelEvent.Success -> {
                    isUpdating = false
                    isEditing = false

                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ProfileViewModelEvent.Error -> {
                    isUpdating = false

                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    ProfileContent(
        modifier = modifier,
        uiState = profileUiState,
        formState = profileFormState,
        isEditing = isEditing,
        isUpdating = isUpdating,
        isDarkTheme = isDarkTheme,
        isLocationTrackingEnabled = isLocationTrackingEnabled,

        onToggleTheme = { enabled ->
            settingsViewModel.setDarkTheme(enabled)
        },

        onToggleLocationTracking = { enabled ->
            if (enabled) {
                requestLocationTrackingPermission = true
            } else {
                locationViewModel.stopTracking()
                settingsViewModel.setLocationTrackingEnabled(false)
            }
        },

        onEvent = { event ->
            when (event) {
                is ProfileUiEvent.StartEditing -> {
                    isEditing = true
                    profileViewModel.onEvent(event)
                }

                is ProfileUiEvent.CancelEditing -> {
                    isEditing = false
                    profileViewModel.onEvent(event)
                }

                is ProfileUiEvent.SaveProfile -> {
                    isUpdating = true
                    profileViewModel.onEvent(event)
                }

                is ProfileUiEvent.AddEmergencyContact -> {
                    isUpdating = true
                    profileViewModel.onEvent(event)
                }

                is ProfileUiEvent.UpdateEmergencyContact -> {
                    isUpdating = true
                    profileViewModel.onEvent(event)
                }

                is ProfileUiEvent.DeleteEmergencyContact -> {
                    isUpdating = true
                    profileViewModel.onEvent(event)
                }

                is ProfileUiEvent.UpdateProfileDraft -> {
                    profileViewModel.onEvent(event)
                }

                is ProfileUiEvent.SignOut -> {
                    profileViewModel.onEvent(event)
                }
            }
        },

        onAddContact = {
            showAddContactDialog = true
        },

        onEditContact = {
            editingContact = it
        }
    )

    if (showAddContactDialog) {
        EmergencyContactDialog(
            title = stringResource(
                R.string.title_add_emergency_contact
            ),
            onDismiss = {
                showAddContactDialog = false
            },
            onConfirm = { name, phone, relationship ->
                isUpdating = true

                profileViewModel.onEvent(
                    ProfileUiEvent.AddEmergencyContact(
                        EmergencyContact(
                            uid = "",
                            name = name,
                            phoneNumber = phone,
                            relationship = relationship
                        )
                    )
                )

                showAddContactDialog = false
            }
        )
    }

    editingContact?.let { contact ->
        EmergencyContactDialog(
            title = stringResource(
                R.string.title_edit_emergency_contact
            ),
            initialName = contact.name,
            initialPhone = contact.phoneNumber,
            initialRelationship = contact.relationship,
            onDismiss = {
                editingContact = null
            },
            onConfirm = { name, phone, relationship ->
                isUpdating = true

                profileViewModel.onEvent(
                    ProfileUiEvent.UpdateEmergencyContact(
                        contact.copy(
                            name = name,
                            phoneNumber = phone,
                            relationship = relationship
                        )
                    )
                )

                editingContact = null
            }
        )
    }
}
