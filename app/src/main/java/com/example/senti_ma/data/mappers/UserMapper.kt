package com.example.senti_ma.data.mappers

import com.example.senti_ma.domain.model.User
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toDomain(): User = User(
    uid = uid,
    email = email,
    photoUrl = photoUrl,
    displayName = displayName,
    isAnonymous = isAnonymous,
    isEmailVerified = isEmailVerified
)
