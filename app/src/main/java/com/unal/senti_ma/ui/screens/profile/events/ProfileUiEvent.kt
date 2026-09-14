package com.unal.senti_ma.ui.screens.profile.events

import com.unal.senti_ma.domain.model.EmergencyContact
import com.unal.senti_ma.domain.model.UserUpdate

sealed interface ProfileUiEvent {

    data object SignOut : ProfileUiEvent

    data object StartEditing : ProfileUiEvent

    data object CancelEditing : ProfileUiEvent

    data class UpdateProfileDraft(
        val userUpdate: UserUpdate
    ) : ProfileUiEvent

    data object SaveProfile : ProfileUiEvent

    data class AddEmergencyContact(
        val contact: EmergencyContact
    ) : ProfileUiEvent

    data class UpdateEmergencyContact(
        val contact: EmergencyContact
    ) : ProfileUiEvent

    data class DeleteEmergencyContact(
        val contactUid: String
    ) : ProfileUiEvent

}
