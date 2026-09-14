package com.unal.senti_ma.data.mappers

import com.google.firebase.auth.FirebaseUser
import com.unal.senti_ma.domain.model.User

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
