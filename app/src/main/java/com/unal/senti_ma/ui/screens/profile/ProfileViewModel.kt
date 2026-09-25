package com.unal.senti_ma.ui.screens.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unal.senti_ma.R
import com.unal.senti_ma.data.mappers.toUserUpdate
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.User
import com.unal.senti_ma.domain.usecase.auth.ObserveAuthStateUseCase
import com.unal.senti_ma.domain.usecase.auth.SignOutUseCase
import com.unal.senti_ma.domain.usecase.user.AddEmergencyContactUseCase
import com.unal.senti_ma.domain.usecase.user.DeleteEmergencyContactUseCase
import com.unal.senti_ma.domain.usecase.user.UpdateEmergencyContactUseCase
import com.unal.senti_ma.domain.usecase.user.UpdateProfileUseCase
import com.unal.senti_ma.ui.screens.profile.events.ProfileUiEvent
import com.unal.senti_ma.ui.screens.profile.events.ProfileViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val addEmergencyContactUseCase: AddEmergencyContactUseCase,
    private val updateEmergencyContactUseCase: UpdateEmergencyContactUseCase,
    private val deleteEmergencyContactUseCase: DeleteEmergencyContactUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(ProfileFormState())
    val formState: StateFlow<ProfileFormState> = _formState.asStateFlow()

    private val _viewModelEvent = MutableSharedFlow<ProfileViewModelEvent>(replay = 0)
    val viewModelEvent = _viewModelEvent.asSharedFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            val user = observeAuthStateUseCase()
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
            _formState.value = ProfileFormState(
                userUpdate = state.user.toUserUpdate()
            )
        }
    }

    private fun cancelEditing() {
        val state = _uiState.value

        if (state is ProfileUiState.Success) {
            _formState.value = ProfileFormState(
                userUpdate = state.user.toUserUpdate()
            )
        }
    }

    private fun updateProfileDraft(
        event: ProfileUiEvent.UpdateProfileDraft
    ) {
        _formState.update {
            it.copy(
                userUpdate = event.userUpdate
            )
        }
    }

    private fun saveProfile() {
        val update = _formState.value.userUpdate
            ?: return

        viewModelScope.launch {
            when (
                val result = updateProfileUseCase(update)
            ) {

                is AppResult.Success -> {
                    updateUser(result.data)

                    emitSuccess(
                        context.getString(
                            R.string.text_success_update_profile
                        )
                    )
                }

                is AppResult.Error -> {
                    emitError(result.errorMessage)
                }

                is AppResult.Cancelled -> Unit
            }
        }
    }

    private fun addEmergencyContact(
        event: ProfileUiEvent.AddEmergencyContact
    ) {
        viewModelScope.launch {
            when (
                val result = addEmergencyContactUseCase(
                    event.contact
                )
            ) {

                is AppResult.Success -> {
                    updateUser(result.data)

                    emitSuccess(
                        context.getString(
                            R.string.text_success_add_emergency_contact
                        )
                    )
                }

                is AppResult.Error -> {
                    emitError(result.errorMessage)
                }

                is AppResult.Cancelled -> Unit
            }
        }
    }

    private fun updateEmergencyContact(
        event: ProfileUiEvent.UpdateEmergencyContact
    ) {
        viewModelScope.launch {
            when (
                val result = updateEmergencyContactUseCase(
                    event.contact
                )
            ) {

                is AppResult.Success -> {
                    updateUser(result.data)

                    emitSuccess(
                        context.getString(
                            R.string.text_success_update_emergency_contact
                        )
                    )
                }

                is AppResult.Error -> {
                    emitError(result.errorMessage)
                }

                is AppResult.Cancelled -> Unit
            }
        }
    }

    private fun deleteEmergencyContact(
        event: ProfileUiEvent.DeleteEmergencyContact
    ) {
        viewModelScope.launch {
            when (
                val result = deleteEmergencyContactUseCase(
                    event.contactUid
                )
            ) {

                is AppResult.Success -> {
                    updateUser(result.data)

                    emitSuccess(
                        context.getString(
                            R.string.text_success_delete_emergency_contact
                        )
                    )
                }

                is AppResult.Error -> {
                    emitError(result.errorMessage)
                }

                is AppResult.Cancelled -> Unit
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
        }
    }

    private fun updateUser(user: User) {
        _formState.value = ProfileFormState(
            userUpdate = user.toUserUpdate()
        )

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
