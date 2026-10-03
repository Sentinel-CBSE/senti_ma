package com.unal.senti_ma.data.mappers

import com.google.firebase.auth.FirebaseUser
import com.unal.senti_ma.data.remote.dto.UserDto
import com.unal.senti_ma.domain.model.User
import com.unal.senti_ma.domain.model.UserUpdate

fun FirebaseUser.toDomain(): User = User(
    uid = uid,
    email = email,
    photoUrl = photoUrl,
    displayName = displayName,
    isAnonymous = isAnonymous,
    isEmailVerified = isEmailVerified,
    emergencyContacts = emptyList(),
    bloodTypeLetter = null,
    bloodTypeRh = null,
    eps = null,
)

fun UserDto.toDomain(): User {
    return User(
        uid = uid,
        email = null,
        photoUrl = null,
        displayName = null,
        isAnonymous = false,
        isEmailVerified = true,
        emergencyContacts = emergencyContacts.map { it.toDomain() },
        bloodTypeLetter = bloodTypeLetter,
        bloodTypeRh = bloodTypeRh,
        eps = eps
    )
}

fun User.toUserUpdate(): UserUpdate {
    return UserUpdate(
        bloodTypeLetter = bloodTypeLetter,
        bloodTypeRh = bloodTypeRh,
        eps = eps.orEmpty()
    )
}
