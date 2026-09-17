package com.unal.senti_ma.ui.screens.profile

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unal.senti_ma.R
import com.unal.senti_ma.data.mappers.toUserUpdate
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.User
import com.unal.senti_ma.domain.model.UserUpdate
import com.unal.senti_ma.domain.usecase.AuthUseCases
import com.unal.senti_ma.domain.usecase.UserUseCases
import com.unal.senti_ma.ui.screens.profile.events.ProfileUiEvent
import com.unal.senti_ma.ui.screens.profile.events.ProfileViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authUseCases: AuthUseCases,
    private val userUseCases: UserUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _viewModelEvent = MutableSharedFlow<ProfileViewModelEvent>(replay = 0)
    val viewModelEvent = _viewModelEvent.asSharedFlow()

    var userUpdate by mutableStateOf<UserUpdate?>(null)
        private set

    var isEditing by mutableStateOf(false)
        private set

    var isUpdating by mutableStateOf(false)
        private set

    init {
        loadUser()
    }

    private fun loadUser() {
        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            val user = authUseCases.authState
                .filterNotNull()
                .first()

            updateUser(user)
        }
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.SignOut -> signOut()
            is ProfileUiEvent.StartEditing -> startEditing()
            is ProfileUiEvent.CancelEditing -> cancelEditing()
            is ProfileUiEvent.UpdateProfileDraft -> updateProfileDraft(event)
            is ProfileUiEvent.SaveProfile -> saveProfile()
            is ProfileUiEvent.AddEmergencyContact -> addEmergencyContact(event)
            is ProfileUiEvent.UpdateEmergencyContact -> updateEmergencyContact(event)
            is ProfileUiEvent.DeleteEmergencyContact -> deleteEmergencyContact(event)
        }
    }

    private fun startEditing() {
        val state = _uiState.value
        if (state is ProfileUiState.Success) {
            userUpdate = state.user.toUserUpdate()
            isEditing = true
        }
    }

    private fun cancelEditing() {
        val state = _uiState.value
        if (state is ProfileUiState.Success) {
            userUpdate = state.user.toUserUpdate()
            isEditing = false
        }
    }

    private fun updateProfileDraft(
        event: ProfileUiEvent.UpdateProfileDraft
    ) {
        userUpdate = event.userUpdate
    }

    private fun saveProfile() {
        val update = userUpdate ?: return
        isUpdating = true

        viewModelScope.launch {
            when (val result = userUseCases.updateProfile(update)) {

                is AppResult.Success -> {
                    updateUser(result.data)
                    isEditing = false
                    isUpdating = false

                    emitSuccess(
                        context.getString(
                            R.string.text_success_update_profile
                        )
                    )
                }

                is AppResult.Error -> {
                    isUpdating = false
                    emitError(result.errorMessage)
                }

                is AppResult.Cancelled -> {}
            }
        }
    }

    private fun addEmergencyContact(
        event: ProfileUiEvent.AddEmergencyContact
    ) {
        isUpdating = true

        viewModelScope.launch {
            when (
                val result = userUseCases.addEmergencyContact(
                    event.contact
                )
            ) {

                is AppResult.Success -> {
                    updateUser(result.data)
                    isUpdating = false

                    emitSuccess(
                        context.getString(
                            R.string.text_success_add_emergency_contact
                        )
                    )
                }

                is AppResult.Error -> {
                    isUpdating = false
                    emitError(result.errorMessage)
                }

                is AppResult.Cancelled -> {}
            }
        }
    }

    private fun updateEmergencyContact(
        event: ProfileUiEvent.UpdateEmergencyContact
    ) {
        isUpdating = true

        viewModelScope.launch {
            when (
                val result = userUseCases.updateEmergencyContact(
                    event.contact
                )
            ) {

                is AppResult.Success -> {
                    updateUser(result.data)
                    isUpdating = false

                    emitSuccess(
                        context.getString(
                            R.string.text_success_update_emergency_contact
                        )
                    )
                }

                is AppResult.Error -> {
                    isUpdating = false
                    emitError(result.errorMessage)
                }

                is AppResult.Cancelled -> {}
            }
        }
    }

    private fun deleteEmergencyContact(
        event: ProfileUiEvent.DeleteEmergencyContact
    ) {
        isUpdating = true

        viewModelScope.launch {
            when (
                val result = userUseCases.deleteEmergencyContact(
                    event.contactUid
                )
            ) {

                is AppResult.Success -> {
                    updateUser(result.data)
                    isUpdating = false

                    emitSuccess(
                        context.getString(
                            R.string.text_success_delete_emergency_contact
                        )
                    )
                }

                is AppResult.Error -> {
                    isUpdating = false
                    emitError(result.errorMessage)
                }

                is AppResult.Cancelled -> {}
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            authUseCases.signOut()
        }
    }

    private fun updateUser(user: User) {
        userUpdate = user.toUserUpdate()
        _uiState.value = ProfileUiState.Success(user)
    }

    private fun emitSuccess(message: String) {
        viewModelScope.launch {
            _viewModelEvent.emit(
                ProfileViewModelEvent.Success(message)
            )
        }
    }

    private fun emitError(message: String) {
        viewModelScope.launch {
            _viewModelEvent.emit(
                ProfileViewModelEvent.Error(message)
            )
        }
    }

}
